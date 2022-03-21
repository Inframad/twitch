package com.example.twitchapp.database.notification

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class TwitchNotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Date,
    val childType: TwitchNotificationType,
    val childId: Long
)