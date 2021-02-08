package com.luan.hsworms.hydroid

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.luan.hsworms.hydroid.backend.database.AppRepository
import com.luan.hsworms.hydroid.backend.database.History
import com.luan.hsworms.hydroid.backend.database.WaterRequirement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

/**
 * MainViewModel class for MainFragment
 *
 */
class MainViewModel(application: Application): AndroidViewModel(application) {

    private val TAG: String = "MainViewModel"

    //Repository
    private val repository = AppRepository(application)

    //TODO: Encapsulate the Live Data
    var userGenderIsFemale = MutableLiveData<Int>()
    var weightOfUser = MutableLiveData<Int>()
    var dailyLiquidRequirement = MutableLiveData<Int>()
    var currentlyDrunkLiquid = MutableLiveData<Int>()
    var isFirstStart: Int? = 1
    private var semaphore = 1


    /**
     * Used for the first start. (1 - the first start; 0 - it's not the first start
     */
    var firstStart: SharedPreferences? = null

    /**
     * To save user data in internal storage (gender, weight, daily water requirement, currently drank water)
     */
    var ourUserData: SharedPreferences? = null


    init {
        userGenderIsFemale.value = 1
        weightOfUser.value = 60
        dailyLiquidRequirement.value = 1800
        currentlyDrunkLiquid.value = 0
    }

    /**
     * Assigning a value to a variable "isFirstStart" from local storage(SharedPrefereces).
     */
    fun populateFirstStart(){
        isFirstStart = firstStart?.getInt(R.string.isFirstStart.toString(), 1)
    }

    /**
     * Saving the value of the "isFirstStart" variable in local storage (SharedPreferences)
     *
     * @param firstStart_in The value, to be stored. 1 - first start; 0- not first start
     */
    fun saveFirstStart(firstStart_in: Int){
        val editor = firstStart?.edit()
        editor?.putInt(R.string.isFirstStart.toString(), firstStart_in)
        editor?.apply()
    }


    /**
     * Filling viewModel Live Data variables with values from internal storage (SharedPreferences)
     */
    fun populateViewModel(){

        userGenderIsFemale.value = ourUserData?.getInt(R.string.saved_gender_of_user.toString(), 1)
        weightOfUser.value = ourUserData?.getInt(R.string.saved_weight_of_user.toString(), 60)
        dailyLiquidRequirement.value = ourUserData?.getInt(R.string.saved_liquid_requirements_of_user.toString(), 1800)
        currentlyDrunkLiquid.value = ourUserData?.getInt(R.string.saved_drunk_liquid_of_user.toString(), 0)
    }

    /**
     * Here we change the data in the local storage (SharedPreferences), in case of changing the actual data
     *
     * @param newGender               user gender (type: Int) 1 - female, 0 - male
     * @param newWeight               user weight (type: Int)
     * @param newLiquidRequirements   water demand (type: Int)
     * @param newDrunkLiquid          amount of water drunk (type: Int)
     */
    private fun saveData(newGender: Int, newWeight: Int, newLiquidRequirements: Int, newDrunkLiquid: Int){
        val editor = ourUserData?.edit()

        editor?.putInt(R.string.saved_gender_of_user.toString(), newGender)
        editor?.putInt(R.string.saved_weight_of_user.toString(), newWeight)
        editor?.putInt((R.string.saved_liquid_requirements_of_user).toString(), newLiquidRequirements)
        editor?.putInt((R.string.saved_drunk_liquid_of_user).toString(), newDrunkLiquid)

        editor?.apply()
    }

    /**
     * Here we change the data in the storage, in case of changing the gender
     *
     *  @param newGender user gender (type: Int) 1 - female, 0 - male
     */
    fun saveGender(newGender: Int){
        val editor = ourUserData?.edit()
        editor?.putInt(R.string.saved_gender_of_user.toString(), newGender)
        editor?.apply()
    }

    /**
     * Here we change the data in the storage, in case of changing the weight
     *
     *  @param newWeight user weight (type: Int)
     */
    fun saveWeight(newWeight: Int){
        val editor = ourUserData?.edit()
        editor?.putInt(R.string.saved_weight_of_user.toString(), newWeight)
        editor?.apply()
    }

    /**
     * Helper function for debugging. Clears the local storage (SharedPreferences) with user data (gender, weight, daily water requirement, amount of water drunk per day).
     */
    fun clearFile(){
        val editor = ourUserData?.edit()
        editor?.clear()
        editor?.apply()
    }

    /**
     * Updating data at application start and adding a record about the current day to History, if it has not been created yet
     *
     * @param weightIn user weight (type: Int)
     * @param genderIn user gender (type: Int) 1 - female, 0 - male
     */
    fun updateDataByStartActivity(weightIn: Long, genderIn: Int)
    {
        //Search the database for water requirements by weight and gender. Setss 1800 if didn't find.
        //if(dailyLiquidRequirement.value == 1 || dailyLiquidRequirement.value == 500) {
            waterRequirementsUpdate(weightIn, genderIn)
        //}
        //If there is no record in the database for the current day, then it is created
        addEntityInHistory()
    }

    /**
     * Search the database for water requirements by weight and gender and stores itin liva data variable. Saves 1800 if didn't find.
     *
     * @param weightIn user weight (type: Int)
     * @param genderIn user gender (type: Int) 1 - female, 0 - male
     */
    private fun waterRequirementsUpdate(weightIn: Long, genderIn: Int){
        viewModelScope.launch {
            val waterRequirement: Int? = getWaterRequirementByWeightAndGender(weightIn, genderIn)
            Log.d(TAG, "waterRequirementsUpdate $waterRequirement")
            if (waterRequirement != null) //if found
            {
                dailyLiquidRequirement.value = waterRequirement
            } else //if not found
            {
                dailyLiquidRequirement.value = 1800
            }
        }
        //Save data in local storage
        saveData(userGenderIsFemale.value!!, weightOfUser.value!!, dailyLiquidRequirement.value!!, currentlyDrunkLiquid.value!!)
    }

    /**
     * If there is no record in the database table History for the current day, then it is created. The live date variables will also be updated. The amount of liquid you drink will be set to zero as a new day begins.
     */
    fun addEntityInHistory()
    {
        viewModelScope.launch {

            while(true){
                if (semaphore == 1){
                    semaphore = 0
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
                    semaphore = 1
                }
                    break
            }
        }
    }


    /**
     * Adding the water you drink to the water already drunk today. Updating live data variables, local storage and database.
     *
     * @param waterIn the water you drink (type: int)
     */
    fun addDrunkWater(waterIn:Int)
    {
        currentlyDrunkLiquid.value = currentlyDrunkLiquid.value?.plus(waterIn)
        saveData(userGenderIsFemale.value!!, weightOfUser.value!!,
            dailyLiquidRequirement.value!!, currentlyDrunkLiquid.value!!)
        updateDrunk()
    }

    /**
     * Change water requirement because of going for sport. Updating live data variables, local storage and database.
     *
     * @param extraRequirement The amount of water that must be additionally added to the current daily requirement due to activity. (type: int)
     */
    fun addWaterRequirementBecauseOfSportOrWeather(extraRequirement: Int){
        dailyLiquidRequirement.value = dailyLiquidRequirement.value?.plus(extraRequirement)

        updateRequirement()

        saveData(userGenderIsFemale.value!!, weightOfUser.value!!,
            dailyLiquidRequirement.value!!, currentlyDrunkLiquid.value!!)

    }

    /**
     * Converts the current date to text format and returns it.
     *
     * @return the current date in text format with patern: "dd.MM.yyyy" (type: String)
     */
    fun currentDate():String{
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
        val dateNow = Date()
        return simpleDateFormat.format(dateNow)
    }

    /**
     * Filling in the table of water demand values at the first start. Values are filled in for both genders in the weight range from 20 to 140 kg.
     */
    fun fillingTheWaterRequirementTableAtTheFirstStart(){
        for(i in 20..140){
            insertRequirements(true,  i,
                (600+(i*20)))
            insertRequirements(false,  i,
                (600+(i*21)))
            println("$i")
        }
    }



    ////////////////////////////////////////////////////////////////////////////////////
    //Methods to interact with the repository:

    /**
     * Save in the table History of the database the values of the fluid drunk for the current day and the percentage of the goal from the lava date variables.
     */
    private fun updateDrunk(){
        viewModelScope.launch{
            val history:History? = getHistoryByDate(currentDate())
            if (history != null){
                history.drunk = currentlyDrunkLiquid.value!!
                history.fulfillment = currentlyDrunkLiquid.value!! * 100 / dailyLiquidRequirement.value!!
                repository.updateHistory(history)
            } else
            {
                addEntityInHistory()
            }
        }
    }

    /**
     * Save in the table History of the database the values of the water requirements for the current day and the percentage of the goal from the lava date variables.
     */
    private fun updateRequirement(){
        viewModelScope.launch{
            val history:History? = getHistoryByDate(currentDate())
            history?.requirements = dailyLiquidRequirement.value!!
            history?.fulfillment = currentlyDrunkLiquid.value!! * 100 / dailyLiquidRequirement.value!!
            repository.updateHistory(history!!)
        }
    }

    /**
     * Adding a new record to the Database History table. Save in the table History of the database the values of the water requirements for the current day and the percentage of the goal from the lava date variables.
     *
     * @param date          The current date (type: String)
     * @param drunk         amount of water drunk (type: Int)
     * @param requirements  daily water demand (type: Int)
     * @param fulfillment   Percentage of Daily Goal Completion (type: Int)
     * @param weight        user weight (type: Int)
     */
    fun insert(date:String, drunk: Int, requirements: Int, fulfillment: Int, weight: Int){
        viewModelScope.launch{
            val history = History(0L, date, drunk, requirements, fulfillment, weight)
            repository.insertInHistory(history)
            updateRequirement()
        }
    }

    /**
     * Adding a new record to the Database WaterRequirement table
     *
     * @param genderWoman   User gender true - female, false - male (type: Boolean)
     * @param weight        User weight (type: Int)
     * @param requirement   daily water requirement(type: Int)
     */
    private fun insertRequirements(genderWoman:Boolean, weight: Int, requirement: Int){
        viewModelScope.launch{
            val waterRequirement = WaterRequirement(0L, genderWoman, weight, requirement)
            repository.insertInRequirements(waterRequirement)
        }
    }

    /**
     * Query the database of daily fluid requirements for gender and weight
     *
     * @param weightIn User weight (type: Int)
     * @param genderIn User gender (type: Int)
     * @return         daily water reyuirement (type: Int?)
     */
    private suspend fun getWaterRequirementByWeightAndGender(weightIn: Long, genderIn: Int):Int?{
        var waterRequirement:Int?
        withContext(Dispatchers.IO){
            waterRequirement = repository.getWaterRequirementByWeightAndGender(weightIn, genderIn)
        }
        return waterRequirement
    }

    /**
     * Query the database for an entry in the Database history table for a specific day.
     *
     * @param dateIn The date on which the data is requested (type: String)
     * @return       Entry from the Database History table (type: history?)
     */
    private suspend fun getHistoryByDate(dateIn: String):History?{
        var history:History?
        withContext(Dispatchers.IO){
            history = repository.getHistoryByDate(dateIn)
        }
        return history
    }
}