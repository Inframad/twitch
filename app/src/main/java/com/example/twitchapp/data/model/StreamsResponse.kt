package com.example.twitchapp.data.model

data class StreamsResponse(
    val data: List<GameStreamDTO>,
    val pagination: Pagination
)

data class Pagination(
    val cursor: String
)
