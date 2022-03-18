package com.example.twitchapp.database.notification

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class TwitchNotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String?,
    val description: String?,
    val date: Date,
    val childType: String, //TODO
    val childId: Long
)