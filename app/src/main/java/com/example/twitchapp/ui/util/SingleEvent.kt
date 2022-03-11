package com.example.twitchapp.ui.util

open class SingleEvent<out T>(private val content: T) {
    private var hasBeenHandled = false
    val data: T = content
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }
}