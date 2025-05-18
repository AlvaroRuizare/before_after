package com.politecnico.beforeafter.viewmodel

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import com.example.apppracticasjc.Data.RoomDB.LimitedAppsDao
import com.example.apppracticasjc.Data.RoomDB.SettingsDao
import com.example.apppracticasjc.Data.RoomDB.SettingsEntity
import com.politecnico.beforeafter.data.model.BeforeAfterSettingsUiState
import com.politecnico.beforeafter.services.BackgroundService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class BeforeAfterSettingsViewModel(
    private val settingsDao: SettingsDao,
    private val limitedAppsDao: LimitedAppsDao
) : ViewModel() {
    private val _estadoPrivado = MutableStateFlow(BeforeAfterSettingsUiState())
    val estadoPublico: StateFlow<BeforeAfterSettingsUiState> = _estadoPrivado.asStateFlow()

    /**
     * When starting viewmodel...
     */
    init {

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun startBackgroundService(context : Context){
        val intent = Intent(context, BackgroundService::class.java)
        context.stopService(intent)
        context.startForegroundService(intent)
    }

    suspend fun insertDefaultSettings() {
        settingsDao.insert(
            SettingsEntity(
                0,
                10,
                60
            )
        )
    }
}