package com.example.twitchapp.api.util

import com.example.twitchapp.model.NetworkState

interface NetworkConnectionChecker {

    fun getNetworkState(): NetworkState
}