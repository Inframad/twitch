package com.example.notification

import android.content.Context
import com.example.repository.notification.NotificationRepository
import com.example.twitchapp.model.UiState
import com.example.twitchapp.model.exception.DatabaseException
import com.example.twitchapp.model.notifications.TwitchNotification
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

@HiltViewModel
class NotificationsListViewModel
@Inject constructor(
    @ApplicationContext context: Context,
    private val notificationRepository: NotificationRepository
) : com.example.common.livedata.BaseViewModelLiveData(context) {

    private val listNotifications: MutableList<TwitchNotification> = mutableListOf()

    val scrollUpCommand = Command()
    val sendScrollStateCommand = Command()

    val uiState = Data<UiState<List<TwitchNotificationPresentation>>>()

    val toggleFabVisibilityCommand = Data<Boolean>()

    init {
        notificationRepository.getAllNotifications()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { uiState.setValue(UiState.Loading) }
            .flatMap { twitchNotifications ->
                Observable.just(twitchNotifications.map {
                    TwitchNotificationPresentation.fromModel(
                        it,
                        getString(com.example.common.R.string.scr_any_date_time_pattern)
                    )
                } to twitchNotifications)
            }
            .subscribe(
                { (twitchNotificationsPresentation, twitchNotifications) ->
                    listNotifications.clear()
                    listNotifications.addAll(twitchNotifications)

                    uiState.setValue(
                        if (twitchNotificationsPresentation.isEmpty()) UiState.Empty
                        else UiState.Loaded(twitchNotificationsPresentation)
                    )
                },
                {
                    if (it is DatabaseException) uiState.setValue(UiState.Empty)
                    else showToast(handleBaseError(it))

                }
            )
            .addToCompositeDisposable()

        notificationRepository.getNotificationsEvent()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                sendScrollStateCommand.setValue(Unit)
            }.addToCompositeDisposable()
    }

    fun onSendScrollStateCommand(scrollPosition: Int, canScrollDown: Boolean) {
        if ((scrollPosition != 0 || canScrollDown) && scrollPosition != -1)
            toggleFabVisibilityCommand.setValue(true)
    }

    fun onFloatingActionButtonClicked() {
        toggleFabVisibilityCommand.setValue(false)
        scrollUpCommand.setValue(Unit)
    }

    fun onRecyclerViewScrollStateChanged(canScrollUp: Boolean) {
        if (!canScrollUp) toggleFabVisibilityCommand.setValue(false)
    }

    fun onRightItemSwipe(layoutPosition: Int) {
        notificationRepository.deleteNotification(listNotifications[layoutPosition])
            .subscribe()
            .addToCompositeDisposable()
    }
}
