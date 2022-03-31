package com.example.firebaseservice

object NotificationConst {
    object MessageKeys {
        const val MESSAGE_ID = "google.message_id"
        const val NOTIFICATION_TYPE = "notification_type"
        const val GAME_NAME = "game_name"
        const val STREAMER_NAME = "streamer_name"
        const val VIEWERS_COUNT = "viewers_count"
    }

    object NotificationType {
        const val STREAMS = "STREAMS"
        const val GAME = "GAME"
    }
}