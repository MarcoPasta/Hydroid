package com.luan.hsworms.hydroid.backend.database

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Through this class, the rest of the classes will be able to access data from the room database.
 *
 */
class AppRepository (application: Application) {

    private val waterRequirementDao: WaterRequirementDao
    private val historyDao: HistoryDao

    /**
     * Now the data is loaded from the local database, later it is possible from the remote server
     */

    init {
        val dbWaterRequirement = HydroidDatabase.createInstance(application)
        waterRequirementDao = dbWaterRequirement.waterRequirementDao
        historyDao = dbWaterRequirement.historyDao
    }

    //implement al methods for History and WaterRequirements

    /**
     * Adding an entry to a DB table waterRequirement
     *
     * @param waterRequirement An entry in a DB table WaterRequirement to be added
     */
    suspend fun insertInRequirements(waterRequirement: WaterRequirement){
        withContext(Dispatchers.IO){
            waterRequirementDao.insert(waterRequirement)
        }
    }

    /**
     * Adding an entry to a DB table History
     *
     * @param history An entry in a DB table History to be added
     */
    suspend fun insertInHistory(history: History){
        withContext(Dispatchers.IO){
            historyDao.insert(history)
        }
    }

    /**
     * Update WaterRequirement table entry
     *
     * @param waterRequirement An entry in a DB table WaterRequirement to be changed
     */
    suspend fun updateRequirements(waterRequirement: WaterRequirement){
        withContext(Dispatchers.IO){
            waterRequirementDao.update(waterRequirement)
        }
    }

    /**
     * Update History table entry
     *
     * @param history An entry in a DB table History to be changed
     */
    suspend fun updateHistory(history: History){
        withContext(Dispatchers.IO){
            historyDao.update(history)
        }
    }

    /**
     * Deleting a record in a  WaterRequirement table
     *
     * @param waterRequirement An entry in a DB table WaterRequirement to be deleted
     */
    suspend fun deleteRequirements(waterRequirement: WaterRequirement){
        withContext(Dispatchers.IO){
            waterRequirementDao.delete(waterRequirement)
        }
    }

    /**
     * Deleting a record in a History table
     *
     * @param history An entry in a DB table History to be deleted
     */
    suspend fun deleteHistory(history: History){
        withContext(Dispatchers.IO){
            historyDao.delete(history)
        }
    }
    /**
     * Searches the table WaterRequirement for records matching the criteria for gender and weight.
     *
     * @param weightIn  Search criteria by weight (type Long)
     * @param genderIn  Search criteria by gender (type Int)
     *
     * @return          Water requirement matching the search criteria (type Int?)
     */
    suspend fun getWaterRequirementByWeightAndGender(weightIn: Long, genderIn: Int):Int?{
        var waterRequirement: Int?
        withContext(Dispatchers.IO){
            waterRequirement = waterRequirementDao.getWaterRequirementByWeightAndGender(weightIn, genderIn)
        }
        return waterRequirement
    }

    /**
     * Searches the table History for records matching the criteria for date
     *
     * @param dateIn    Search criteria by date (type String)
     *
     * @return          An entry in a DB table History matching the search criteria
     */
    suspend fun getHistoryByDate(dateIn: String):History?{
        var history: History?
        withContext(Dispatchers.IO){
            history = historyDao.getHistoryByDate(dateIn)
        }
        return history
    }

    /**
     * Retrieving all records from a table WaterRequirement
     *
     * @return All records from the table WaterRequirement (type List<WaterRequirement>?)
     */
    suspend fun getWaterRequirements():List<WaterRequirement>?{
        var waterRequirements: List<WaterRequirement>?
        withContext(Dispatchers.IO){
            waterRequirements = waterRequirementDao.getWaterRequirements()
        }
        return waterRequirements
    }

    /**
     * Retrieving all records from a table History
     *
     * @return All records from the table History (type List<History>?)
     */
    suspend fun getHistoryAll():List<History>?{
        var history: List<History>?
        withContext(Dispatchers.IO){
            history = historyDao.getHistoryAll()
        }
        return history
    }

    /**
     * Informs the observer about the WaterRequirement table change
     */
    fun getWaterRequirementLiveData(): LiveData<List<WaterRequirement>> = waterRequirementDao.getLiveData()

    /**
     * Informs the observer about the History table change
     */
    fun getHistoryLiveData (): LiveData<List<History>> = historyDao.getLiveData()
}