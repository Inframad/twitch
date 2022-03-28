package com.example.twitchapp.ui.notification

import android.content.Context
import com.example.twitchapp.R
import com.example.twitchapp.common.livedata.BaseViewModelLiveData
import com.example.twitchapp.model.exception.DatabaseException
import com.example.twitchapp.repository.notification.NotificationRepository
import com.example.twitchapp.ui.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import javax.inject.Inject

@HiltViewModel
class NotificationsListViewModel
@Inject constructor(
    @ApplicationContext context: Context,
    repository: NotificationRepository,
    notificationRepository: NotificationRepository
) : BaseViewModelLiveData(context) {

    val scrollUpCommand = Command()
    val sendScrollStateCommand = Command()

    val uiState = Data<UiState<List<TwitchNotificationPresentation>>>()

    val toggleFabVisibilityCommand = Data<Boolean>()

    init {
        repository.getAllNotifications()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { uiState.setValue(UiState.Loading) }
            .map { twitchNotifications ->
                twitchNotifications.map {
                    TwitchNotificationPresentation.fromModel(
                        it,
                        getString(R.string.scr_any_date_time_pattern)
                    )
                }
            }
            .subscribe(
                {
                    uiState.setValue(
                        if (it.isEmpty()) UiState.Empty
                        else UiState.Loaded(it)
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
        if (scrollPosition != 0 || canScrollDown) toggleFabVisibilityCommand.setValue(true)
    }

    fun onFloatingActionButtonClicked() {
        toggleFabVisibilityCommand.setValue(false)
        scrollUpCommand.setValue(Unit)
    }

    fun onRecyclerViewScrollStateChanged(canScrollUp: Boolean) {
        if (!canScrollUp) toggleFabVisibilityCommand.setValue(false)
    }
}
