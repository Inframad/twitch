package com.example.twitchapp.ui

sealed class UiState<out T> {
    data class Loaded<out T>(val data: T): UiState<T>()
    data class Error(val msg: String): UiState<Nothing>()
    object Loading: UiState<Nothing>()
    object Empty : UiState<Nothing>()
}