package com.yempe.financeapps.core.domain.repository.asset

import com.yempe.financeapps.core.domain.model.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getMaxDecimalDigitsCount(): Flow<ResultWrapper<Int>>

    suspend fun setMaxDecimalDigitCount(maxDigits: Int): ResultWrapper<Unit>
}