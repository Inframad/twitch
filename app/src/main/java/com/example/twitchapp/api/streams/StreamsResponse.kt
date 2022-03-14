package com.example.twitchapp.api.streams

data class StreamsResponse(
    val data: List<GameStreamDTO>,
    val pagination: Pagination
)

data class Pagination(
    val cursor: String
)
