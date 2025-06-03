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
    suspend fun insert(app: LimitedAppEntity)

    @Delete
    suspend fun delete(app: LimitedAppEntity)

    @Query("SELECT * from limitedapps")
    suspend fun getLimitedApps(): List<LimitedAppEntity>

    @Query("DELETE from limitedApps")
    suspend fun deleteAll()
}