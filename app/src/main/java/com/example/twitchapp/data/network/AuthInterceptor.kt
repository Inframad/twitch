package com.example.twitchapp.data.network

import com.example.twitchapp.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder()
                .header("Authorization", "Bearer ${BuildConfig.TOKEN}")
                .header("Client-ID", BuildConfig.CLIENT_ID)
                .build()
        )
    }
}