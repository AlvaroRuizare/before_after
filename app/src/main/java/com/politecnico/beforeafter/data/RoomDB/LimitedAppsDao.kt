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

    @Query("SELECT packageName from limitedapps")
    suspend fun getLimitedPackageNames(): List<String>

    @Query("DELETE from limitedApps")
    suspend fun deleteAll()

    @Query("SELECT limited from limitedapps where packageName = :packageName")
    suspend fun getLimited(packageName: String) : Boolean

    @Query("UPDATE limitedApps set limited = :isLimited where packageName = :packageName")
    suspend fun updateLimited(packageName: String, isLimited: Boolean)

    @Query("SELECT blocked from limitedapps where packageName = :packageName")
    suspend fun getBlocked(packageName: String) : Boolean

    @Query("UPDATE limitedApps set blocked = :isBlocked where packageName = :packageName")
    suspend fun updateBlocked(packageName: String, isBlocked: Boolean)
}