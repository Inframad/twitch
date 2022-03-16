package com.example.twitchapp.ui

import androidx.annotation.IdRes
import com.example.twitchapp.R

enum class AppScreen {
    STREAMS,
    GAME,
    FAVOURITE,
    APP_REVIEW;

    companion object {
        fun fromResId(@IdRes id: Int) =
            when (id) {
                R.id.gameStreamsFragment -> STREAMS
                R.id.gameFragment -> GAME
                R.id.appReviewFragment -> APP_REVIEW
                R.id.favouriteGamesFragment -> FAVOURITE
                else -> throw IllegalArgumentException("AppScreens don't contain mapper for id ($id)")
            }
    }
}