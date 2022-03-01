package com.example.twitchapp.data.converter

import com.example.twitchapp.data.model.GameStreamDTO

fun GameStreamDTO.toGameStream() =
    com.example.twitchapp.data.model.GameStream(
        username = username ?: "Unknown",
        gameName = gameName ?: "Unknown",
        viewerCount = viewerCount ?: 0,
        imageUrl = imageUrl?.replace("{width}x{height}", "1000x500")
    )