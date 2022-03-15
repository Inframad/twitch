package com.example.twitchapp.api.streams

import android.net.Uri
import com.example.twitchapp.api.ApiConst
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson
import javax.inject.Inject

class UriJsonStreamAdapter @Inject constructor() {

    @ToJson fun toJson(@StreamUri uri: Uri?) =
        uri?.toString()

    @FromJson @StreamUri fun fromJson(url: String?): Uri? {
        val path = url?.replace(
            "{width}x{height}",
            "${ApiConst.IMAGE_HEIGHT}x${ApiConst.IMAGE_WIDTH}"
        )
        return Uri.parse(path)
    }
}

class UriJsonGameAdapter @Inject constructor() {

    @ToJson fun toJson(@GameUri uri: Uri?) =
        uri?.toString()

    @FromJson @GameUri fun fromJson(url: String?): Uri? {
        val path = url?.replace(
            "{width}x{height}",
            "${1000}x${1000}"
        )
        return Uri.parse(path)
    }
}

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class GameUri

@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class StreamUri


