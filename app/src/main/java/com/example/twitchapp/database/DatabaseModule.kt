package com.example.twitchapp.database

import android.content.Context
import androidx.room.Room
import com.example.twitchapp.database.game.GameDao
import com.example.twitchapp.database.notification.GameNotificationDao
import com.example.twitchapp.database.notification.StreamNotificationDao
import com.example.twitchapp.database.notification.TwitchNotificationDao
import com.example.twitchapp.database.streams.GameStreamDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app-db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideGameStreamDao(database: AppDatabase): GameStreamDao =
        database.gameStreamDao()

    @Provides
    @Singleton
    fun provideGameDao(database: AppDatabase): GameDao =
        database.gameDao()

    @Provides
    @Singleton
    fun provideTwitchNotificationDao(database: AppDatabase): TwitchNotificationDao =
        database.twitchNotificationDao()

    @Provides
    @Singleton
    fun provideGameNotificationDao(database: AppDatabase): GameNotificationDao =
        database.gameNotificationDao()

    @Provides
    @Singleton
    fun provideStreamNotificationDao(database: AppDatabase): StreamNotificationDao =
        database.streamNotificationDao()
}