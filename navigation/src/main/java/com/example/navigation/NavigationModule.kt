package com.example.navigation

import com.example.appreview.AppReviewNavigator
import com.example.game.GameNavigator
import com.example.streams.StreamsNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface NavigationModule {

    @Binds
    fun bindStreamsNavigation(streamsNavigation: Navigator): StreamsNavigator

    @Binds
    fun bindGameNavigator(gameNavigator: Navigator): GameNavigator

    @Binds
    fun bindAppReviewNavigator(appReviewNavigator: Navigator): AppReviewNavigator
}