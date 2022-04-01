package com.example.streams

import com.example.twitchapp.model.streams.GameStream

interface StreamsNavigator {

    fun openGame(stream: GameStream)
}