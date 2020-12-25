package com.luan.hsworms.hydroid

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.luan.hsworms.hydroid.Backend.Database.AppRepository
import com.luan.hsworms.hydroid.Backend.Database.History
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class HistoryViewModel(application: Application): AndroidViewModel(application) {
    ////////////////////////////////////////////////////////////////////////////////////
    //Repository
    private val repository = AppRepository(application)
    private val historyLiveData = repository.getHistoryLiveData()
    ////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////
    //Methods to interact with the repository:
    fun insert(date: String, drunk:Int, requirement:Int, weight: Int){
        viewModelScope.launch{
            val history = History(0L, Date().toStringFormat(), drunk, requirement, (drunk/requirement)*100, weight)
            repository.insertInHistory(history)
        }
    }

    fun update(history: History){
        viewModelScope.launch {
            repository.updateHistory(history)
        }
    }

    fun delete(history: History){
        viewModelScope.launch {
            repository.deleteHistory(history)
        }
    }

    fun getHistoryByDate(date:Date): History?{
        var history:History? = null
        viewModelScope.launch {
            history = repository.getHistoryByDate(date.toStringFormat())
        }
        return history
    }

    fun getHistoryAll():List<History>?{
        var history:List<History>? = null
        viewModelScope.launch {
            history = repository.getHistoryAll()
        }
        return history
    }
    ////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////
    //Get-Method for LiveData
    fun getHistoryLiveData(): LiveData<List<History>> = historyLiveData
    ////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////
    //Utils:
    private fun Date.toStringFormat(pattern: String = "dd.MM.yyyy"): String{
        return SimpleDateFormat(pattern).format(this)
    }
    ////////////////////////////////////////////////////////////////////////////////////
}