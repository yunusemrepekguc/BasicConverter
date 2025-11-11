package com.yempe.financeapps.feature.setting.domain.usecase

import com.yempe.financeapps.core.domain.repository.asset.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMaxDecimalDigitsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    operator fun invoke(): Flow<Int> {
        return settingsRepository.getMaxDecimalDigitsCount()
    }
}