package com.luan.hsworms.hydroid

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luan.hsworms.hydroid.Backend.Database.AppRepository
import com.luan.hsworms.hydroid.Backend.Database.History
import com.luan.hsworms.hydroid.Backend.Database.WaterRequirement
import kotlinx.coroutines.launch

class AddWaterViewModel(application: Application):AndroidViewModel(application) {

    ////////////////////////////////////////////////////////////////////////////////////
    //Repository
    private val repository = AppRepository(application)
    ////////////////////////////////////////////////////////////////////////////////////


    var consum = MutableLiveData<Int>()

    init {
        consum.value = 0
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //Methods to interact with the repository:

    fun update(drunk:Int){
        viewModelScope.launch{
            //TODO:Implementieren update the Value in Database
        }
    }
}