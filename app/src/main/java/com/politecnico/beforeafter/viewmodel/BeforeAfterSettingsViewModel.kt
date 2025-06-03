package com.politecnico.beforeafter.viewmodel

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apppracticasjc.Data.RoomDB.LimitedAppsDao
import com.example.apppracticasjc.Data.RoomDB.SettingsDao
import com.example.apppracticasjc.Data.RoomDB.SettingsEntity
import com.politecnico.beforeafter.data.model.BeforeAfterSettingsUiState
import com.politecnico.beforeafter.services.BackgroundService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BeforeAfterSettingsViewModel(
    private val settingsDao: SettingsDao,
    private val limitedAppsDao: LimitedAppsDao
) : ViewModel() {
    private val _privateState = MutableStateFlow(BeforeAfterSettingsUiState())
    val publicState: StateFlow<BeforeAfterSettingsUiState> = _privateState.asStateFlow()

    /**
     * When starting viewmodel...
     */
    init {

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun startBackgroundService(context : Context){
        viewModelScope.launch(Dispatchers.IO) {
            val intent = Intent(context, BackgroundService::class.java)
            if (!BackgroundService.isRunning){ // If it's running, don't start it again
                context.startForegroundService(intent)
            }
        }
    }

    fun insertDefaultSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            settingsDao.insert(
                SettingsEntity(
                    0,
                    10,
                    60
                )
            )

            val settings = settingsDao.getSettings()

            // Update UiState beforeSeconds
            _privateState.update { estadoActual ->
                estadoActual.copy(
                    beforeSeconds = settings.beforeSeconds
                )
            }

            // Update UiState afterMinutes
            _privateState.update { estadoActual ->
                estadoActual.copy(
                    afterMinutes = settings.afterMinutes
                )
            }
        }
    }

    fun getLimitedApps() {
        viewModelScope.launch(Dispatchers.IO) {
            val limitedAppsList = limitedAppsDao.getLimitedApps()

            _privateState.update { estadoActual ->
                estadoActual.copy(
                    limitedAppsList = limitedAppsList
                )
            }
        }
    }
}