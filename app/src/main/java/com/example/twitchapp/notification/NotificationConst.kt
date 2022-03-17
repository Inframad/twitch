package com.example.twitchapp.notification

object NotificationConst {
    const val CHANNEL_ID = "NOTIFICATION"
    const val CHANNEL_NAME = "FIREBASE NOTIFICATION"
    const val INTENT_FILTER_FIREBASE = "com.example.twitchapp.message"
    const val INTENT_FILTER_GAME = "com.example.twitchapp.game"

    object MessageKeys {
        const val NOTIFICATION_TYPE = "notification_type"
        const val TITLE = "title"
        const val DESCRIPTION = "description"
        const val GAME_NAME = "game_name"
        const val STREAMER_NAME = "streamer_name"
        const val VIEWERS_COUNT = "viewers_count"
    }

    object NotificationType {
        const val STREAMS = "STREAMS"
        const val GAME = "GAME"
    }
}