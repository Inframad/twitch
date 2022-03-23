package com.example.twitchapp.datasource.local

import com.example.twitchapp.database.streams.GameStreamEntity
import com.example.twitchapp.model.Result
import com.example.twitchapp.model.game.Game
import io.reactivex.rxjava3.core.Observable

interface LocalDatasource {

    suspend fun getGameStreamsPage(startId: Int, endId: Int): List<GameStreamEntity>
    suspend fun getGameStreamByAccessKey(accessKey: String): GameStreamEntity
    suspend fun saveGameStreams(gameStreams: List<GameStreamEntity>)
    suspend fun deleteAllGameStreams()
    suspend fun getGameStreamsFirstPage(): List<GameStreamEntity>
    suspend fun getGame(name: String): Game
    suspend fun isGameExist(name: String): Boolean
    fun getFavouriteGames(): Observable<Result.Success<List<Game>>>
    suspend fun updateGame(game: Game)
    suspend fun saveAndGetGame(game: Game): Game
}