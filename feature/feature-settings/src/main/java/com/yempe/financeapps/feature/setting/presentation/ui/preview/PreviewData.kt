package com.yempe.financeapps.feature.setting.presentation.ui.preview

import com.yempe.financeapps.feature.setting.presentation.model.SettingsScreenUIModel

object PreviewData {

    fun getSettingsScreenUIModel(): SettingsScreenUIModel {
        return SettingsScreenUIModel(
            currentDecimalCount = 2
        )
    }
}