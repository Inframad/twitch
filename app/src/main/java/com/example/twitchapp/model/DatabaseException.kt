package com.example.twitchapp.model

class DatabaseException(val state: DatabaseState): Exception()

enum class DatabaseState {
    EMPTY
}
