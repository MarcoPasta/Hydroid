package com.luan.hsworms.hydroid.Backend.Database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [History::class], version = 1, exportSchema = false)
abstract class HistoryDatabase:RoomDatabase() {
    abstract val historyDao:HistoryDao

    companion object{
        @Volatile
        private var INSTANCE:HistoryDatabase? = null

        fun createInstance(application: Application):HistoryDatabase{
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        application.applicationContext,
                        HistoryDatabase::class.java,
                        "history_database"
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