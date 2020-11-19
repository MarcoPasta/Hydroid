package com.luan.hsworms.hydroit.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.luan.hsworms.hydroit.UserData

@Database(entities = [UserData::class], version = 1)
abstract class HydroitDatabase: RoomDatabase() {
    abstract fun hydroitDao(): HydroitDao
}

