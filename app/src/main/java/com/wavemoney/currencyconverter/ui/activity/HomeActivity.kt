package com.wavemoney.currencyconverter.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.wavemoney.currencyconverter.R
import com.wavemoney.currencyconverter.databinding.ActivityHomeBinding
import com.wavemoney.currencyconverter.utility.delegateutils.activityViewBinding
import com.wavemoney.currencyconverter.utility.kodeinViewModel
import com.wavemoney.currencyconverter.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import android.app.AlarmManager

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import com.wavemoney.currencyconverter.utility.setBooleanPref
import org.kodein.di.android.x.closestDI


/**
 * Home Activity
 * Set Toolbar
 * Handle Navhost clicks and menu navhost
 * Set Refresh Logic (modify refresh variable in homeviewmodel every 30 minutes).
 */
class HomeActivity : AppCompatActivity(), DIAware {
    override val di by closestDI()
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var toggle: ActionBarDrawerToggle
    val homeViewModel: HomeViewModel by kodeinViewModel()
    private val binding by activityViewBinding(ActivityHomeBinding::inflate)

    companion object {
        var hvm: HomeViewModel? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setToolbar()
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcvHomeNavHostFragment) as NavHostFragment
        navController = navHostFragment.findNavController()
        hvm = homeViewModel

        settingDrawerToggle()
        navListener()
        triggerAlarm()
    }

    private fun triggerAlarm() {

        val repeatTime = 1800L * 1000L  //30 minutes
        val processTimer = getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, HomeActivity.TimerReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        //Repeat alarm every second
        processTimer.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            repeatTime,
            pendingIntent
        )
    }

    /**
     * This is the timer receiver class
     * Modify refresh variables in homeViewmodel every 30 minutes.
     */
    //This is called every second (depends on repeatTime)
    class TimerReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            //Set Boolean true every 30 minutes
            Log.d("brnyr", "onReceivedTimer")
            setBooleanPref(context!!, "Timer", "Timer", true)
        }
    }

    /**
     * Setting the drawer toggle icon
     */
    private fun settingDrawerToggle() {
        //setting the drawer toggle
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            toggle = ActionBarDrawerToggle(
                this, binding.drlHome, binding.include.tlbToolbar, R.string.open, R.string.close
            )

            binding.drlHome.addDrawerListener(toggle)
            toggle.syncState()
        }
    }

    /**
     * Listen the nav click events and trigger responsible actions
     */
    private fun navListener() {
        binding.ngvHome.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_available -> {
                    navController.popBackStack(R.id.availableCurrenciesFragment, true)
                    navController.navigate(R.id.availableCurrenciesFragment)
                    closeDrawer()
                }
                R.id.nav_liveRates -> {
                    navController.popBackStack(R.id.availableCurrenciesFragment, false)
                    navController.navigate(R.id.liveRatesFragment)
                    closeDrawer()
                }
                R.id.nav_convert -> {
                    navController.popBackStack(R.id.availableCurrenciesFragment, false)
                    navController.navigate(R.id.convertFragment)
                    closeDrawer()
                }
                else -> {
                    closeDrawer()
                }
            }
            false
        }
    }

    /**
     * This method is about closing the drawer.
     *
     */
    private fun closeDrawer() {
        if (binding.drlHome.isDrawerOpen(GravityCompat.START)) {
            binding.drlHome.closeDrawer(GravityCompat.START)
        }
    }

    /**
     * setting the toolbar
     */
    private fun setToolbar() {
        setSupportActionBar(binding.include.tlbToolbar)
    }

    /**
     * handle back press action
     * If drawer is open, close the drawer first
     * If Fragment is child nav fragment, then redirect to default nav host fragment, parent fragment(Available Currencies Fragment)
     */
    override fun onBackPressed() {
        if (binding.drlHome.isDrawerOpen(GravityCompat.START)) {
            binding.drlHome.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}