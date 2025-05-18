package com.politecnico.beforeafter.data.model

import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import com.politecnico.beforeafter.view.PagerContent

data class SettingsPagerUiState @RequiresApi(Build.VERSION_CODES.O) constructor(
    var valorCampoUsuario : String = "",

    var pagerContentList: List<PagerContent> = listOf(
        PagerContent("Usage access", Settings.ACTION_USAGE_ACCESS_SETTINGS),
        PagerContent("Battery optimization", Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS),
        PagerContent("Notifications", Settings.ACTION_APP_NOTIFICATION_SETTINGS),
        PagerContent("All ready?", null)
    )
)