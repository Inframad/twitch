package com.example.navigation

import android.view.MenuItem
import androidx.navigation.NavController
import androidx.navigation.ui.onNavDestinationSelected
import com.example.appreview.AppReviewNavigator
import com.example.game.GameFragmentArgs
import com.example.game.GameNavigator
import com.example.streams.StreamsNavigator
import com.example.twitchapp.model.notifications.GameNotification
import com.example.twitchapp.model.streams.GameStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Navigator @Inject constructor() :
    StreamsNavigator,
    GameNavigator,
    AppReviewNavigator {
    var navController: NavController? = null

    override fun openGame(notification: GameNotification) {
        navigator.navController?.navigate(
            com.example.game.R.id.nav_game,
            GameFragmentArgs(notification = notification).toBundle()
        )
    }

    override fun openGame(stream: GameStream) {
        navigator.navController?.navigate(
            com.example.game.R.id.nav_game,
            GameFragmentArgs(stream = stream).toBundle()
        )
    }

    override fun goBack() {
        navigator.navController?.popBackStack()
    }

    companion object {

        private val navigator: Navigator = Navigator()

        fun setNavController(navController: NavController) {
            if (navigator.navController == null) {
                synchronized(navigator) {
                    if (navigator.navController == null) {
                        navigator.navController = navController
                    }
                }
            }
        }

        fun get() = navigator

        fun onNavDestinationSelected(item: MenuItem) =
            navigator.navController?.let { item.onNavDestinationSelected(it) }
                ?: false
    }
}