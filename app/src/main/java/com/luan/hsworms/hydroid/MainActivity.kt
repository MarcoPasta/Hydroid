package com.luan.hsworms.hydroid

import android.app.AlertDialog
import android.app.PendingIntent.getActivity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.luan.hsworms.hydroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        //Set drawer
        drawerLayout = binding.drawerLayout
        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.nav_app_bar_open_drawer_description,
            R.string.nav_app_bar_close_drawer_description
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener {
            Log.i("SelectedListener 1", "NavigationItemSelectedListener")
            when(it.itemId){
                R.id.user_settings -> {
                    Log.i("SelectedListener 2", "NavigationItemSelectedListener")

                }
                R.id.notifications -> {
                    Log.i("SelectedListener ", "NavigationItemSelectedListener")
                }
            }
            true
        }



        //Add Navigation drawer
//        NavigationUI.setupWithNavController(binding.navView, navController)
//
//        //Add navigation drawer button in the app bar
//        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
//
//
//        //Handling Navigation Drawer Clicks
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            if (destination.id == R.id.item_1_userData) {
//
//                Log.i("SelectedListener 2", "NavigationItemSelectedListener")
//                val fragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
//                val myFrag: MainFragment = fragment?.childFragmentManager?.findFragmentById(R.id.mainFragment) as MainFragment
//                myFrag.showUserInputDialog()
////                val dialog = AlertDialog.Builder(this)
////                val dialogView = layoutInflater.inflate(R.layout.user_data_dialog_fragment, null)
////                dialog.setView(dialogView)
////                //dialog.setCancelable(false)
////                dialog.show()
//            }
//        }
    }

    //Configure Navigation Drawer
//    private fun setupNavigationMenu(navController: NavController) {
//        val sideNavView = findViewById<NavigationView>(R.id.navView)
//        sideNavView?.setupWithNavController(navController)
//    }
//
//    //Add an Up button in the app bar
//    override fun onSupportNavigateUp(): Boolean {
//        val navController = this.findNavController(R.id.nav_host_fragment)
//        return NavigationUI.navigateUp(navController, drawerLayout)
//    }


    //to correctly respond to clicks on drawer menu's items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
            return true

        return super.onOptionsItemSelected(item)
    }
}