package com.yempe.financeapps.core.domain.repository.asset

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    fun getMaxDecimalDigitsCount(): Flow<Int>

    suspend fun setMaxDecimalDigitCount(maxDigits: Int)
}