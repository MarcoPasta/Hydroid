package com.luan.hsworms.hydroid

import android.content.SharedPreferences
import java.text.SimpleDateFormat
import java.util.*

/**
 * ViewModel for WeatherDialog Fragment
 */
class WeatherDialogViewModel {

    var weatherData: SharedPreferences? = null//To save the last weather conditions
    var date:String = ""
    var waterAddBecauseOfWeather: Int = 0


    //Clear Data for debugging
    /**
     * Cleaning up local storage (weather data)
     */
    private  fun clearFile(){
        val editor = weatherData?.edit()
        editor?.clear()
        editor?.apply()
    }


    /**
     *  Here we change the data (current date and additional amount of water) in the storage, in case of changing the actual data.
     *
     *  @param newDate      date, to be saved (type: String)
     *  @param waterAdd     additional amount of water (type: Int)
     */
    fun saveData(newDate: String, waterAdd: Int){
        val editor = weatherData?.edit()

        editor?.putString(R.string.saved_date.toString(), newDate)
        editor?.putInt(R.string.saved_water_add_because_of_weather.toString(), waterAdd)

        editor?.apply()
    }


    //Filling Variables with values from internal storage
    /**
     * Cleaning the local storage (the last weather conditions set for the current day are stored, namely the additional need for water) in the case of the first launch of the application. Writing new data in variables
     */
    fun populateVariables()
    {
        //for the first start сleaning storage
        if(weatherData?.getString(R.string.saved_date.toString(), null) == null) {
            clearFile()
        }

        date = weatherData!!.getString(R.string.saved_date.toString(), "01.01.1970")!!
        waterAddBecauseOfWeather = weatherData?.getInt(R.string.saved_water_add_because_of_weather.toString(), 0)!!
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
}