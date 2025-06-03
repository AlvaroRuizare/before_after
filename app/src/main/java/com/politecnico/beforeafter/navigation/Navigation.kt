package com.politecnico.beforeafter.navigation

import android.annotation.SuppressLint
import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apppracticasjc.Data.RoomDB.LimitedAppsDao
import com.politecnico.beforeafter.view.AppSelector
import com.politecnico.beforeafter.view.SettingsPager
import com.politecnico.beforeafter.view.BeforeAfterSettings
import com.politecnico.beforeafter.view.Startup

// This is where we manage navigation in the app

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation() {
    val context = LocalContext.current
    val navController = rememberNavController() // Creamos variable con el NavController por defecto para enviarsela al NavHost
    var startDestination by remember { mutableStateOf<String?>(null) }
    var limitedAppsDao: LimitedAppsDao? = null

    // Check permissions...
    LaunchedEffect(Unit) {
        val hasUsageAccess = hasUsageStatsPermission(context)
        val isBackgroundAllowed = isBackgroundUsageAllowed(context)
        val hasNotificationPermission = hasNotificationPermission(context)

        // Check settings...
        startDestination = if (hasUsageAccess && isBackgroundAllowed && hasNotificationPermission) {
            AppScreens.BeforeAfterSettings.route // If all settings are enabled, go straight to main page
        } else {
            AppScreens.SettingsPagerScreen.route // If not, go to setup page
        }
    }

    // Wait for permissions check...
    startDestination?.let { startScreen ->
        // NavHost allows to navigate between different Composables
        NavHost(
            navController = navController,
            startDestination = startScreen) // Default screen to start with
        {
            // Startup
            composable(route = AppScreens.Startup.route) {
                Startup(navController)
            }

            // Settings Pager
            composable(route = AppScreens.SettingsPagerScreen.route) { // Pass route defined in AppScreens.kt
                val activity = LocalActivity.current
                activity?.let { // If activity is not null...
                    SettingsPager(navController, it) // Send navController and activity
                }
            }

            // Before & After settings
            composable(route = AppScreens.BeforeAfterSettings.route) {
                BeforeAfterSettings(navController)
            }

            // App Selection list
            composable(route = AppScreens.AppSelector.route) {
                AppSelector(navController)
            }
        }
    }
}

fun hasUsageStatsPermission(context: Context): Boolean {
    val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    val mode = appOps.checkOpNoThrow(
        AppOpsManager.OPSTR_GET_USAGE_STATS,
        android.os.Process.myUid(),
        context.packageName
    )
    return mode == AppOpsManager.MODE_ALLOWED
}

fun isBackgroundUsageAllowed(context: Context): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        val usageStatsManager = context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val standbyBucket = usageStatsManager.appStandbyBucket
        return standbyBucket <= UsageStatsManager.STANDBY_BUCKET_WORKING_SET
    }
    return true // Para versiones antiguas asumimos que sí
}

fun hasNotificationPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
    } else {
        NotificationManagerCompat.from(context).areNotificationsEnabled()
    }
}



