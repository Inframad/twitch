package com.example.twitchapp.notification

object NotificationConst {
    const val CHANNEL_ID = "NOTIFICATION"
    const val CHANNEL_NAME = "FIREBASE NOTIFICATION"
    const val INTENT_FILTER_FIREBASE = "com.example.twitchapp.message"
    const val INTENT_FILTER_GAME = "com.example.twitchapp.screen.game"
    const val INTENT_FILTER_NOTIFICATIONS_SCREEN = "com.example.twitchapp.screen.notifications"
    const val TWITCH_NOTIFICATION_KEY = "TWITCH_NOTIFICATION"
    const val FCM_TOPIC_NAME = "dev"
    const val NOTIFICATION_ID = 0
    const val NOTIFICATION_REQUEST_CODE = 0

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