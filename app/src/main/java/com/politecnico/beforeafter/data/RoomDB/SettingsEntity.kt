package com.example.apppracticasjc.Data.RoomDB

import androidx.room.Entity
import androidx.room.PrimaryKey

// Here we define a database table

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,

    val beforeSeconds : Int,

    val afterMinutes : Int
)
