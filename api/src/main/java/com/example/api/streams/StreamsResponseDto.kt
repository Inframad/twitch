package com.example.api.streams

import com.example.twitchapp.api.streams.GameStreamDto

class StreamsResponseDto(
    val data: List<GameStreamDto>,
    val pagination: Pagination
)

