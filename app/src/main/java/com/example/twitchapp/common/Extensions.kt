package com.example.twitchapp.common

import android.content.res.ColorStateList
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.twitchapp.R
import com.example.twitchapp.ui.AppScreen

fun ImageView.glideImage(uri: Uri?) {
    val circularProgressDrawable = CircularProgressDrawable(this.context)

    circularProgressDrawable.apply {
        strokeWidth = 5f
        centerRadius = 30f
    }.start()

    Glide.with(this)
        .load(uri)
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
    when (id) {
        R.id.gameStreamsFragment -> AppScreen.STREAMS
        R.id.gameFragment -> AppScreen.GAME
        R.id.appReviewFragment -> AppScreen.APP_REVIEW
        R.id.favouriteGamesFragment -> AppScreen.FAVOURITE
        else -> throw IllegalArgumentException("AppScreens don't contain mapper for id ($id)")
    }