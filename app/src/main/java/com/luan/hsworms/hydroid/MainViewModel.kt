package com.luan.hsworms.hydroid

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.luan.hsworms.hydroid.Backend.Database.AppRepository
import com.luan.hsworms.hydroid.Backend.Database.History
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(application: Application): AndroidViewModel(application) {

    //Repository
    private val repository = AppRepository(application)

    //TODO: Encapsulate the Live Data
    var userGenderIsFemale = MutableLiveData<Int>()
    var weightOfUser = MutableLiveData<Int>()
    var dailyLiquidRequirement = MutableLiveData<Int>()
    var currentlyDrunkLiquid = MutableLiveData<Int>()
    var isFirstStart: Int? = 1


    var firstStart: SharedPreferences? = null       //Use for first start
    var ourUserData: SharedPreferences? = null      //To save user data in internal storage


    init {
        userGenderIsFemale.value = 1
        weightOfUser.value = 0
        dailyLiquidRequirement.value = 1
        currentlyDrunkLiquid.value = 0
    }

    //Methods for first Start and clear SharedPreferences file
    fun populateFirstStart(){
        isFirstStart = firstStart?.getInt(R.string.isFirstStart.toString(), 1)
    }
    fun saveFirstStart(firstStart_in: Int){
        val editor = firstStart?.edit()
        editor?.putInt(R.string.isFirstStart.toString(), firstStart_in)
        editor?.apply()
    }


    //Filling viewModel variables with values from internal storage
    fun populateViewModel(){

        userGenderIsFemale.value = ourUserData?.getInt(R.string.saved_gender_of_user.toString(), 1)
        weightOfUser.value = ourUserData?.getInt(R.string.saved_weight_of_user.toString(), 0)
        dailyLiquidRequirement.value = ourUserData?.getInt(R.string.saved_liquid_requirements_of_user.toString(), 1)
        currentlyDrunkLiquid.value = ourUserData?.getInt(R.string.saved_drunk_liquid_of_user.toString(), 0)
    }

    //Here we change the data in the storage, in case of changing the actual data
    fun saveData(newGender: Int, newWeight: Int, newLiquidRequirements: Int, newDrunkLiquid: Int){
        val editor = ourUserData?.edit()

        editor?.putInt(R.string.saved_gender_of_user.toString(), newGender)
        editor?.putInt(R.string.saved_weight_of_user.toString(), newWeight)
        editor?.putInt((R.string.saved_liquid_requirements_of_user).toString(), newLiquidRequirements)
        editor?.putInt((R.string.saved_drunk_liquid_of_user).toString(), newDrunkLiquid)

        editor?.apply()
    }

    //Here we change the data in the storage, in case of changing the gender
    fun saveGender(newGender: Int){
        val editor = ourUserData?.edit()
        editor?.putInt(R.string.saved_gender_of_user.toString(), newGender)
        editor?.apply()
    }

    //Here we change the data in the storage, in case of changing the weight
    fun saveWeight(newWeight: Int){
        val editor = ourUserData?.edit()
        editor?.putInt(R.string.saved_weight_of_user.toString(), newWeight)
        editor?.apply()
    }

    //Clear Data for testing
    fun clearFile(){
        val editor = ourUserData?.edit()
        editor?.clear()
        editor?.apply()
    }

    //Updating data at application start and adding a record about the current day to History, if it has not been created yet
    fun updateDataByStartActivity(weightIn: Long, genderIn: Int)
    {
        //Search the database for water requirements by weight and gender. Returns 2500 if didn't find.
        if(dailyLiquidRequirement.value == 0 || dailyLiquidRequirement.value == 1) {
            waterRequirementsUpdate(weightIn, genderIn)
        }

        //If there is no record in the database for the current day, then it is created
        addEntityInHistory()
    }

    //Search the database for water requirements by weight and gender. Returns 2500 if didn't find.
    private fun waterRequirementsUpdate(weightIn: Long, genderIn: Int){
        viewModelScope.launch {
            val waterRequirement: Int? = getWaterRequirementByWeightAndGender(weightIn, genderIn)

            if (waterRequirement != null)
            {
                dailyLiquidRequirement.value = waterRequirement
                saveData(userGenderIsFemale.value!!, weightOfUser.value!!, waterRequirement, currentlyDrunkLiquid.value!!)
            } else
            {
                dailyLiquidRequirement.value = 2500
            }
        }
        saveData(userGenderIsFemale.value!!, weightOfUser.value!!, dailyLiquidRequirement.value!!, currentlyDrunkLiquid.value!!)
    }

    //If there is no record in the database for the current day, then it is created
    fun addEntityInHistory()
    {
        viewModelScope.launch {

            val drunk = 0
            val history = getHistoryByDate(currentDate())
            if(history == null)
            {
                waterRequirementsUpdate(weightOfUser.value!!.toLong(), userGenderIsFemale.value!!)
                val requirement = dailyLiquidRequirement.value!!
                currentlyDrunkLiquid.value = drunk //A new day has begun, the value of the drink is set to zero
                saveData(
                    userGenderIsFemale.value!!, weightOfUser.value!!,
                    dailyLiquidRequirement.value!!, currentlyDrunkLiquid.value!!
                )
                insert(currentDate(), drunk, requirement,drunk*100/requirement, weightOfUser.value!!)
            }
        }
    }

    //Adding the water you drink to the water already drunk today
    fun addDrunkWater(waterIn:Int)
    {
        currentlyDrunkLiquid.value = currentlyDrunkLiquid.value?.plus(waterIn)
        updateDrunk()
        saveData(userGenderIsFemale.value!!, weightOfUser.value!!,
            dailyLiquidRequirement.value!!, currentlyDrunkLiquid.value!!)
    }

    //Change water requirement because of going for sport
    fun addWaterRequirementBecauseOfSportOrWeather(extraRequirement: Int){
        dailyLiquidRequirement.value = dailyLiquidRequirement.value?.plus(extraRequirement)

        updateRequirement()

        saveData(userGenderIsFemale.value!!, weightOfUser.value!!,
            dailyLiquidRequirement.value!!, currentlyDrunkLiquid.value!!)

    }

    //Return the current date in text format
    fun currentDate():String{
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
        val dateNow = Date()
        return simpleDateFormat.format(dateNow)
    }



    ////////////////////////////////////////////////////////////////////////////////////
    //Methods to interact with the repository:

    fun updateDrunk(){
        viewModelScope.launch{
            val history:History? = getHistoryByDate(currentDate())
            history?.drunk = currentlyDrunkLiquid.value!!
            history?.fulfillment = currentlyDrunkLiquid.value!! * 100 / dailyLiquidRequirement.value!!
            repository.updateHistory(history!!)
        }
    }

    fun updateRequirement(){
        viewModelScope.launch{
            val history:History? = getHistoryByDate(currentDate())
            history?.requirements = dailyLiquidRequirement.value!!
            history?.fulfillment = currentlyDrunkLiquid.value!! * 100 / dailyLiquidRequirement.value!!
            repository.updateHistory(history!!)
        }
    }

    fun insert(date:String, drunk: Int, requirements:Int, fulfillment:Int, weight: Int){
        viewModelScope.launch{
            val history = History(0L, date, drunk, requirements, fulfillment, weight)
            repository.insertInHistory(history)
            updateRequirement()
        }
    }

    suspend fun getWaterRequirementByWeightAndGender(weightIn: Long, genderIn: Int):Int?{
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