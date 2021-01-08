package com.luan.hsworms.hydroid

import android.app.Application
import android.content.SharedPreferences
import android.icu.util.Calendar
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.luan.hsworms.hydroid.Backend.Database.AppRepository
import com.luan.hsworms.hydroid.Backend.Database.History
import com.luan.hsworms.hydroid.Backend.Database.HistoryDao
import com.luan.hsworms.hydroid.Backend.Database.WaterRequirementDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

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

    //Clear Data for testing
    fun clearFile(){
        val editor = ourUserData?.edit()
        editor?.clear()
        editor?.apply()
    }


    fun updateDataByStartActivity(weightIn: Long, genderIn: Boolean)
    {
        //Save the current date in text format
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
        val dateNow = Date()
        val dateAsString:String = simpleDateFormat.format(dateNow)// val test:String = "01.01.2000"

        viewModelScope.launch {
            //Search the database for water requirements by weight and gender.
            // In the absence of data, assigning a value of 2500
            val waterRequirement: Int? = getWaterRequirementByWeightAndGender(weightIn, genderIn)
            if (waterRequirement != null) {
                dailyLiquidRequirement.value = waterRequirement
                println("TEST ${dailyLiquidRequirement.value}")//TODO: comment out the string
            } else {
                dailyLiquidRequirement.value = 2500
            }

            //Local variables for code readability
            val requirement = dailyLiquidRequirement.value!!
            var drunk = currentlyDrunkLiquid.value!!
            var history = getHistoryByDate(dateAsString)

            //If there is no record in the database for the current day, then it is created
            if(history == null)
            {
                //A new day has begun, the value of the drink is set to zero
                currentlyDrunkLiquid.value = 0
                drunk = 0

                insert(dateAsString, drunk, requirement,
                    drunk*100/requirement, weightOfUser.value!!)
                // println("no ${waterRequirement.toString()} ${weightIn}  ${genderIn}")

            }else{
                println("yes")
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //Methods to interact with the repository:

    fun update(drunk:Int){
        viewModelScope.launch{
            //TODO:Implement update the Value in Database
        }
    }

    fun insert(date:String, drunk: Int, requirements:Int, fulfillment:Int, weight: Int){
        viewModelScope.launch{
            val history = History(0L, date, drunk, requirements, fulfillment, weight)
            repository.insertInHistory(history)
        }
    }

    suspend fun getWaterRequirementByWeightAndGender(weightIn: Long, genderIn: Boolean):Int?{
        var waterRequirement:Int? = null
        withContext(Dispatchers.IO){
            waterRequirement = repository.getWaterRequirementByWeightAndGender(weightIn, genderIn)
        }
        return waterRequirement
    }

    suspend fun getHistoryByDate(dateIn: String):History?{
        var history:History? = null
        withContext(Dispatchers.IO){
            history = repository.getHistoryByDate(dateIn)
        }
        return history
    }


    //TODO: Add function to clear all user data in internal storage
    //TODO: Add the function of zeroing the value of the drunk liquid
}