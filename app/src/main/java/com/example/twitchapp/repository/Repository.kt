package com.example.twitchapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.twitchapp.model.NetworkState
import com.example.twitchapp.model.game.Game
import com.example.twitchapp.model.streams.GameStream
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface Repository {

    fun getGameStreamsLiveData(): LiveData<PagingData<GameStream>>
    fun getCurrentNetworkState(): NetworkState
    fun getGame(name: String): Single<Game>
    fun getFavouriteGames(): Observable<List<Game>>
    fun updateGame(game: Game): Completable
}