package com.example.apppracticasjc.Data.RoomDB

import androidx.room.Entity
import androidx.room.PrimaryKey

// Here we define a database table

@Entity(tableName = "limitedApps")
data class LimitedAppEntity(
    @PrimaryKey val packageName: String,
    val appName : String,
    val limited : Boolean,
    val blocked : Boolean
)
