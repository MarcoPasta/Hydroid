package com.luan.hsworms.hydroid

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.luan.hsworms.hydroid.Backend.Database.AppRepository
import com.luan.hsworms.hydroid.Backend.Database.History
import com.luan.hsworms.hydroid.Backend.Database.WaterRequirement
import kotlinx.coroutines.launch
import java.util.*

//AndroidViewModel in order to be able to pass an Application-object to the view model
class WaterRequirementTableViewModel(application: Application):AndroidViewModel(application) {

    ////////////////////////////////////////////////////////////////////////////////////
    //Repository
    private val repository = AppRepository(application)
    private val waterRequirementsLiveData = repository.getWaterRequirementLiveData()
    ////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////
    //Methods to interact with the repository:
    fun insert(genderWoman:Boolean, weight: Int, requirement: Int){
        viewModelScope.launch{
            val waterRequirement = WaterRequirement(0L, genderWoman, weight, requirement)
            repository.insertInRequirements(waterRequirement)
        }
    }

    fun update(waterRequirement: WaterRequirement){
        viewModelScope.launch {
            repository.updateRequirements(waterRequirement)
        }
    }

    fun delete(waterRequirement: WaterRequirement){
        viewModelScope.launch {
            repository.deleteRequirements(waterRequirement)
        }
    }

    fun getWaterRequirementByWeightAndGender(weightIn: Long, genderIn: Boolean): Int?{
        var waterRequirement: Int? = null
        viewModelScope.launch {
            waterRequirement = repository.getWaterRequirementByWeightAndGender(weightIn, genderIn)
        }
        return waterRequirement
    }

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
    fun getLiveWaterRequirement():LiveData<List<WaterRequirement>> = waterRequirementsLiveData
    ////////////////////////////////////////////////////////////////////////////////////

}