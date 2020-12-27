package com.luan.hsworms.hydroid.Backend.Database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WaterRequirement::class, History::class], version = 1, exportSchema = false)
abstract class HydroidDatabase : RoomDatabase() {
    abstract val waterRequirementDao: WaterRequirementDao
    abstract val historyDao: HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: HydroidDatabase? = null

        fun createInstance(application: Application): HydroidDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        application.applicationContext,
                        HydroidDatabase::class.java,
                        "hydroid.db"
                    )
                        .createFromAsset("database/hydroid.db")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}