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

    //Repository
    private val repository = AppRepository(application)

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


    //Updating data at application start and adding a record about the current day to History, if it has not been created yet
    fun updateDataByStartActivity(weightIn: Long, genderIn: Boolean)
    {
        //Search the database for water requirements by weight and gender. Returns 2500 if didn't find.
        waterRequirementsUpdate(weightIn, genderIn)

        //If there is no record in the database for the current day, then it is created
        addEntityInHistory()
    }


    //Search the database for water requirements by weight and gender. Returns 2500 if didn't find.
    private fun waterRequirementsUpdate(weightIn: Long, genderIn: Boolean){
        viewModelScope.launch {
            val waterRequirement: Int? = getWaterRequirementByWeightAndGender(weightIn, genderIn)
            if (waterRequirement != null)
            {
                dailyLiquidRequirement.value = waterRequirement
            } else
            {
                dailyLiquidRequirement.value = 2500
            }
        }
    }


    //If there is no record in the database for the current day, then it is created
    private  fun addEntityInHistory()
    {
        viewModelScope.launch {
            val requirement = dailyLiquidRequirement.value!!
            val drunk = 0
            val history = getHistoryByDate(currentDate())

            if(history == null)
            {
                currentlyDrunkLiquid.value = drunk //A new day has begun, the value of the drink is set to zero
                insert(currentDate(), drunk, requirement,drunk*100/requirement, weightOfUser.value!!)
            }
        }
    }

    //Adding the water you drink to the water already drunk today
    fun addDrunkWater(waterIn:Int)
    {
        println("TEST3 ${waterIn}")
        currentlyDrunkLiquid.value = currentlyDrunkLiquid.value?.plus(waterIn)
        println("TEST4 ${currentlyDrunkLiquid.value}")
            update()
            saveData(userGenderIsFemale.value!!, weightOfUser.value!!,
                dailyLiquidRequirement.value!!, currentlyDrunkLiquid.value!!)
    }

    //Return the current date in text format
    private fun currentDate():String{
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
        val dateNow = Date()
        return simpleDateFormat.format(dateNow)
    }


    ////////////////////////////////////////////////////////////////////////////////////
    //Methods to interact with the repository:

    fun update(){
        viewModelScope.launch{
            val history:History? = getHistoryByDate(currentDate())
            history?.drunk = currentlyDrunkLiquid.value!!
            //history?.drunk = 0
            history?.fulfillment = currentlyDrunkLiquid.value!! * 100 / dailyLiquidRequirement.value!!
            repository.updateHistory(history!!)
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
}