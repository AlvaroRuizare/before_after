package com.politecnico.beforeafter.data.model

import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.politecnico.beforeafter.R
import com.politecnico.beforeafter.view.PagerContent

data class SettingsPagerUiState @RequiresApi(Build.VERSION_CODES.O) constructor(
    var valorCampoUsuario : String = "",

    // Content of each HorizontalPager screen
    var pagerContentList: List<PagerContent> = listOf(
        PagerContent("Display over apps", Settings.ACTION_MANAGE_OVERLAY_PERMISSION, R.drawable.display_over_apps, "Enable"),
        PagerContent("Usage access", Settings.ACTION_USAGE_ACCESS_SETTINGS, R.drawable.usage_access, "Enable"),
        PagerContent("Battery optimization", Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS, R.drawable.battery_optimization, "Disable"),
        PagerContent("Notifications", Settings.ACTION_APP_NOTIFICATION_SETTINGS, R.drawable.notifications, "Enable"),
        PagerContent("All ready?", null, R.drawable.all_ready, "Get started")
    )
)