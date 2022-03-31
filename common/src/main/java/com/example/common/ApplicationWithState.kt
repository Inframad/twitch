package com.example.common

interface ApplicationWithState {

    fun getCurrentState(): AppState?
}