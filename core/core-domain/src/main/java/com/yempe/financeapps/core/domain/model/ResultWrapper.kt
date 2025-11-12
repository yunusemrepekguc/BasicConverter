package com.yempe.financeapps.core.domain.model

sealed interface ResultWrapper<out T> {

    data class Success<T>(val data: T) : ResultWrapper<T>

    data class Error(
        val exception: Throwable,
        val message: String? = exception.message
    ) : ResultWrapper<Nothing>

    data object Loading : ResultWrapper<Nothing>
}