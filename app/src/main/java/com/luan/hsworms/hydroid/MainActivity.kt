package com.luan.hsworms.hydroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.luan.hsworms.hydroid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout

        //had a problem: IllegalStateException: Activity [...] does not have a NavController set on [...]
        //Solution from https://stackoverflow.com/questions/50502269/illegalstateexception-link-does-not-have-a-navcontroller-set
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        //Add Navigation drawer
        NavigationUI.setupWithNavController(binding.navView, navController)

        //Add navigation drawer button in the app bar
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)

        //Handling Nawigation Drawer Clicks
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.userDataDialogFragment) {
                //Ready for implementation if needed

//                val newFragment = UserDataDialogFragment()
//                newFragment.show(supportFragmentManager, "userdata")
            }
        }
    }
// TODO: Change the call DialogFragment so that the fragment does not change in the background

    //Configure Navigation Drawer
    private fun setupNavigationMenu(navController: NavController) {
        val sideNavView = findViewById<NavigationView>(R.id.navView)
        sideNavView?.setupWithNavController(navController)
    }

    //Add an Up button in the app bar
    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }
}