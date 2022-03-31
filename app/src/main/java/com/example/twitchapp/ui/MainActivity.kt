package com.example.twitchapp.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.onNavDestinationSelected
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.common.extensions.bindActionLiveData
import com.example.twitchapp.R
import com.example.twitchapp.TWITCH_NOTIFICATION_KEY
import com.example.twitchapp.databinding.ActivityMainBinding
import com.example.twitchapp.model.notifications.GameNotification
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private var navController: NavController? = null

    private val viewModel: MainActivityViewModel by viewModels()
    private val viewBinding: ActivityMainBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initNavigation()
        bindViewModel()
        init()
    }

    private fun bindViewModel() {
        with(viewModel) {
            bindActionLiveData(toggleBottomNavigationViewVisibility) {
                viewBinding.bottomNavigation.isVisible = it
            }
            bindActionLiveData(showToastCommand) {
                Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
            }
            bindActionLiveData(navigateToGameScreenCommand) {
                findNavController(R.id.nav_host_fragment)
                    .navigate(com.example.streams.R.id.gameFragment, it.toBundle())
            }
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

    private fun init() {
        viewModel.initialized()
        intent?.extras?.getParcelable<GameNotification>(TWITCH_NOTIFICATION_KEY)
            ?.let {
                viewModel.onIntent(it)
                intent.removeExtra(TWITCH_NOTIFICATION_KEY)
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