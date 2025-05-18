package com.politecnico.beforeafter.view

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.politecnico.beforeafter.viewmodel.SettingsPagerViewModel
import com.politecnico.beforeafter.viewmodel.SettingsPagerViewModelFactory

data class PagerContent(val titulo: String, val intentAjuste: String?)

// ViewPager that contains the different necessary setup steps

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsPager(navController: NavHostController, activity : Activity) {
    val context = LocalContext.current
    val settingsPagerViewModel : SettingsPagerViewModel = viewModel( // ViewModel global que sobrevive a cambios de configuracion
        factory = SettingsPagerViewModelFactory()
    )
    val settingsPagerUiState by settingsPagerViewModel.estadoPublico.collectAsState()

    // Start background service just once
    LaunchedEffect(Unit) {
        settingsPagerViewModel.startBackgroundService(context)
    }

    // Updated HorizontalPager info
    val pagerState = rememberPagerState(
        pageCount = { 4 }
    )

    // Content objects for the Pager screens
    val pagerContentList = settingsPagerUiState.pagerContentList

    // ViewPager
    HorizontalPager(state = pagerState) { page ->
        // Everytime the viewPager is swiped...
        PagerScreen(pagerContentList[page], activity, navController) // Content of the next page updates
    }
}