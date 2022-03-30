package com.example.twitchapp.model.exception

class DatabaseException(state: DatabaseState): Exception(state.name)

enum class DatabaseState {
    EMPTY
}
