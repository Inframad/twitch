package com.example.twitchapp.datasource.local

import com.example.twitchapp.database.streams.GameStreamEntity
import com.example.twitchapp.model.Result
import com.example.twitchapp.model.game.Game
import kotlinx.coroutines.flow.Flow

interface LocalDatasource {

    suspend fun getGameStreamsPage(startId: Int, endId: Int): List<GameStreamEntity>
    suspend fun getGameStreamByAccessKey(accessKey: String): GameStreamEntity
    suspend fun saveGameStreams(gameStreams: List<GameStreamEntity>)
    suspend fun deleteAllGameStreams()
    suspend fun getGameStreamsFirstPage(): List<GameStreamEntity>
    suspend fun getGame(name: String): Game
    suspend fun saveGame(game: Game)
    suspend fun isGameExist(name: String): Boolean
    fun getFavouriteGames(): Flow<Result.Success<List<Game>>>
    suspend fun updateGame(game: Game)
}