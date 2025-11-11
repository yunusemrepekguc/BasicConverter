package com.yempe.financeapps.core.common.extensions

import com.yempe.financeapps.core.common.result.ResultData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

suspend fun <T> safeCall(block: suspend () -> T): ResultData<T> {
    return try {
        ResultData.Success(block())
    } catch (e: Exception) {
        ResultData.Error(e)
    }
}

fun <T> Flow<T>.asResult(): Flow<ResultData<T>> {
    return this
        .map<T, ResultData<T>> { ResultData.Success(it) }
        .catch { emit(value = ResultData.Error(it)) }
}