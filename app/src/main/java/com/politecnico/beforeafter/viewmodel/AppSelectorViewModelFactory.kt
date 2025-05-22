package com.politecnico.beforeafter.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.apppracticasjc.Data.RoomDB.LimitedAppsDao
import com.example.apppracticasjc.Data.RoomDB.SettingsDao

class AppSelectorViewModelFactory(
    private val limitedAppsDao: LimitedAppsDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppSelectorViewModel::class.java)) {
            return AppSelectorViewModel(limitedAppsDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}