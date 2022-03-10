package com.example.twitchapp.ui.util

import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.example.twitchapp.R

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