package com.example.twitchapp.datasource.remote

import com.example.twitchapp.api.game.TwitchGamesApi
import com.example.twitchapp.model.game.Game
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDatasourceImpl @Inject constructor(
    private val gamesApi: TwitchGamesApi
) : RemoteDatasource {

    override fun getGame(name: String): Single<Game> =
        gamesApi.getGames(name)
            .map { it.data.first().toModel() }
            .subscribeOn(Schedulers.io())
}
