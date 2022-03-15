package com.example.twitchapp.api

import android.content.Context
import com.example.twitchapp.BuildConfig
import com.example.twitchapp.api.game.TwitchGamesApi
import com.example.twitchapp.api.streams.TwitchGameStreamsApi
import com.example.twitchapp.api.util.NetworkConnectionChecker
import com.example.twitchapp.api.util.NetworkConnectionCheckerImpl
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideMoshi(): Moshi =
        Moshi.Builder()
            .add(UriAdapter.INSTANCE)
            .addLast(KotlinJsonAdapterFactory()) //TODO Inject
            .build()

    @Provides
    @Singleton
    fun provideClient(interceptor: AuthInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()

    @Provides
    @Singleton
    fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory =
        MoshiConverterFactory.create(moshi)

    @Provides
    @Singleton
    fun provideRetrofit(
        moshiConverterFactory: MoshiConverterFactory,
        client: OkHttpClient,
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

    @Provides
    @Singleton
    fun provideNetworkConnectionChecker(
        @ApplicationContext context: Context
    ): NetworkConnectionChecker =
        NetworkConnectionCheckerImpl(context)
}