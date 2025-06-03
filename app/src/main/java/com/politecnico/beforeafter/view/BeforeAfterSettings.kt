package com.politecnico.beforeafter.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.rememberPickerState
import com.example.apppracticasjc.Data.RoomDB.BeforeAfterDB
import com.politecnico.beforeafter.navigation.AppScreens
import com.politecnico.beforeafter.ui.theme.DaydreamFont
import com.politecnico.beforeafter.viewmodel.BeforeAfterSettingsViewModel
import com.politecnico.beforeafter.viewmodel.BeforeAfterSettingsViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BeforeAfterSettings(navController: NavController) {
    val context = LocalContext.current
    val beforeAfterSettingsViewModel : BeforeAfterSettingsViewModel = viewModel( // Global ViewModel that survives configuration changes
        factory = BeforeAfterSettingsViewModelFactory(
            BeforeAfterDB.getDatabase(context).settingsDao(),
            BeforeAfterDB.getDatabase(context).limitedAppsDao()
        )
    )
    val beforeAfterSettingsUiState by beforeAfterSettingsViewModel.publicState.collectAsState()


    // Insert default settings and get limited apps
    LaunchedEffect(Unit) {
        // Start BackgroundService (if it's not running) and load screen settings
        beforeAfterSettingsViewModel.startBackgroundService(context)
        beforeAfterSettingsViewModel.insertDefaultSettings()
        beforeAfterSettingsViewModel.getLimitedApps()
    }

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ){
        val valuesBefore = remember { (0..60).map { it.toString() } }
        val valuesBeforePickerState = rememberPickerState(
            initialNumberOfOptions = 60
        )
        val valuesAfter = remember { (0..120).map { it.toString() } }
        val valuesAfterPickerState = rememberPickerState(
            initialNumberOfOptions = 120
        )

        // BEFORE
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "BEFORE",
                fontSize = 40.sp,
                modifier = Modifier.padding(top = 35.dp),
                fontFamily = DaydreamFont,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .border(
                        2.dp,
                        Color.Black,
                        CircleShape
                    )
            ) {
                Picker(
                    state = valuesBeforePickerState,
                    items = valuesBefore,
                    visibleItemsCount = 3,
                    textModifier = Modifier.padding(8.dp),
                    textStyle = TextStyle(fontSize = 32.sp),
                    dividerColor = Color.Black,
                    startIndex = beforeAfterSettingsUiState.beforeSeconds
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = "(seconds to warn the user BEFORE using app)",
                color = Color.Red,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // AFTER
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "AFTER",
                fontSize = 40.sp,
                modifier = Modifier.padding(top = 35.dp),
                fontFamily = DaydreamFont,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .border(
                        2.dp,
                        Color.Black,
                        CircleShape
                    )
            ) {
                Picker(
                    state = valuesAfterPickerState,
                    items = valuesAfter,
                    visibleItemsCount = 3,
                    textModifier = Modifier.padding(8.dp),
                    textStyle = TextStyle(fontSize = 32.sp),
                    dividerColor = Color.Black,
                    startIndex = beforeAfterSettingsUiState.afterMinutes
                )
            }
            Spacer(Modifier.height(10.dp))
            Text(
                text = "(minutes to block app AFTER exceeding set time)",
                color = Color.Red,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // APPS
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .border(
                        2.dp,
                        Color.Black,
                        CircleShape
                    )
                    .background(Color.Black)
                    .clickable { navController.navigate(AppScreens.AppSelector.route) }
            ) {
                LazyRow (
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    if(beforeAfterSettingsUiState.limitedAppsList.isEmpty()){
                        item {
                            Text(text = "Choose limited apps",
                                fontFamily = DaydreamFont,
                                fontSize = 12.sp,
                                color = Color.White,
                                modifier = Modifier.padding(
                                    start = 10.dp,
                                    end = 10.dp,
                                    top = 20.dp,
                                    bottom = 20.dp
                                )
                            )
                        }
                    } else {
                        items(beforeAfterSettingsUiState.limitedAppsList) { app ->
                            val icon = remember {
                                context.packageManager.getApplicationIcon(app.packageName)
                            }
                                Image(
                                    painter = rememberDrawablePainter(drawable = icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(70.dp).padding(
                                        start = 5.dp,
                                        end = 5.dp,
                                        top = 10.dp,
                                        bottom = 10.dp
                                    )
                                )
                        }
                    }
                }
            }
        }
    }
}