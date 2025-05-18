package com.example.apppracticasjc.Data.RoomDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

// This is where the queries are defined

@Dao
interface SettingsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(setting: SettingsEntity)

    @Update
    suspend fun update(setting: SettingsEntity)

    @Query("SELECT * from settings")
    fun getSettings(): SettingsEntity
}