package com.example.apppracticasjc.Data.RoomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// This is where the Room database is defined and obtained

@Database(entities = [SettingsEntity::class, LimitedAppsEntity::class], version = 1, exportSchema = false)
abstract class BeforeAfterDB : RoomDatabase() {
    // Database imports daos to call queries
    abstract fun settingsDao(): SettingsDao
    abstract fun limitedAppsDao(): LimitedAppsDao

    // Allow access to the class methods (in the companion object brackets) withour creating an object
    // For example (BaseDatos.function())
    companion object {
        @Volatile
        private var InstanciaBD: BeforeAfterDB? = null

        fun getDatabase(context: Context): BeforeAfterDB {
            // If the BD instance isn't null, it's returned
            return InstanciaBD ?: synchronized(this) { // If it's null, a new instance is created
                Room.databaseBuilder(context, BeforeAfterDB::class.java, "BeforeAfterDB") // Name can be different than class name
                    .fallbackToDestructiveMigration(false)
                    .build() // Create database instance
                    .also { InstanciaBD = it }
            }
        }
    }
}