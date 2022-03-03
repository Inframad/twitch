package com.example.twitchapp.data.model

class DatabaseException(val state: DatabaseState): Exception()

enum class DatabaseState {
    EMPTY
}
