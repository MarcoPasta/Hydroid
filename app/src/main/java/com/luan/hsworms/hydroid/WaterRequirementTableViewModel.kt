package com.luan.hsworms.hydroid

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.luan.hsworms.hydroid.backend.database.AppRepository
import com.luan.hsworms.hydroid.backend.database.WaterRequirement
import kotlinx.coroutines.launch

/**
 * (type: AndroidViewModel) in order to be able to pass an Application-object to the view model. ViewModel to  WaterRequirementTableFragment. Contains functions for interacting with the database.
 *
 * @author Andrej Alpatov
 */
class WaterRequirementTableViewModel(application: Application):AndroidViewModel(application) {

    ////////////////////////////////////////////////////////////////////////////////////
    //Repository
    private val repository = AppRepository(application)
    private val waterRequirementsLiveData = repository.getWaterRequirementLiveData()
    ////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////
    //Methods to interact with the repository:
    /**
     * Adding an entry to the WaterRequirement Database table with data for for a specific gender and weight
     *
     * @param genderWoman   user gender (type: Boolean) true - female, false - male
     * @param weight        user weight (type: Int)
     * @param requirement   water demand (type: Int)
     */
    fun insert(genderWoman:Boolean, weight: Int, requirement: Int){
        viewModelScope.launch{
            val waterRequirement = WaterRequirement(0L, genderWoman, weight, requirement)
            repository.insertInRequirements(waterRequirement)
        }
    }

    /**
     * Updating an entry in the History Database table
     *
     * @param waterRequirement an entry in the WaterRequirement Database table, to be changed
     */
    fun update(waterRequirement: WaterRequirement){
        viewModelScope.launch {
            repository.updateRequirements(waterRequirement)
        }
    }

    /**
     * Deleting an entry in data base History table
     *
     * @param waterRequirement an entry in the WaterRequirement Database table, to be deleted
     */
    fun delete(waterRequirement: WaterRequirement){
        viewModelScope.launch {
            repository.deleteRequirements(waterRequirement)
        }
    }

    /**
     * Retrieving the amount of water to meet the needs of a specific gender and weight (passed as a parameters) from the Database WaterRequirement table
     * (Will be used for further development of functionality)
     *
     * @param weightIn  The date, the record for which you want to get from the Database History table. (type: Long)
     * @param genderIn  user gender (type: Int) 1 - female, 0 - male
     * @return          The record from the Database WaterRequirement tavble, which you want to get. (type: Int?)
     */
    fun getWaterRequirementByWeightAndGender(weightIn: Long, genderIn: Int): Int?{
        var waterRequirement: Int? = null
        viewModelScope.launch {
            waterRequirement = repository.getWaterRequirementByWeightAndGender(weightIn, genderIn)
        }
        return waterRequirement
    }

    /**
     * Retrieving all records from the Database WaterRequirement table
     * (Will be used for further development of functionality)
     *
     * @return List of WaterRequirement table entities (type: List<WaterRequirement>?)
     */
    fun getWaterRequirements():List<WaterRequirement>?{
        var waterRequirement:List<WaterRequirement>? = null
        viewModelScope.launch {
            waterRequirement = repository.getWaterRequirements()
        }
        return waterRequirement
    }
    ////////////////////////////////////////////////////////////////////////////////////


    ////////////////////////////////////////////////////////////////////////////////////
    //Get-Method for LiveData
    /**
     * Get-Method for LiveData. Returns all records from WaterRequirement table when the table changes
     */
    fun getLiveWaterRequirement():LiveData<List<WaterRequirement>> = waterRequirementsLiveData
    ////////////////////////////////////////////////////////////////////////////////////

}