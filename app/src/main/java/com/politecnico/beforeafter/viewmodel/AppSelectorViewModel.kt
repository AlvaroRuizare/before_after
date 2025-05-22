package com.politecnico.beforeafter.viewmodel

import androidx.lifecycle.ViewModel
import com.example.apppracticasjc.Data.RoomDB.LimitedAppEntity
import com.example.apppracticasjc.Data.RoomDB.LimitedAppsDao

class AppSelectorViewModel(
    private val limitedAppsDao: LimitedAppsDao
) : ViewModel() {

    suspend fun guardarApps(appsToSave: List<LimitedAppEntity>) {
        for (app in appsToSave){
            limitedAppsDao.insert(app)
        }
    }
}