package com.example.twitchapp.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.example.twitchapp.ui.util.SingleEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@SuppressLint("StaticFieldLeak")
abstract class BaseViewModel(private val applicationContext: Context) : ViewModel() {

    protected fun Command(): ViewModelStateFlowSingleEvent<Unit?> = ViewModelStateFlowSingleEventImpl(null)
    protected fun <T> TCommand(value: T? = null): ViewModelStateFlowSingleEvent<T?> =
        ViewModelStateFlowSingleEventImpl(value)

    protected fun getString(@StringRes resId: Int, vararg formatArgs: Any): String =
        applicationContext.getString(resId, formatArgs)

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