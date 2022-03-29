package com.example.api.util

import com.example.twitchapp.model.NetworkState

interface NetworkConnectionChecker {

    fun getNetworkState(): NetworkState
}