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

    // When starting viewmodel...
    init {
        loadSettings()
        getLimitedApps()
    }


    /**
     * Start the service that detects when limited apps are opened
     */
    @RequiresApi(Build.VERSION_CODES.O)
    fun startBackgroundService(context : Context){
        viewModelScope.launch(Dispatchers.IO) {
            val intent = Intent(context, BackgroundService::class.java)
            if (!BackgroundService.isRunning){ // If it's running, don't start it again
                context.startForegroundService(intent)
            }
        }
    }


    /**
     * Loads database settings or, if the app is opened for the first time, default settings
     */
    fun loadSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            val savedSettings = settingsDao.getSettings() // Attempt to get settings from DB

            // If there are saved settings (it's not the first time opening)...
            if (savedSettings != null) {
                _privateState.update { currentState -> // Load settings on UiState
                    currentState.copy(
                        beforeSeconds = savedSettings.beforeSeconds,
                        afterMinutes = savedSettings.afterMinutes
                    )
                }
            } else { // If there are no saved settings (it's the first time)...
                settingsDao.insert(SettingsEntity( // Insert defaults
                    0,
                    _privateState.value.beforeSeconds,
                    _privateState.value.afterMinutes)
                )
            }

            // 'isLoading' makes the rest of the app wait for the database to retrieve the real data
            _privateState.update { currentState ->
                currentState.copy(
                    isLoading = false
                )
            }
        }
    }


    /**
     * Get the limited apps list from DB and store it in state
     */
    fun getLimitedApps() {
        viewModelScope.launch(Dispatchers.IO) {
            val limitedAppsList = limitedAppsDao.getLimitedApps()

            _privateState.update { currentState ->
                currentState.copy(
                    limitedAppsList = limitedAppsList
                )
            }
        }
    }


    /**
     * Update beforeSeconds state
     */
    fun updateBeforeState(beforeSeconds: Int) {
        _privateState.update { currentState ->
            currentState.copy(
                beforeSeconds = beforeSeconds
            )
        }
    }


    /**
     * Update afterMinutes state
     */
    fun updateAfterState(afterMinutes: Int) {
        _privateState.update { currentState ->
            currentState.copy(
                afterMinutes = afterMinutes
            )
        }
    }


    /**
     * Update DB settings
     */
    fun updateDBSettings(beforeSeconds: Int, afterMinutes: Int) {
        if (!_privateState.value.isLoading){ // When the data has stopped loading...
            viewModelScope.launch {
                val settingsToSave = SettingsEntity(0, beforeSeconds, afterMinutes)
                settingsDao.insert(settingsToSave) // Save settings
            }
        }
    }
}
