package com.example.twitchapp.api.streams

import android.net.Uri
import com.example.twitchapp.api.ApiConst
import com.squareup.moshi.*
import javax.inject.Inject

class UriJsonAdapter @Inject constructor() : JsonAdapter<Uri>() {

    @ToJson
    override fun toJson(writer: JsonWriter, value: Uri?) {
        writer.value(value?.toString())
    }

    @FromJson
    override fun fromJson(reader: JsonReader): Uri? {
        val path = reader.nextString()?.replace(
            "{width}x{height}",
            "${ApiConst.IMAGE_WIDTH}x${ApiConst.IMAGE_HEIGHT}"
        )
        return Uri.parse(path)
    }
}
