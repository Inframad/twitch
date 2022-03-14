package com.example.twitchapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.twitchapp.database.game.GameDao
import com.example.twitchapp.database.game.GameEntity
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
abstract class AppDatabase : RoomDatabase() {

    abstract fun gameStreamDao(): GameStreamDao
    abstract fun gameDao(): GameDao
}