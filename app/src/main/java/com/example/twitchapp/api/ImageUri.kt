package com.example.twitchapp.api

import com.squareup.moshi.JsonQualifier

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class ImageUri(val height: Int, val width: Int)


