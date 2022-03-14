package com.example.twitchapp.datasource.remote

import com.example.twitchapp.model.Result
import com.example.twitchapp.model.game.Game

interface RemoteDatasource {

    suspend fun getGame(name: String): Result<Game>
}