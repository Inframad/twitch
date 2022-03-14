package com.example.twitchapp.api

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import com.example.twitchapp.model.NetworkState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NetworkConnectionChecker @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun getNetworkState(): NetworkState {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (connectivityManager.activeNetwork != null) NetworkState.AVAILABLE
            else NetworkState.NOT_AVAILABLE
        } else {
            @Suppress("DEPRECATION")
            if (connectivityManager.activeNetworkInfo != null) NetworkState.AVAILABLE
            else NetworkState.NOT_AVAILABLE
        }
    }
}