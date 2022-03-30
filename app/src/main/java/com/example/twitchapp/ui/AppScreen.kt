package com.example.twitchapp.ui

import androidx.annotation.IdRes
import com.example.twitchapp.R

enum class AppScreen {
    STREAMS,
    GAME,
    FAVOURITE,
    NOTIFICATIONS,
    APP_REVIEW;

    companion object {
        fun fromResId(@IdRes id: Int) =
            when (id) {
                com.example.streams.R.id.gameStreamsFragment -> STREAMS
                com.example.streams.R.id.gameFragment -> GAME
                R.id.appReviewFragment -> APP_REVIEW
                com.example.favourite.R.id.favouriteGamesFragment-> FAVOURITE
                R.id.notificationsListFragment -> NOTIFICATIONS
                else -> throw IllegalArgumentException("AppScreens don't contain mapper for id ($id)")
            }
    }
}