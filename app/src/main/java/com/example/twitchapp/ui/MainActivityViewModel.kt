package com.example.twitchapp.ui

import android.content.Context
import com.example.twitchapp.common.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @ApplicationContext context: Context
) : BaseViewModel(context) {

    val toggleBottomNavigationViewVisibility = TCommand<Boolean>()

    fun onDestinationChanged(screen: AppScreen) {
        toggleBottomNavigationViewVisibility.setValue(
            when (screen) {
                AppScreen.APP_REVIEW -> false
                else -> true
            }
        )
    }
}