package com.example.twitchapp.di.data

import com.example.twitchapp.BuildConfig
import com.example.twitchapp.data.network.AuthInterceptor
import com.example.twitchapp.data.network.TwitchGameStreamsApi
import com.example.twitchapp.data.network.TwitchGamesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    @Singleton
    fun provideClient(interceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

    @Provides
    @Singleton
    fun provideMoshiConverterFactory(): MoshiConverterFactory =
        MoshiConverterFactory.create()

    @Provides
    @Singleton
    fun provideRetrofit(
        moshiConverterFactory: MoshiConverterFactory,
        client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(moshiConverterFactory)
            .build()

    @Provides
    @Singleton
    fun provideTwitchStreamsApi(
        retrofit: Retrofit
    ): TwitchGameStreamsApi =
        retrofit.create(TwitchGameStreamsApi::class.java)

    @Provides
    @Singleton
    fun provideTwitchGamesApi(
        retrofit: Retrofit
    ): TwitchGamesApi =
        retrofit.create(TwitchGamesApi::class.java)
}