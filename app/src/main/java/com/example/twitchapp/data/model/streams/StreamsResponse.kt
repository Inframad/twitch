package com.example.twitchapp.data.model.response

import com.example.twitchapp.data.model.streams.GameStreamDTO

data class StreamsResponse(
    val data: List<GameStreamDTO>,
    val pagination: Pagination
)

data class Pagination(
    val cursor: String
)
