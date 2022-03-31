package com.example.common.livedata

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.common.R
import com.example.common.SnackbarData
import com.example.twitchapp.model.exception.NetworkException
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable

@Suppress("FunctionName")
@SuppressLint("StaticFieldLeak")
abstract class BaseViewModelLiveData(private val applicationContext: Context) : ViewModel() {

    protected val compositeDisposable = CompositeDisposable()
    val showToastCommand = TCommand<String>()
    val showSnackbarCommand = TCommand<SnackbarData>()

    protected fun getString(@StringRes resId: Int, vararg formatArgs: Any): String =
        applicationContext.getString(resId, *formatArgs)

    protected fun Command(): LiveData<Unit> =
        SingleLiveEvent()

    protected fun <T> TCommand(): LiveData<T> =
        SingleLiveEvent()

    protected fun showToast(message: String) = showToastCommand.setValue(message)

    protected fun <T> Data(): LiveData<T> =
        MutableLiveData()

    protected fun <T> LiveData<T>.setValue(newValue: T) = when (this) {
        is MutableLiveData -> value = newValue
        else -> throw NoSuchMethodException("This method only for MutableLiveData casted to LiveData")
    }

    protected fun Disposable.addToCompositeDisposable() {
        compositeDisposable.add(this)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    protected fun handleBaseError(e: Throwable): String {
        Log.e("HandleBaseError", e.toString())
        return when (e) {
            NetworkException.NetworkIsNotAvailable ->
                getString(R.string.scr_any_lbl_check_internet_connection)
            NetworkException.InternalServerError ->
                getString(R.string.scr_any_lbl_server_is_not_available)
            else -> getString(R.string.scr_any_lbl_unknown_error)
        }
    }

}


