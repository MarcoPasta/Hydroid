package com.luan.hsworms.hydroid

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel: ViewModel() {

    //TODO: Encapsulate the Live Data
    var glassSmall = MutableLiveData<Int>()
    var glassMiddle = MutableLiveData<Int>()
    var glassBig = MutableLiveData<Int>()
    var glassHuge = MutableLiveData<Int>()

    var glasses: SharedPreferences? = null      //To save user data in internal storage


    init {
        glassSmall.value = 50
        glassMiddle.value = 100
        glassBig.value = 150
        glassHuge.value = 200
    }

    //Filling viewModel variables with values from internal storage
    fun populateViewModel(){

        glassSmall.value = glasses?.getInt((R.string.savedSmallGlass).toString(), 50)
        glassMiddle.value = glasses?.getInt((R.string.savedMiddleGlass).toString(), 100)
        glassBig.value = glasses?.getInt((R.string.savedBigGlass).toString(), 300)
        glassHuge.value = glasses?.getInt((R.string.savedHugeGlass).toString(), 400)

        println("Populate ViewModel       ${glassSmall.value}")
    }

    //Here we change the data in the storage, in case of changing the actual data
    fun saveData(small: Int, middle: Int, big: Int, huge: Int){
        val editor = glasses?.edit()

        editor?.putInt(R.string.savedSmallGlass.toString(), small)
        editor?.putInt(R.string.savedMiddleGlass.toString(), middle)
        editor?.putInt((R.string.savedBigGlass).toString(), big)
        editor?.putInt((R.string.savedHugeGlass).toString(), huge)

        editor?.apply()
    }

}