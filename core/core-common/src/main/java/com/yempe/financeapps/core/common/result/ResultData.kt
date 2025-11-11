package com.yempe.financeapps.core.common.result

sealed class ResultData<out T> {

    data class Success<T>(val data: T) : ResultData<T>()

    data class Error(val exception: Throwable) : ResultData<Nothing>()

    data object Loading : ResultData<Nothing>()
}