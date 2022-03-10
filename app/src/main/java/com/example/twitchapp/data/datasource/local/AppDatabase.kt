package com.example.twitchapp.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.twitchapp.data.model.game.GameEntity
import com.example.twitchapp.data.model.streams.GameStreamEntity

@Database(entities = [GameStreamEntity::class, GameEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gameStreamDao(): GameStreamDao
    abstract fun gameDao(): GameDao
}