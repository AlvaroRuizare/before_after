package com.politecnico.beforeafter.services

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.apppracticasjc.Data.RoomDB.BeforeAfterDB
import com.politecnico.beforeafter.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AppBlockingService : Service() {
    private lateinit var db: BeforeAfterDB // Room database instance
    private val activeJobs = mutableMapOf<String, Job>() // Active jobs happening concurrently
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)



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

        // Get data from the 'Now' screen
        val appPackageName = intent?.getStringExtra("appPackageName") ?: return START_NOT_STICKY
        val nowMinutesMs = intent.getLongExtra("nowMinutesMs", 0L)
        val afterMinutesMs = intent.getLongExtra("afterMinutesMs", 0L)


        // If timer coroutine (job) isn't already in progress...
        if (!activeJobs.containsKey(appPackageName)) {
            // Start 'NOW' timer
            val job = serviceScope.launch {
                delay(nowMinutesMs) // Allow usage for X time
                Log.d("TESTING", "Allowing usage of $appPackageName for $nowMinutesMs ms")

                // Get unlock time and store it in SharedPreferences
                val unlockTime = System.currentTimeMillis() + afterMinutesMs
                val prefs = applicationContext.getSharedPreferences("app_locks", Context.MODE_PRIVATE)
                prefs.edit().putLong("${appPackageName}_unlockAt", unlockTime).apply()

                // Set app as blocked
                db.limitedAppsDao().updateBlocked(appPackageName, true)
                Log.d("TESTING", "Blocking $appPackageName for $afterMinutesMs ms")

                delay(afterMinutesMs) // Block app for X time

                // Set app to initial state
                db.limitedAppsDao().updateBlocked(appPackageName, false)
                db.limitedAppsDao().updateLimited(appPackageName, true)
                Log.d("TESTING", "Setting $appPackageName as default")

                // When done, remove from active jobs
                activeJobs.remove(appPackageName)
            }

            activeJobs[appPackageName] = job
        }

        return START_STICKY
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