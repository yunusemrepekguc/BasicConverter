package com.yempe.financeapps.feature.settings.api

import kotlinx.coroutines.flow.Flow

interface SettingsModuleApi {

    fun observeMaxDecimalDigits(): Flow<Int>
}