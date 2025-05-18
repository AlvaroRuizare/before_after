package com.example.apppracticasjc.Data.RoomDB

import androidx.room.Entity
import androidx.room.PrimaryKey

// Here we define a database table

@Entity(tableName = "limitedApps")
data class LimitedAppsEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,

    val appName : String
)
