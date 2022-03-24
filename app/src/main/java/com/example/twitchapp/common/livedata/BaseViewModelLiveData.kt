package com.example.twitchapp.common.livedata

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.twitchapp.R
import com.example.twitchapp.model.SnackbarData
import com.example.twitchapp.ui.game.GameFragmentArgs
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@SuppressLint("StaticFieldLeak")
abstract class BaseViewModelLiveData(private val applicationContext: Context) : ViewModel() {

    protected val compositeDisposable = CompositeDisposable()
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

}


