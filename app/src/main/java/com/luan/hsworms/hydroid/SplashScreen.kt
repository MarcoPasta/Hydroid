package com.luan.hsworms.hydroid

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Suppress("DEPRECATION")
class SplashScreen : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        var pauseOnStart = 2000

        // This is used to hide the status bar and make
        // the splash screen as a full screen activity.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        viewModel = ViewModelProvider(ViewModelStoreOwner{-> ViewModelStore() },
            MainViewModelFactory(application)).get(MainViewModel::class.java)

        //Initializing an object with user data with data from a file
        viewModel.firstStart = applicationContext?.getSharedPreferences(
            getString(R.string.preferences_file_first_start),
            Context.MODE_PRIVATE
        )
//
//        //Initializing an object with data for first start with data from a file
//        viewModel.ourUserData = applicationContext?.getSharedPreferences(
//            getString(R.string.preference_file_key),
//            Context.MODE_PRIVATE
//        )


        //TODO: Auskommentieren
        /////for debugging///////////////////
        //viewModel.clearFile()
        //viewModel.saveFirstStart(1)
        /////////////////////////////////////



//
        //Populate ScharedPreferences to check if the start is first
        viewModel.populateFirstStart()
//
//        //Calling the function of initializing variables with values from internal storage
//        viewModel.populateViewModel()
//
        //Check if the Strat is first
        if(viewModel.isFirstStart == 1){//first start
            //viewModel.clearFile()
                viewModel.fillingTheWaterRequirementTableAtTheFirstStart()
        }
//        else{//not first start
//            viewModel.updateDataByStartActivity(viewModel.weightOfUser.value!!.toLong(),
//                viewModel.userGenderIsFemale.value!!)
//        }


        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, pauseOnStart.toLong()) // pauseOnStart is the delayed time in milliseconds.
    }
}
