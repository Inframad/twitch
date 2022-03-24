package com.example.twitchapp.datasource.local

import com.example.twitchapp.database.streams.GameStreamEntity
import com.example.twitchapp.model.game.Game
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface LocalDatasource {

    suspend fun getGameStreamsPage(startId: Int, endId: Int): List<GameStreamEntity>
    suspend fun getGameStreamByAccessKey(accessKey: String): GameStreamEntity
    suspend fun saveGameStreams(gameStreams: List<GameStreamEntity>)
    suspend fun deleteAllGameStreams()
    suspend fun getGameStreamsFirstPage(): List<GameStreamEntity>
    fun getGame(name: String): Single<Game>
    fun isGameExist(name: String): Boolean
    fun getFavouriteGames(): Observable<List<Game>>
    fun updateGame(game: Game): Completable
    fun saveAndGetGame(game: Game): Single<Game>
}