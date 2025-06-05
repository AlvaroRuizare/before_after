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
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.os.SystemClock
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import com.example.apppracticasjc.Data.RoomDB.BeforeAfterDB
import com.politecnico.beforeafter.MainActivity
import com.politecnico.beforeafter.R
import com.politecnico.beforeafter.navigation.AppScreens
import kotlinx.coroutines.runBlocking

class BackgroundService : Service() {
    private val handler = Handler(Looper.getMainLooper())
    private val checkInterval = 2000L // every 2 seconds
    private lateinit var db: BeforeAfterDB // your Room database instance


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

        // App blocking logic
        startAppMonitoring()

        return START_STICKY
    }

    private fun startAppMonitoring() {
        handler.post(object : Runnable {
            override fun run() {
                val topPackage = getForegroundAppPackageName()
                if (topPackage != null && isBlockedApp(topPackage)) {
                    // Launch your overlay activity or dialog
                    launchOverlay()
                }
                handler.postDelayed(this, checkInterval)
            }
        })
    }


    private fun getForegroundAppPackageName(): String? {
        val usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val endTime = System.currentTimeMillis()
        val beginTime = endTime - 10000

        val usageStatsList = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            beginTime,
            endTime
        )

        if (usageStatsList.isNullOrEmpty()) return null

        val recentStat = usageStatsList.maxByOrNull { it.lastTimeUsed }
        return recentStat?.packageName
    }


    private fun launchOverlay() {
        val intent = Intent(this, MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        startActivity(intent)
    }


    private fun isBlockedApp(topPackage: String): Boolean {
        var isBlocked = false

        runBlocking {
            val blockedPackages = db.limitedAppsDao().getLimitedPackageNames()
            isBlocked = blockedPackages.contains(topPackage)
        }

        return isBlocked
    }


    /**
     * Show service notification
     */
    private fun createAndLaunchNotification() {
        val channelId = "limite_tiempo_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Monitor de uso de apps",
                NotificationManager.IMPORTANCE_HIGH
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Background service is active")
            .setContentText("Waiting for limited apps to be open...")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        startForeground(1, notification)
    }


    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)

        val restartServiceIntent = Intent(applicationContext, BackgroundService::class.java).also {
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