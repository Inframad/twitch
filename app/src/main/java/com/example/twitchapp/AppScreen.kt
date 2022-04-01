package com.example.twitchapp

import androidx.annotation.IdRes

enum class AppScreen {
    STREAMS,
    GAME,
    FAVOURITE,
    NOTIFICATIONS,
    APP_REVIEW;

    companion object {
        fun fromResId(@IdRes id: Int) =
            when (id) {
                com.example.navigation.R.id.gameStreamsFragment -> STREAMS
                com.example.game.R.id.gameFragment-> GAME
                com.example.appreview.R.id.appReviewFragment -> APP_REVIEW
                com.example.favourite.R.id.favouriteGamesFragment -> FAVOURITE
                com.example.notification.R.id.notificationsListFragment -> NOTIFICATIONS
                else -> throw IllegalArgumentException("AppScreens don't contain mapper for id ($id)")
            }
    }
}