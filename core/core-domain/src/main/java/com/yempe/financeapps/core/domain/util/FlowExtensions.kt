package com.yempe.financeapps.core.domain.util

import com.yempe.financeapps.core.domain.model.ResultWrapper

inline fun <T> ResultWrapper<T>.onSuccess(action: (T) -> Unit): ResultWrapper<T> {
    if (this is ResultWrapper.Success) action(data)
    return this
}

inline fun <T> ResultWrapper<T>.onError(action: (Throwable, String?) -> Unit): ResultWrapper<T> {
    if (this is ResultWrapper.Error) action(exception, message)
    return this
}

inline fun <T> ResultWrapper<T>.onLoading(action: () -> Unit): ResultWrapper<T> {
    if (this is ResultWrapper.Loading) action()
    return this
}