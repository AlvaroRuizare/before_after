package com.politecnico.beforeafter.viewmodel

import androidx.lifecycle.ViewModel
import com.example.apppracticasjc.Data.RoomDB.LimitedAppEntity
import com.example.apppracticasjc.Data.RoomDB.LimitedAppsDao

class AppSelectorViewModel(
    private val limitedAppsDao: LimitedAppsDao
) : ViewModel() {

    suspend fun getLimitedApps() : List<LimitedAppEntity> {
        return limitedAppsDao.getLimitedApps()
    }

    suspend fun saveApps(appsToSave: List<LimitedAppEntity>) {
        limitedAppsDao.deleteAll()

        for (app in appsToSave){
            limitedAppsDao.insert(app)
        }
    }
}