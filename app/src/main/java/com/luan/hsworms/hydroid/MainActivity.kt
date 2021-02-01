package com.luan.hsworms.hydroid

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.luan.hsworms.hydroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivityFile"

    //Navigation components
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private  lateinit var  navController: NavController

    //AppBar configuration (was passiert, wenn wir clicken)
    private val idSets = setOf(R.id.mainFragment, R.id.settingsFragment, R.id.notificationFragment)
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var notificationViewModel: NotificationViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d(TAG, "onCreate called")

        //setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = binding.drawerLayout
        navView = binding.navView
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(idSets, drawerLayout)

        //Verbinden navControlller und appBarConfiguration
        setupActionBarWithNavController(navController, appBarConfiguration)

        //Verbinden Menu mit NavController
        navView.setupWithNavController(navController)


        ////////////////////////////////////////////////////////////////////////////
        /*
         * Dieser Bereich gehört der Bundesrepublik Deutschland
         *
         */

        ///////////////////////////////////////////////////////////////////////////
        // Area Reserve for calling the AlarmManager on Creating the application.
        Log.d(TAG, "Entering reserved AlarmManager space")
        // Creating an instance of ViewModel Object
        notificationViewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        // Make a reference to object and SharedPreferences
        notificationViewModel.notificationPreference = getSharedPreferences("NotificationPreference", Context.MODE_PRIVATE)
        // Load SharedPreferences
        notificationViewModel.loadData()

        // If Notifications are allowed, we can create the AlarmManagerStuff
        if(notificationViewModel.canSendHelpDrinkNotification()) {
            Log.d(TAG, "HelpDrink function is enabled")

            // Times maybe need to be checked here
            /* TODO: Insert Hours & Minutes from NotificationViewModel here so it can be used to determin when an
             *          notification can be sent. 
             */
            AlarmService.setAlarm(this)
        } else {
            Log.d(TAG, "HelpDrink is disabled")
        }

        Log.d(TAG, "End of onCreate")
        //////////////////////////////////////////////////////////////////////////
    }

    //Add an Up button in the app bar
    override fun onSupportNavigateUp(): Boolean {
        val navController:NavController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}