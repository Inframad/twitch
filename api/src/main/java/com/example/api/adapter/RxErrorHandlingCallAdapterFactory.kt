package com.example.api.adapter

import com.example.twitchapp.model.exception.NetworkException
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import java.lang.reflect.Type
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class RxErrorHandlingCallAdapterFactory
@Inject constructor() : CallAdapter.Factory() {

    private val original: RxJava3CallAdapterFactory = RxJava3CallAdapterFactory.create()

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        return RxCallAdapterWrapper(
            original.get(returnType, annotations, retrofit)
                ?: return null
        )
    }

    inner class RxCallAdapterWrapper<R>(
        private val wrapped: CallAdapter<R, *>
    ) : CallAdapter<R, Any> {

        override fun responseType(): Type =
            wrapped.responseType()

        override fun adapt(call: Call<R>): Any {
            return when (val result = wrapped.adapt(call)) {
                is Observable<*> -> result.onErrorResumeNext {
                    Observable.error(mapToNetworkException(it))
                }
                is Single<*> -> result.onErrorResumeNext {
                    Single.error(mapToNetworkException(it))
                }
                is Completable -> result.onErrorResumeNext {
                    Completable.error(mapToNetworkException(it))
                }
                else -> result
            }
        }

        private fun mapToNetworkException(t: Throwable): Throwable {
            return when (t) {
                is retrofit2.HttpException -> when (t.code()) {
                    500 -> NetworkException.InternalServerError
                    404 -> NetworkException.NotFound
                    400 -> NetworkException.BadRequest
                    else -> NetworkException.HttpException(t.message(), t.code())
                }
                is UnknownHostException -> NetworkException.NetworkIsNotAvailable
                is SocketTimeoutException -> NetworkException.NetworkIsNotAvailable
                else -> t
            }
        }

    }

    companion object {
        fun create(): CallAdapter.Factory =
            RxErrorHandlingCallAdapterFactory()
    }
}