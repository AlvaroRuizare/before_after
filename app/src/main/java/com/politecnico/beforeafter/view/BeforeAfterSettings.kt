package com.politecnico.beforeafter.view

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apppracticasjc.Data.RoomDB.BeforeAfterDB
import com.politecnico.beforeafter.viewmodel.BeforeAfterSettingsViewModel
import com.politecnico.beforeafter.viewmodel.BeforeAfterSettingsViewModelFactory

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BeforeAfterSettings() {
    val context = LocalContext.current
    val beforeAfterSettingsViewModel : BeforeAfterSettingsViewModel = viewModel( // ViewModel global que sobrevive a cambios de configuracion
        factory = BeforeAfterSettingsViewModelFactory(
            BeforeAfterDB.getDatabase(context).settingsDao(),
            BeforeAfterDB.getDatabase(context).limitedAppsDao()
        )
    )
    val beforeAfterSettingsUiState by beforeAfterSettingsViewModel.estadoPublico.collectAsState()

    // Start BackgroundService
    beforeAfterSettingsViewModel.startBackgroundService(context)

    // Insert default settings
    LaunchedEffect(Unit) {
        beforeAfterSettingsViewModel.insertDefaultSettings()
    }

    Text("settings")
}