package com.example.twitchapp.ui.temp

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val intent = message.toIntent()
        intent.action = "com.example.twitchapp.message"
        applicationContext.sendBroadcast(intent)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        val foo = token
    }
}