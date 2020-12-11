package com.luan.hsworms.hydroid

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.currentCoroutineContext

class MainViewModel : ViewModel() {
    //TODO: Encapsulate the Live Data
    var ageOfUser = MutableLiveData<Int>()
    var weightOfUser = MutableLiveData<Int>()
    var dailyLiquidRequirement = MutableLiveData<Int>()
    var currentlyDrunkLiquid = MutableLiveData<Int>()
    var ourUserData: SharedPreferences? = null//To save user data in internal storage


    init {
        ageOfUser.value = 0
        weightOfUser.value = 0
        dailyLiquidRequirement.value = 0
        currentlyDrunkLiquid.value = 0
    }


    //Here we change the data in the storage, in case of changing the actual data
    fun saveData(age_in: Int, weight_in: Int, liquidRequirments_in: Int, DrukLiquid_in: Int){
        val editor = ourUserData?.edit()


    }
}