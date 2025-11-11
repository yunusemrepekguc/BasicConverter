package com.yempe.financeapps.feature.setting.di.impl

import com.yempe.financeapps.feature.setting.domain.usecase.GetMaxDecimalDigitsUseCase
import com.yempe.financeapps.feature.settings.api.SettingsModuleApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsModuleApiImpl @Inject constructor(
    private val observeMaxDecimalDigitsUseCase: GetMaxDecimalDigitsUseCase
) : SettingsModuleApi {

    override fun observeMaxDecimalDigits(): Flow<Int> = observeMaxDecimalDigitsUseCase()
}