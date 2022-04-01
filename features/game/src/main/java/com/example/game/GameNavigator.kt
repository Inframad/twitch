package com.example.game

import com.example.twitchapp.model.notifications.GameNotification

interface GameNavigator {

    fun openGame(notification: GameNotification)
    fun goBack()
}