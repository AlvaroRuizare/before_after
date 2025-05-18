package com.politecnico.beforeafter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.apppracticasjc.Data.RoomDB.LimitedAppsDao
import com.example.apppracticasjc.Data.RoomDB.SettingsDao

class BeforeAfterSettingsViewModelFactory(
    private val settingsDao: SettingsDao,
    private val limitedAppsDao: LimitedAppsDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BeforeAfterSettingsViewModel::class.java)) {
            return BeforeAfterSettingsViewModel(settingsDao, limitedAppsDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}