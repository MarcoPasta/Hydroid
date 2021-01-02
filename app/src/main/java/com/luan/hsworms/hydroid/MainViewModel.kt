package com.luan.hsworms.hydroid

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luan.hsworms.hydroid.Backend.Database.AppRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    ////////////////////////////////////////////////////////////////////////////////////
    //Repository
    private val repository = AppRepository(application)
    ////////////////////////////////////////////////////////////////////////////////////

    //TODO: Encapsulate the Live Data
    var userGenderIsFemale = MutableLiveData<Boolean>()
    var weightOfUser = MutableLiveData<Int>()
    var dailyLiquidRequirement = MutableLiveData<Int>()
    var currentlyDrunkLiquid = MutableLiveData<Int>()

    var ourUserData: SharedPreferences? = null//To save user data in internal storage


    init {
        userGenderIsFemale.value = true
        weightOfUser.value = 0
        dailyLiquidRequirement.value = 0
        currentlyDrunkLiquid.value = 0
    }


    //Filling viewModel variables with values from internal storage
    fun populateViewModel(){

        userGenderIsFemale.value = ourUserData?.getBoolean(R.string.saved_gender_of_user.toString(), true)
        weightOfUser.value = ourUserData?.getInt(R.string.saved_weight_of_user.toString(), 0)
        dailyLiquidRequirement.value = ourUserData?.getInt(R.string.saved_liquid_requirements_of_user.toString(), 0)
        currentlyDrunkLiquid.value = ourUserData?.getInt(R.string.saved_drunk_liquid_of_user.toString(), 0)
    }

    //Here we change the data in the storage, in case of changing the actual data
    fun saveData(newGender: Boolean, newWeight: Int, newLiquidRequirements: Int, newDrunkLiquid: Int){
        val editor = ourUserData?.edit()

        editor?.putBoolean(R.string.saved_gender_of_user.toString().toString(), newGender)
        editor?.putInt(R.string.saved_weight_of_user.toString(), newWeight)
        editor?.putInt((R.string.saved_liquid_requirements_of_user).toString(), newLiquidRequirements)
        editor?.putInt((R.string.saved_drunk_liquid_of_user).toString(), newDrunkLiquid)

        editor?.apply()
    }

    fun clearFile(){
        val editor = ourUserData?.edit()
        editor?.clear()
        editor?.apply()

    }

    ////////////////////////////////////////////////////////////////////////////////////
    //Methods to interact with the repository:

    fun update(drunk:Int){
        viewModelScope.launch{
            //TODO:Implementieren update the Value in Database
        }
    }


    //TODO: Add function to clear all user data in internal storage
    //TODO: Add the function of zeroing the value of the drunk liquid
}