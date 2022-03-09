package com.example.twitchapp.ui

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@SuppressLint("StaticFieldLeak")
abstract class BaseViewModel(private val applicationContext: Context) : ViewModel() {

    protected fun getString(@StringRes resId: Int, vararg formatArgs: Any): String =
        applicationContext.getString(resId, formatArgs)

    protected fun <T> mutableStateFlow(value: T): ViewModelStateFlow<T> =
        ViewModelStateFlowImpl(value)


    protected fun <T> ViewModelStateFlow<T>.setValue(value: T) = when (this) {
        is ViewModelStateFlowImpl -> wrapped.value = value
    }
}

sealed class ViewModelStateFlow<T>(stateFlow: StateFlow<T>) :
    StateFlow<T> by stateFlow

private class ViewModelStateFlowImpl<T>(
    initial: T,
    val wrapped: MutableStateFlow<T> = MutableStateFlow(initial)
) : ViewModelStateFlow<T>(wrapped)