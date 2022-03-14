package com.example.twitchapp.model

class DatabaseException(state: DatabaseState): Exception(state.name)

enum class DatabaseState {
    EMPTY
}
