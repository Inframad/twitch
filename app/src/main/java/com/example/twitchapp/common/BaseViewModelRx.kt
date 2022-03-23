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
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@SuppressLint("StaticFieldLeak")
abstract class BaseViewModelRx(private val applicationContext: Context) : ViewModel(),
    LifecycleEventObserver {

    protected var _currentLifecycleOwnerState = Lifecycle.Event.ON_ANY

    protected fun getString(@StringRes resId: Int, vararg formatArgs: Any): String =
        applicationContext.getString(resId, *formatArgs)

    protected fun handleBaseError(e: Throwable): String {
        Log.e("HandleBaseError", e.toString())
        return when (e) {
            is HttpException -> when (e.code()) {
                429 -> getString(R.string.scr_any_lbl_too_many_request)
                404 -> getString(R.string.scr_any_lbl_not_found)
                else -> e.localizedMessage
            }
            is IOException -> getString(R.string.scr_any_lbl_check_internet_connection)
            is SocketTimeoutException -> getString(R.string.scr_any_lbl_check_internet_connection)
            is UnknownHostException -> getString(R.string.scr_any_lbl_check_internet_connection)
            else -> e.localizedMessage
                ?: getString(R.string.scr_any_lbl_unknown_error)
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        _currentLifecycleOwnerState = event
    }
}


