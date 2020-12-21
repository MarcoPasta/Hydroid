package com.luan.hsworms.hydroid.Backend

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserData::class], version = 1)
abstract class HydroitDatabase: RoomDatabase() {
    abstract fun hydroitDao(): HydroitDao
}

