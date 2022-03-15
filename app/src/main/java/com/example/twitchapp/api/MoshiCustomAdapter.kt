package com.example.twitchapp.api

import android.net.Uri
import android.util.Log
import com.example.twitchapp.api.game.GameDTO
import com.example.twitchapp.api.streams.GameStreamDTO
import com.squareup.moshi.*
import com.squareup.moshi.JsonAdapter.Factory
import kotlin.reflect.KClass

class MoshiCustomAdapter<T : Any>(private val type: KClass<T>) : JsonAdapter<Uri>() {

    @FromJson
    override fun fromJson(reader: JsonReader): Uri? {
        return when (type) {
            GameDTO::class -> {
                val path = try {
                    reader.nextString()?.replace(
                        "{width}x{height}",
                        "${ApiConst.IMAGE_WIDTH}x${ApiConst.IMAGE_HEIGHT}"
                    )
                } catch (e: Throwable) {
                    Log.e("CustomMoshiAdapter", e.toString())
                    ""
                }
                Uri.parse(path)
            }
            GameStreamDTO::class -> {

                val path = try {
                    reader.nextString()?.replace(
                        "{width}x{height}",
                        "${1000}x${1000}"
                    )
                } catch(e: Exception) {
                    Log.e("CustomMoshiAdapter", e.toString())
                    ""
                }
                Uri.parse(path)
            }
            else -> Uri.parse(reader.nextString())
        }
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: Uri?) {
        writer.value(value?.toString())
    }

    companion object {
        val INSTANCE: Factory = Factory { type, annotations, moshi ->
            when (Types.getRawType(type)) {
                GameDTO::class.java -> MoshiCustomAdapter(GameDTO::class)
                GameStreamDTO::class.java -> MoshiCustomAdapter(GameStreamDTO::class) //TODO Refactoring
                else -> null
            }
        }
    }
}