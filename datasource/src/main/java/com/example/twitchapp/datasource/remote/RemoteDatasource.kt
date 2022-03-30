package com.example.twitchapp.datasource.remote

import com.example.twitchapp.model.game.Game
import io.reactivex.rxjava3.core.Single

interface RemoteDatasource {

    fun getGame(name: String): Single<Game>
}