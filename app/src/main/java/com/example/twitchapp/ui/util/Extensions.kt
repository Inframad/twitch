package com.example.twitchapp.ui.util

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.twitchapp.R
import com.example.twitchapp.ui.AppScreen

fun ImageView.glideImage(url: String?) {
    val circularProgressDrawable = CircularProgressDrawable(this.context)

    circularProgressDrawable.apply {
        strokeWidth = 5f
        centerRadius = 30f
    }.start()

    Glide.with(this)
        .load(url)
        .placeholder(circularProgressDrawable)
        .error(R.drawable.image_error_placeholder)
        .into(this)
}

fun ImageView.setTintColor(@ColorRes colorRes: Int) {
    ImageViewCompat.setImageTintList(
        this,
        ColorStateList.valueOf(ContextCompat.getColor(this.context, colorRes))
    )
}

fun getAppScreenFromResId(@IdRes id: Int) =
    when(id) {
        R.id.game_streams_page -> AppScreen.STREAMS
        R.id.gameFragment -> AppScreen.GAME
        R.id.app_review_fragment -> AppScreen.APP_REVIEW
        R.id.favorite_games_page -> AppScreen.FAVOURITE
        else -> throw IllegalArgumentException("AppScreens don't contain mapper for id ($id)")
    }