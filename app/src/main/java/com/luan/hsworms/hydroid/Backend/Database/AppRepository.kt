package com.luan.hsworms.hydroid.Backend.Database

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository (application: Application) {

    private val waterRequirementDao: WaterRequirementDao
    private val historyDao: HistoryDao

    //Now the data is loaded from the local database, later it is possible from the remote server
    init {
        val dbWaterRequirement = HydroidDatabase.createInstance(application)
        waterRequirementDao = dbWaterRequirement.waterRequirementDao
        historyDao = dbWaterRequirement.historyDao
    }

    //implement al methods for History and WaterRequirements
    suspend fun insertInRequirements(waterRequirement: WaterRequirement){
        withContext(Dispatchers.IO){
            waterRequirementDao.insert(waterRequirement)
        }
    }

    suspend fun insertInHistory(history: History){
        withContext(Dispatchers.IO){
            historyDao.insert(history)
        }
    }

    suspend fun updateRequirements(waterRequirement: WaterRequirement){
        withContext(Dispatchers.IO){
            waterRequirementDao.update(waterRequirement)
        }
    }

    suspend fun updateHistory(history: History){
        withContext(Dispatchers.IO){
            historyDao.update(history)
        }
    }

    suspend fun deleteRequirements(waterRequirement: WaterRequirement){
        withContext(Dispatchers.IO){
            waterRequirementDao.delete(waterRequirement)
        }
    }

    suspend fun deleteHistory(history: History){
        withContext(Dispatchers.IO){
            historyDao.delete(history)
        }
    }

    suspend fun getWaterRequirementByWeightAndGender(weightIn: Long, genderIn: Int):Int?{
        var waterRequirement:Int? = null
        withContext(Dispatchers.IO){
            waterRequirement = waterRequirementDao.getWaterRequirementByWeightAndGender(weightIn, genderIn)
        }
        return waterRequirement
    }

    suspend fun getHistoryByDate(dateIn: String):History?{
        var history:History? = null
        withContext(Dispatchers.IO){
            history = historyDao.getHistoryByDate(dateIn)
        }
        return history
    }

    suspend fun getWaterRequirements():List<WaterRequirement>?{
        var waterRequirements:List<WaterRequirement>? = null
        withContext(Dispatchers.IO){
            waterRequirements = waterRequirementDao.getWaterRequirements()
        }
        return waterRequirements
    }

    suspend fun getHistoryAll():List<History>?{
        var history:List<History>? = null
        withContext(Dispatchers.IO){
            history = historyDao.getHistoryAll()
        }
        return history
    }

    fun getWaterRequirementLiveData(): LiveData<List<WaterRequirement>> = waterRequirementDao.getLiveData()

    fun getHistoryLiveData (): LiveData<List<History>> = historyDao.getLiveData()
}