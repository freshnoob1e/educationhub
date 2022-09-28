package com.educationhub.mobi

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.educationhub.mobi.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        // Drawer navigation
        val drawerLayout: DrawerLayout = binding.drawerLayout
        // Bottom navigation
        val navBottomView: BottomNavigationView =
            findViewById(R.id.bottom_navigation_view)
        val navView: NavigationView = binding.navView
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.findNavController()

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_profile,
                R.id.nav_career,
                R.id.nav_setting,
                R.id.nav_progress,
                R.id.nav_certificate_list,
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navBottomView.setupWithNavController(navController)

        // Get bottom nav listener
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                in arrayListOf(
                    R.id.nav_course_learn,
                    R.id.nav_course_assessment,
                    R.id.nav_course_detail,
                    R.id.nav_certificate_detail,
                    R.id.nav_career,
                    R.id.nav_setting,
                    R.id.nav_policy,
                    R.id.nav_help,
                    R.id.nav_blog_list,
                    R.id.nav_profile
                ) -> {
                    navView.visibility = View.GONE
                    navBottomView.visibility = View.GONE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    this.supportActionBar?.show()
                }
                in arrayListOf(
                    R.id.nav_course_assessment_fail,
                    R.id.nav_course_assessment_pass
                ) -> {
                    navView.visibility = View.GONE
                    navBottomView.visibility = View.GONE
                    supportActionBar?.hide()
                }
                else -> {
                    navView.visibility = View.VISIBLE
                    navBottomView.visibility = View.VISIBLE
                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
                    this.supportActionBar?.show()
                }
            }
        }
        // Manage bottom nav visibility etc

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController =
            findNavController(R.id.nav_host_fragment_content_main)
        if (navController.currentDestination?.id == R.id.nav_certificate_detail) {
            navController.popBackStack(R.id.nav_home, true)
            navController.navigate(R.id.nav_certificate_list)
            return super.onSupportNavigateUp()
        }
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}