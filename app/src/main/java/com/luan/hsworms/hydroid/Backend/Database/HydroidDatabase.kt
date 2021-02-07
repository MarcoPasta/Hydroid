package com.luan.hsworms.hydroid.Backend.Database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * A room database class that includes two tables: History, WaterRequirement
 *
 * @author Andrej Alpatov
 */
@Database(entities = [WaterRequirement::class, History::class], version = 1, exportSchema = false)
abstract class HydroidDatabase : RoomDatabase() {
    abstract val waterRequirementDao: WaterRequirementDao
    abstract val historyDao: HistoryDao

    /**
     * Creating a companion object through which we can efficiently work with the database
     */
    companion object {
        /**
         * Database Hydroid Instance with @Volatile , which means that operations on it will be
         * performed not in the cache but in the main memory. which will allow several threads to work with it in parallel
         */
        @Volatile
        private var INSTANCE: HydroidDatabase? = null

        /**
         * Method for creating a HydroidDatabase instance
         *
         * @return HydroidDatabase instance
         */
        fun createInstance(application: Application): HydroidDatabase {
            /**
             *  synchronized allows multiple threads to create instances in parallel and each will do it alone.
             */
            synchronized(this) {
                /**
                 * Local Database Hydroid Instance
                 */
                var instance = INSTANCE
                /**
                 * If the instance is not created, it is created
                 */
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        application.applicationContext,
                        HydroidDatabase::class.java,
                        "hydroidDB"
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