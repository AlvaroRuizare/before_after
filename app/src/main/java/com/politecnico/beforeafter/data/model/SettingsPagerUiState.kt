package com.politecnico.beforeafter.data.model

import android.os.Build
import android.provider.Settings
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.ui.res.painterResource
import com.politecnico.beforeafter.R
import com.politecnico.beforeafter.view.PagerContent

data class SettingsPagerUiState @RequiresApi(Build.VERSION_CODES.O) constructor(
    var valorCampoUsuario : String = "",

    // Content of each HorizontalPager screen
    var pagerContentList: List<PagerContent> = listOf(
        PagerContent("Usage access", Settings.ACTION_USAGE_ACCESS_SETTINGS, R.drawable.usage_access),
        PagerContent("Battery optimization", Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS, R.drawable.battery_optimization),
        PagerContent("Notifications", Settings.ACTION_APP_NOTIFICATION_SETTINGS, R.drawable.notifications),
        PagerContent("All ready?", null, R.drawable.all_ready)
    )
)