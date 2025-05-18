package com.example.apppracticasjc.Data.RoomDB

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

// This is where the queries are defined

@Dao
interface LimitedAppsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(app: LimitedAppsEntity)

    @Delete
    suspend fun delete(app: LimitedAppsEntity)

    @Query("SELECT * from limitedapps")
    fun getLimitedApps(): LimitedAppsEntity
}