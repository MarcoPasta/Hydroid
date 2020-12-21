package com.luan.hsworms.hydroid.Backend.Database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.luan.hsworms.hydroid.Backend.HydroitDatabase

private const val DATABASE_NAME = "hydroit-database" //File name for Database

class HydroidRepository private constructor(context: Context) {

    private val database : HydroitDatabase = Room.databaseBuilder(
        context.applicationContext,//stay longer in memory then other activity classes
        HydroitDatabase::class.java,//Database class to be created with Room
        DATABASE_NAME
    ).build()

    private val hydroitDao = database.hydroitDao()//DAO-Object reference

    fun getUserName(): LiveData<String> = hydroitDao.getUserName()

    companion object{
        private  var INSTANCE: HydroidRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null){
                INSTANCE = HydroidRepository(context)
            }
        }

        fun get(): HydroidRepository {
            return  INSTANCE ?:
            throw IllegalStateException("HydroidRepository must be initialized")
        }
    }
}