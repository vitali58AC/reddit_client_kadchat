package com.kadun.kadchat.data.utils

import android.util.Log

sealed class AppResult<out T : Any> {

    object Empty : AppResult<Nothing>()
    data class Failure(val throwable: Throwable) : AppResult<Nothing>()
    data class Success<out T : Any>(val data: T) : AppResult<T>()

    fun isSuccess(): Boolean = this is Success
    fun isFailure(): Boolean = this is Failure
}

suspend fun <T : Any> suspendCallForAppResult(
    call: suspend () -> T?
): AppResult<T> = try {
    call()?.let {
        AppResult.Success(it)
    } ?: AppResult.Empty
} catch (e: Throwable) {
    Log.e("tag", "$e")
    AppResult.Failure(e)
}

inline fun <T : Any> AppResult<T>.onSuccess(block: (T) -> Unit): AppResult<T> {
    if (this is AppResult.Success) block(this.data)
    return this
}

inline fun <T : Any> AppResult<T>.onFailure(block: (Throwable) -> Unit): AppResult<T> {
    if (this is AppResult.Failure) block(this.throwable)
    return this
}

suspend fun <T : Any, V : Any> AppResult<T>.suspendTransform(
    throwableTransformation: (Throwable) -> V,
    transformation: suspend (T) -> V,
): V {
    return when (this) {
        is AppResult.Success -> transformation(this.data)
        is AppResult.Failure -> throwableTransformation(throwable)
        else -> throwableTransformation(EmptyDataAppResultException())
    }
}

class EmptyDataAppResultException : Exception() {

    override val message: String
        get() = "AppResult has no data"

    override fun getLocalizedMessage(): String = message
}