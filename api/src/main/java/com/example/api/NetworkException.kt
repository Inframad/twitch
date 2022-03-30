package com.example.api

sealed class NetworkException : Exception() {
    object NetworkIsNotAvailable : NetworkException()
    object BadRequest : NetworkException()
    object InternalServerError : NetworkException()
    object NotFound : NetworkException()

    class HttpException(
        override val message: String,
        val code: Int
    ) : NetworkException()
}