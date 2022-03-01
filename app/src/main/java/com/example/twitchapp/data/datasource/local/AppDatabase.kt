package com.example.twitchapp.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.twitchapp.data.model.GameStream

@Database(entities = [GameStream::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun gameStreamDao(): GameStreamDao
}