package com.luan.hsworms.hydroid

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.luan.hsworms.hydroid.backend.database.AppRepository
import com.luan.hsworms.hydroid.backend.database.History
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * ViewModel class for HistoryFragment
 *
 * @author Andrej Alpatov
 */
class HistoryViewModel(application: Application): AndroidViewModel(application) {
    ////////////////////////////////////////////////////////////////////////////////////
    //Repository
    private val repository = AppRepository(application)
    private val historyLiveData = repository.getHistoryLiveData()
    ////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////
    //Methods to interact with the repository:
    /**
     * Adding an entry to the History Database table with data for the current day (date, water demand, amount of water drunk, user weight)
     *
     * @param drunk         amount of water drunk (type: Int)
     * @param requirement   water demand (type: Int)
     * @param weight        user weight (type: Int)
     */
    fun insert(drunk: Int, requirement: Int, weight: Int){
        viewModelScope.launch{
            val history = History(0L, Date().toStringFormat(), drunk, requirement, (drunk/requirement)*100, weight)
            repository.insertInHistory(history)
        }
    }

    /**
     * Updating an entry in the History Database table
     *
     * @param history an entry in the History Database table, to be changed
     */
    fun update(history: History){
        viewModelScope.launch {
            repository.updateHistory(history)
        }
    }

    /**
     * Deleting an entry in data base History table
     *
     * @param history an entry in the History Database table, to be deleted
     */
    fun delete(history: History){
        viewModelScope.launch {
            repository.deleteHistory(history)
        }
    }

    /**
     * Retrieving a record from the Database History table for the date passed as a parameter
     * (Will be used for further development of functionality)
     *
     * @param date   The date, the record for which you want to get from the Database History table. (type: Date)
     * @return       The record from the Database History tavble, which you want to get. (type: History?)
     */
    fun getHistoryByDate(date:Date): History?{
        var history:History? = null
        viewModelScope.launch {
            history = repository.getHistoryByDate(date.toStringFormat())
        }
        return history
    }

    /**
     * Retrieving all records from the Database History table
     * (Will be used for further development of functionality)
     *
     * @return List of History table entities (type: List<History>?)
     */
    fun getHistoryAll():List<History>?{
        var history:List<History>? = null
        viewModelScope.launch {
            history = repository.getHistoryAll()
        }
        return history
    }


    /**
     * Get-Method for LiveData. Returns all records from History table when the table changes
     */
    fun getHistoryLiveData(): LiveData<List<History>> = historyLiveData


    ////////////////////////////////////////////////////////////////////////////////////
    //Utils:
    /**
     * Returns a curent date in text format with a pattern: "dd.MM.yyyy"
     */
    private fun Date.toStringFormat(pattern: String = "dd.MM.yyyy"): String{
        return SimpleDateFormat(pattern).format(this)
    }
    ////////////////////////////////////////////////////////////////////////////////////
}