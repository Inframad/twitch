package com.example.common.extensions

import android.content.res.ColorStateList
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide

fun ImageView.glideImage(uri: Uri?) {
    val circularProgressDrawable = CircularProgressDrawable(this.context)

    circularProgressDrawable.apply {
        strokeWidth = 5f
        centerRadius = 30f
    }.start()

    Glide.with(this)
        .load(uri)
        .placeholder(circularProgressDrawable)
        //.error(R.drawable.image_error_placeholder) TODO
        .into(this)
}

fun ImageView.setTintColor(@ColorRes colorRes: Int) {
    ImageViewCompat.setImageTintList(
        this,
        ColorStateList.valueOf(ContextCompat.getColor(this.context, colorRes))
    )
}