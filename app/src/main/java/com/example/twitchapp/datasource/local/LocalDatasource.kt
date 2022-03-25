package com.example.twitchapp.datasource.local

import com.example.twitchapp.database.streams.GameStreamEntity
import com.example.twitchapp.model.game.Game
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface LocalDatasource {

    fun getGameStreamsPage(startId: Int, endId: Int): Single<List<GameStreamEntity>>
    fun getGameStreamByAccessKey(accessKey: String): Single<GameStreamEntity>
    fun saveGameStreams(gameStreams: List<GameStreamEntity>): Completable
    fun deleteAllGameStreams(): Completable
    fun getGameStreamsFirstPage(): Single<List<GameStreamEntity>>
    fun getGame(name: String): Single<Game>
    fun getFavouriteGames(): Observable<List<Game>>
    fun updateGame(game: Game): Completable
    fun saveAndGetGame(game: Game): Single<Game>
}