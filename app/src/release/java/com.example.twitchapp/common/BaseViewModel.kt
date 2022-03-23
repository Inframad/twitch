package com.example.twitchapp.common

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.twitchapp.R
import com.example.twitchapp.model.SnackbarData
import com.example.twitchapp.ui.game.GameFragmentArgs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@SuppressLint("StaticFieldLeak")
abstract class BaseViewModel(private val applicationContext: Context)
    : ViewModel(), LifecycleEventObserver {

    protected var _currentLifecycleOwnerState = Lifecycle.Event.ON_ANY
    val showToastCommand = TCommand<String?>()
    val showSnackbarCommand = TCommand<SnackbarData?>()
    val navigateToGameScreenCommand = TCommand<GameFragmentArgs?>()

    protected fun getString(@StringRes resId: Int, vararg formatArgs: Any): String =
        applicationContext.getString(resId, *formatArgs)

    protected fun Command(): ViewModelStateFlowSingleEvent<Unit?> =
        ViewModelStateFlowSingleEventImpl(null)

    protected fun <T> TCommand(value: T? = null): ViewModelStateFlowSingleEvent<T?> =
        ViewModelStateFlowSingleEventImpl(value)

    protected fun showToast(message: String) = showToastCommand.setValue(message)

    protected fun <T> mutableStateFlowSingleEvent(value: T): ViewModelStateFlowSingleEvent<T> =
        ViewModelStateFlowSingleEventImpl(value)

    protected fun <T> ViewModelStateFlowSingleEvent<T>.setValue(value: T) = when (this) {
        is ViewModelStateFlowSingleEventImpl -> wrapped.value = SingleEvent(value)
    }

    protected fun <T> mutableStateFlow(value: T): ViewModelStateFlow<T> =
        ViewModelStateFlowImpl(value)

    protected fun <T> ViewModelStateFlow<T>.setValue(value: T) = when (this) {
        is ViewModelStateFlowImpl -> wrapped.value = value
    }

    protected fun handleBaseError(e: Throwable): String {
        return when (e) {
            is HttpException -> when (e.code()) {
                429 -> getString(R.string.scr_any_lbl_too_many_request)
                404 -> getString(R.string.scr_any_lbl_not_found)
                else -> getString(R.string.scr_any_lbl_unknown_error)
            }
            is IOException -> getString(R.string.scr_any_lbl_check_internet_connection)
            is SocketTimeoutException -> getString(R.string.scr_any_lbl_check_internet_connection)
            is UnknownHostException -> getString(R.string.scr_any_lbl_check_internet_connection)
            else -> getString(R.string.scr_any_lbl_unknown_error)
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        _currentLifecycleOwnerState = event
    }
}

sealed class ViewModelStateFlowSingleEvent<T>(stateFlow: StateFlow<SingleEvent<T>>) :
    StateFlow<SingleEvent<T>> by stateFlow

private class ViewModelStateFlowSingleEventImpl<T>(
    initial: T,
    val wrapped: MutableStateFlow<SingleEvent<T>> = MutableStateFlow(SingleEvent(initial))
) : ViewModelStateFlowSingleEvent<T>(wrapped)

sealed class ViewModelStateFlow<T>(stateFlow: StateFlow<T>) :
    StateFlow<T> by stateFlow

private class ViewModelStateFlowImpl<T>(
    initial: T,
    val wrapped: MutableStateFlow<T> = MutableStateFlow(initial)
) : ViewModelStateFlow<T>(wrapped)