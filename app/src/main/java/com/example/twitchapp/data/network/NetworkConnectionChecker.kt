package com.example.twitchapp.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import com.example.twitchapp.data.model.NetworkState
import javax.inject.Inject

class NetworkConnectionChecker @Inject constructor(
    private val context: Context
) {

    fun getNetworkState(): NetworkState {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (connectivityManager.activeNetwork != null) NetworkState.AVAILABLE
            else NetworkState.NOT_AVAILABLE
        } else {
            if (connectivityManager.activeNetworkInfo != null) NetworkState.AVAILABLE
            else NetworkState.NOT_AVAILABLE
        }
    }
}