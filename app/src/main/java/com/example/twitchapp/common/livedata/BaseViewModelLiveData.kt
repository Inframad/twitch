package com.example.twitchapp.common.livedata

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.example.twitchapp.R
import com.example.twitchapp.model.SnackbarData
import com.example.twitchapp.ui.game.GameFragmentArgs
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@SuppressLint("StaticFieldLeak")
abstract class BaseViewModelLiveData(private val applicationContext: Context)
    : ViewModel(), LifecycleEventObserver {

    protected var _currentLifecycleOwnerState = Lifecycle.Event.ON_ANY
    val showToastCommand = TCommand<String>()
    val showSnackbarCommand = TCommand<SnackbarData>()
    val navigateToGameScreenCommand = TCommand<GameFragmentArgs>()

    protected fun getString(@StringRes resId: Int, vararg formatArgs: Any): String =
        applicationContext.getString(resId, *formatArgs)

    protected fun Command(): LiveData<Unit> =
        SingleLiveEvent()

    protected fun <T> TCommand(): LiveData<T> =
        SingleLiveEvent()

    protected fun showToast(message: String) = showToastCommand.setValue(message)

    protected fun <T> mutableLiveData(): LiveData<T> =
        MutableLiveData()

    protected fun <T> LiveData<T>.setValue(newValue: T) = when(this) {
        is MutableLiveData -> value = newValue
        else -> throw NoSuchMethodException("This method only for MutableLiveData casted to LiveData")
    }

    protected fun handleBaseError(e: Throwable): String {
        Log.e("HandleBaseError", e.toString())
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


