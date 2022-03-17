package com.example.twitchapp.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.twitchapp.R
import com.example.twitchapp.common.extensions.bindCommandAction
import com.example.twitchapp.databinding.ActivityMainBinding
import com.example.twitchapp.navigation.Navigator
import com.example.twitchapp.notification.NotificationConst
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private var navController: NavController? = null

    private val viewModel: MainActivityViewModel by viewModels()
    private val viewBinding: ActivityMainBinding by viewBinding()

    private val firebaseBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            viewModel.messageReceived(
                intent?.extras,
                AppScreen.fromResId(Navigator.getCurrentScreen(this@MainActivity)!!)
            )
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(
            firebaseBroadcastReceiver,
            IntentFilter(NotificationConst.INTENT_FILTER_FIREBASE)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initNavigation()
        bindViewModel()
        viewModel.initialized()
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(firebaseBroadcastReceiver)
    }

    private fun bindViewModel() {
        bindCommandAction(viewModel.toggleBottomNavigationViewVisibility) {
            viewBinding.bottomNavigation.isVisible = it
        }
        bindCommandAction(viewModel.sendIntentToGameScreenCommand) {
            sendBroadcast(Intent().apply {
                action = NotificationConst.INTENT_FILTER_GAME
                putExtras(it)
            })
        }
    }

    private fun initNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        navController?.let {
            NavigationUI.setupWithNavController(viewBinding.bottomNavigation, it)
            it.addOnDestinationChangedListener { _, destination, _ ->
                viewModel.onDestinationChanged(AppScreen.fromResId(destination.id))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController!!)
                || super.onOptionsItemSelected(item)
    }
}