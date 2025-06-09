package com.politecnico.beforeafter.viewmodel

import androidx.lifecycle.ViewModel
import com.example.apppracticasjc.Data.RoomDB.LimitedAppEntity
import com.example.apppracticasjc.Data.RoomDB.LimitedAppsDao

class AppSelectorViewModel(
    private val limitedAppsDao: LimitedAppsDao
) : ViewModel() {

    /**
     * Get limited apps from the DB
     */
    suspend fun getLimitedApps() : List<LimitedAppEntity> {
        return limitedAppsDao.getLimitedApps()
    }

    /**
     * Save limited apps in the DB
     */
    suspend fun saveApps(appsToSave: List<LimitedAppEntity>) {
        // Delete all apps in DB
        limitedAppsDao.deleteAll()

        // For each app to save...
        for (app in appsToSave){
            limitedAppsDao.insert(app) // Insert app
        }
    }
}