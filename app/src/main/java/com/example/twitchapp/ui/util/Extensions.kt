package com.example.twitchapp.ui.util

import android.content.res.ColorStateList
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
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

fun ImageView.setTintColor(@ColorRes colorRes: Int) {
    ImageViewCompat.setImageTintList(
        this,
        ColorStateList.valueOf(ContextCompat.getColor(this.context, colorRes))
    )
}