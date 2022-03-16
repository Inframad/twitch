package com.example.twitchapp.datasource

import com.example.twitchapp.datasource.local.LocalDatasource
import com.example.twitchapp.datasource.local.LocalDatasourceImpl
import com.example.twitchapp.datasource.remote.RemoteDatasource
import com.example.twitchapp.datasource.remote.RemoteDatasourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DatasourceModule {

    @Binds
    @Singleton
    abstract fun bindLocalDatasource(localDatasourceImpl: LocalDatasourceImpl): LocalDatasource

    @Binds
    @Singleton
    abstract fun bindRemoteDatasource(remoteDatasourceImpl: RemoteDatasourceImpl): RemoteDatasource
}