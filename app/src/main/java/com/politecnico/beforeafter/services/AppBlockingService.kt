package com.politecnico.beforeafter.services

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import com.example.apppracticasjc.Data.RoomDB.BeforeAfterDB
import com.politecnico.beforeafter.R
import com.politecnico.beforeafter.view.BlockedOverlay
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class AppBlockingService : Service() {
    private val handler = Handler(Looper.getMainLooper())
    private val checkInterval = 2000L // every 2 seconds
    private lateinit var db: BeforeAfterDB // your Room database instance
    var remainingTime = 0L



    companion object {
        // isRunning property so the service isn't ran again if it's running
        var isRunning = false
    }


    /**
     * When service starts...
     */
    override fun onCreate() {
        super.onCreate()
        isRunning = true // Sets isRunning to true when service starts
        db = BeforeAfterDB.getDatabase(applicationContext)
    }


    /**
     * ???
     */
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createAndLaunchNotification()

        val appPackageName = intent?.getStringExtra("appPackageName") ?: "App is null"
        val nowMinutesMs = intent?.getLongExtra("nowMinutesMs", 0L) ?: 0L

        // Start timer
        startTimer(nowMinutesMs, appPackageName)

        // App blocking logic
        detectAndDisplayOver()

        return START_STICKY
    }


    private fun startTimer(receivedMs: Long, appPackageName: String) {

        var countdown_timer: CountDownTimer = object : CountDownTimer(
            receivedMs,
            20000
        ) {
            override fun onFinish() {
                CoroutineScope(Dispatchers.IO).launch {
                    db.limitedAppsDao().updateBlocked(appPackageName, true)
                    Log.e("TAG", "onFinish: ACABAO")
                }
            }

            override fun onTick(p0: Long) {
                remainingTime = p0
                Toast.makeText(applicationContext, "$appPackageName: $remainingTime.toString()", Toast.LENGTH_SHORT).show()
            }
        }
        countdown_timer.start   ()

        isRunning = true

    }


    private fun detectAndDisplayOver() {
        handler.post(object : Runnable {
            override fun run() {


                val openedApp = getForegroundAppPackageName()
                if (openedApp != null && isLimitedApp(openedApp)) {
                    // Launch your overlay activity or dialog
                    navigateToBlockedOverlay(openedApp)
                }
                handler.postDelayed(this, checkInterval)
            }
        })
    }


    private fun getForegroundAppPackageName(): String? {
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val endTime = System.currentTimeMillis()
        val beginTime = endTime - 5000

        val usageStatsList = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            beginTime,
            endTime
        )

        if (usageStatsList.isNullOrEmpty()) return null

        val recentStat = usageStatsList.maxByOrNull { it.lastTimeUsed }
        return recentStat?.packageName
    }


    private fun navigateToBlockedOverlay(packageName : String) {
        val intent = Intent(this, BlockedOverlay::class.java).apply {
            putExtra("APP_PACKAGE_NAME", packageName)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        }
        startActivity(intent)
    }


    private fun isLimitedApp(topPackage: String): Boolean {
        var isBlocked = false

        runBlocking {
            val blockedPackages = db.limitedAppsDao().getLimitedPackageNames()
            val blocked = db.limitedAppsDao().getLimited(topPackage)

            // Si está en las apps limitadas y está modo bloqueo, está bloqueado
            isBlocked = blockedPackages.contains(topPackage) && blocked
        }

        return isBlocked
    }


    /**
     * Show service notification
     */
    private fun createAndLaunchNotification() {
        val channelId = "countdown_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "App time usage countdown",
                NotificationManager.IMPORTANCE_HIGH
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("App timer countdown is active")
            .setContentText("Waiting for time to run out...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        startForeground(1, notification)
    }


    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

        val restartServiceIntent = Intent(applicationContext, AppBlockingService::class.java).also {
            it.setPackage(packageName)
        }

        val restartServicePendingIntent = PendingIntent.getService(
            applicationContext,
            1,
            restartServiceIntent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(
            AlarmManager.ELAPSED_REALTIME,
            SystemClock.elapsedRealtime() + 1000, // restart after 1 second
            restartServicePendingIntent
        )
    }


    /**
     * When service stops...
     */
    override fun onDestroy() {
        super.onDestroy()
        isRunning = false // Sets isRunning to false when service stops
    }

    override fun onBind(intent: Intent?): IBinder? = null
}