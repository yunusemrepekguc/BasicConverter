package com.yempe.financeapps.feature.setting.presentation.intent

sealed interface SettingScreenUIIntent {

    data object OnMaxDigitIncrease : SettingScreenUIIntent

    data object OnMaxDigitDecrease : SettingScreenUIIntent
}