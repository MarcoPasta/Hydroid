package com.luan.hsworms.hydroid

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import com.luan.hsworms.hydroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //Navigation components
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private  lateinit var  navController: NavController

    //AppBar configuration (was passiert, wenn wir clicken)
    private val idSets = setOf(R.id.mainFragment, R.id.settingsFragment, R.id.notificationFragment)
    private lateinit var appBarConfiguration: AppBarConfiguration


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    }

    //Add an Up button in the app bar
    override fun onSupportNavigateUp(): Boolean {
        val navController:NavController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}