package com.luan.hsworms.hydroid.Backend.Database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WaterRequirement::class], version = 1, exportSchema = false)
abstract class WaterRequirementDatabase : RoomDatabase() {
    abstract val waterRequirementDao: WaterRequirementDao

    companion object {
        @Volatile
        private var INSTANCE: WaterRequirementDatabase? = null

        fun createInstance(application: Application): WaterRequirementDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        application.applicationContext,
                        WaterRequirementDatabase::class.java,
                        "water_requirement_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}