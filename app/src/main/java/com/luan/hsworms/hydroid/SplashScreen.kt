package com.luan.hsworms.hydroid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore

/**
 * The SplashScreen class is always executed when the application starts and shows the splash screen. At the first start, the database table WaterRequirement is filled. After a set time, you go to the main screen (MainFragment) of the application.
 */
@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val pauseOnStart = 2000 //In milliseconds

        // This is used to hide the status bar and make the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        viewModel = ViewModelProvider(
            { ViewModelStore() },
            MainViewModelFactory(application)).get(MainViewModel::class.java)

        //Initializing an object with user data with data from a file
        viewModel.firstStart = applicationContext?.getSharedPreferences(
            getString(R.string.preferences_file_first_start),
            Context.MODE_PRIVATE
        )

        /////////////////////////////////////
        /////for debugging///////////////////
        //viewModel.clearFile()
        //viewModel.saveFirstStart(1)
        /////////////////////////////////////

        //Populate ScharedPreferences to check if the start is first
        viewModel.populateFirstStart()


        //Check if the Strat is first, if first calls fillingTheWaterRequirementTableAtTheFirstStart() function
        if(viewModel.isFirstStart == 1){//first start
            //viewModel.clearFile()
                viewModel.fillingTheWaterRequirementTableAtTheFirstStart()
        }


        /**
         * We used the postDelayed(Runnable, time) method to send a message with a delayed time.
         */
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, pauseOnStart.toLong()) // pauseOnStart is the delayed time in milliseconds.
    }
}
