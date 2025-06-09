package com.politecnico.beforeafter.view

import android.app.Activity
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.politecnico.beforeafter.viewmodel.SettingsPagerViewModel
import com.politecnico.beforeafter.viewmodel.SettingsPagerViewModelFactory

// ViewPager that contains the different necessary setup steps

data class PagerContent(val titulo: String, val intentAjuste: String?, @DrawableRes val imagen: Int? = null)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SettingsPager(navController: NavHostController, activity : Activity) {
    val context = LocalContext.current
    val settingsPagerViewModel : SettingsPagerViewModel = viewModel( // ViewModel global que sobrevive a cambios de configuracion
        factory = SettingsPagerViewModelFactory()
    )
    val settingsPagerUiState by settingsPagerViewModel.publicState.collectAsState()

    // Updated HorizontalPager info
    val pagerState = rememberPagerState(
        pageCount = { 4 }
    )

    // List of the contents of each page of the HorizontalPager
    val pagerContentList = settingsPagerUiState.pagerContentList

    // HorizontalPager
    HorizontalPager(state = pagerState) { page ->
        // Everytime the HorizontalPager is swiped...
        PagerScreen(pagerContentList[page], activity, navController) // Content of the next page updates
    }
}