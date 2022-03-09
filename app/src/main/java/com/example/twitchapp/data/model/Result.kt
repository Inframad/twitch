package com.example.twitchapp.data.model

sealed class Result<out T> {
    data class Success<out T> (val data: T): Result<T>()
    data class Error<out T> (val e: Throwable): Result<T>()
    object Empty: Result<Nothing>()
}
