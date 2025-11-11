package com.yempe.financeapps.feature.setting.presentation.mapper

import com.yempe.financeapps.feature.setting.presentation.model.SettingsScreenUIModel
import com.yempe.financeapps.feature.setting.presentation.mvi.SettingsScreenState
import javax.inject.Inject

class SettingsScreenUIMapper @Inject constructor() {

    fun stateToUIModel(state: SettingsScreenState): SettingsScreenUIModel {
        return SettingsScreenUIModel(
            currentDecimalCount = state.maxDigitCount
        )
    }
}