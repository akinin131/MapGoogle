package com.example.googlemapsapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.googlemapsapi.databinding.ActivityMainBinding
import com.example.googlemapsapi.fragment.MapFragment
import com.example.googlemapsapi.service.FileSendingService
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.pow
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        val fileSendingIntent = Intent(this, FileSendingService::class.java)
        ContextCompat.startForegroundService(this, fileSendingIntent)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_save_coordinates -> navigateToMapFragmentAndShowDialog()
            R.id.ic_list_coordinates -> navigateToListCoordinatesFragment()

            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun navigateToMapFragmentAndShowDialog() {
        val currentFragment = getCurrentFragment()
        if (currentFragment is MapFragment) {
            val mapCenter = currentFragment.getMapCenter()
            val latitude = formatDecimal(mapCenter.latitude, 4)
            val longitude = formatDecimal(mapCenter.longitude, 4)
            currentFragment.showDialog(latitude, longitude)
        } else {
            Toast.makeText(this, "Перейдите на карту", Toast.LENGTH_SHORT).show()
        }
    }

    private fun formatDecimal(value: Double, decimalPlaces: Int): Double {
        val factor = 10.0.pow(decimalPlaces.toDouble())
        return (value * factor).roundToInt() / factor
    }

    private fun navigateToListCoordinatesFragment() {
        val currentFragment = getCurrentFragment()
        if (currentFragment is MapFragment) {
            navController.navigate(R.id.action_mapFragment_to_listCoordinates)

        }
    }
    private fun getCurrentFragment(): Fragment? {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return navHostFragment.childFragmentManager.primaryNavigationFragment
    }

}

