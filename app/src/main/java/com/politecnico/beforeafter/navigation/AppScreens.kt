package com.politecnico.beforeafter.navigation

// Different screens that we can navigate
sealed class AppScreens(val route : String) { // Class that receives route parameter
    object Startup : AppScreens("startup_screen")
    object SettingsPagerScreen : AppScreens("settings_pager_screen")
    object BeforeAfterSettings : AppScreens("beforeafter_settings_screen")
    object AppSelector : AppScreens("app_selector_screen")
}
