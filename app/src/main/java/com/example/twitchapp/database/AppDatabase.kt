package com.example.twitchapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.twitchapp.database.game.GameDao
import com.example.twitchapp.database.game.GameEntity
import com.example.twitchapp.database.notification.GameNotificationDao
import com.example.twitchapp.database.streams.GameStreamDao
import com.example.twitchapp.database.streams.GameStreamEntity

@Database(
    entities = [
        GameStreamEntity::class,
        GameEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(UriConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gameStreamDao(): GameStreamDao
    abstract fun gameDao(): GameDao
    abstract fun gameNotificationDao(): GameNotificationDao
}