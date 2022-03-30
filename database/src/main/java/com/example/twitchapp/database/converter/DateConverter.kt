package com.example.twitchapp.database.converter

import androidx.room.TypeConverter
import java.time.Instant
import java.util.*

class DateConverter {
    @TypeConverter
    fun fromMillis(millis: Long): Date {
        return Date.from(Instant.ofEpochMilli(millis))
    }

    @TypeConverter
    fun toMillis(date: Date): Long {
        return date.time
    }
}