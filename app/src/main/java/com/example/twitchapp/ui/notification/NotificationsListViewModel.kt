package com.example.twitchapp.ui.notification

import android.content.Context
import com.example.twitchapp.common.BaseViewModel
import com.example.twitchapp.repository.notification.NotificationRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class NotificationsListViewModel
@Inject constructor(
    @ApplicationContext context: Context,
    private val repository: NotificationRepositoryImpl //TODO Inject interface impl
): BaseViewModel(context) {

    val notifications = repository.getAll()
}