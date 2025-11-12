package com.yempe.financeapps.feature.settings.api

import com.yempe.financeapps.core.domain.model.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface SettingsModuleApi {

    fun observeMaxDecimalDigits(): Flow<ResultWrapper<Int>>
}