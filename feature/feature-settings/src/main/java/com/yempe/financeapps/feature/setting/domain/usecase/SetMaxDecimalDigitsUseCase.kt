package com.yempe.financeapps.feature.setting.domain.usecase

import com.yempe.financeapps.core.domain.repository.asset.SettingsRepository
import javax.inject.Inject

class SetMaxDecimalDigitsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {

    suspend operator fun invoke(maxDigits: Int) {
        settingsRepository.setMaxDecimalDigitCount(maxDigits)
    }
}