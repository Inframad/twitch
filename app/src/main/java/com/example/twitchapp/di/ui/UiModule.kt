package com.example.twitchapp.di.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.twitchapp.presentation.GameStreamViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
interface UiModule {

    @Binds
    @Singleton
    fun bindViewModelFactory(
        factory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(GameStreamViewModel::class)
    fun bindGameStreamViewModel(viewModel: GameStreamViewModel): ViewModel
}