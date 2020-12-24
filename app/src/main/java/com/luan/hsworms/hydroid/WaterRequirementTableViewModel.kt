package com.luan.hsworms.hydroid

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.luan.hsworms.hydroid.Backend.Database.AppRepository

//AndroidViewModel in order to be able to pass an Application-object to the view model
class WaterRequirementTableViewModel(application: Application):AndroidViewModel(application) {

    ////////////////////////////////////////////////////////////////////////////////////
    //Repository
    private val repository = AppRepository(application)
    private val waterRequirementsLiveData = repository.getWaterRequirementLiveData()
    ////////////////////////////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////
    //Methods to interact with the repository:

}