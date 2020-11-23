package com.grappim.myvpnclient.ui

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.VpnService
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.grappim.myvpnclient.R
import com.grappim.myvpnclient.vpn.MyLocalVpnService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.drawerLayout
import kotlinx.android.synthetic.main.activity_main.navigationView
import kotlinx.android.synthetic.main.activity_main.toolbar
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main),
    NavigationView.OnNavigationItemSelectedListener {

    companion object {
        const val VPN_REQUEST_CODE = 421232
        const val LOCATION_PERMISSION_CODE = 9665
    }

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavigation()
        initViews()
    }

    override fun onResume() {
        checkPermissions()
        super.onResume()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == VPN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val intent = Intent(this, MyLocalVpnService::class.java)
            startService(intent)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp() =
        findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        item.isChecked = true
        drawerLayout.closeDrawers()
        return displaySelectedScreen(item.itemId)
    }

    private fun checkPermissions() {
        if (ActivityCompat
                .checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_CODE
            )
        } else {

        }
    }

    private fun displaySelectedScreen(itemId: Int): Boolean {
        when (itemId) {
            R.id.navMenuNetworkInfo -> navController.navigate(R.id.navNetworkInfoFragment)
            R.id.navMenuVpn -> navController.navigate(R.id.navVpnFragment)
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setupNavigation() {
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navNetworkInfoFragment,
                R.id.navVpnFragment
            ), drawerLayout
        )

        setSupportActionBar(toolbar)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navigationView.setupWithNavController(navController)
        navigationView.setNavigationItemSelectedListener(this)
    }

    private fun initViews() {

    }

    private fun startVpn() {
        Timber.d("click startVpn")
        val intent = VpnService.prepare(this)
        intent?.let {
            startActivityForResult(intent, VPN_REQUEST_CODE)
        } ?: onActivityResult(VPN_REQUEST_CODE, Activity.RESULT_OK, null)
    }

    private fun endVpn() {
        Timber.d("click endVpn")
        val intent = Intent(this, MyLocalVpnService::class.java)
        intent.putExtra(MyLocalVpnService.COMMAND, MyLocalVpnService.COMMAND_STOP)
        startService(intent)
    }
}
