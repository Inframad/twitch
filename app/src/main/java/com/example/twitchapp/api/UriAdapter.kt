package com.example.twitchapp.api

import android.net.Uri
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonAdapter.Factory
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Types

class UriAdapter(private val annotation: List<Annotation>) : JsonAdapter<Uri>() {

    override fun fromJson(reader: JsonReader): Uri? {
        return if (annotation.isEmpty()) null else {
            val imageUriSettings = annotation.firstOrNull() as? ImageUri

            val path = reader.nextString()?.replace(
                "{width}x{height}",
                "${imageUriSettings?.width}x${imageUriSettings?.height}"
            )

            try {
                Uri.parse(path)
            } catch (ex: Exception) {
                null
            }

        }

    }

    override fun toJson(writer: JsonWriter, value: Uri?) {
        writer.value(value?.toString())
    }

    companion object {
        val INSTANCE: Factory = Factory { type, annotations, _ ->
            when (Types.getRawType(type)) {
                Uri::class.java -> UriAdapter(annotations.filterIsInstance<ImageUri>())
                else -> null
            }
        }
    }
}