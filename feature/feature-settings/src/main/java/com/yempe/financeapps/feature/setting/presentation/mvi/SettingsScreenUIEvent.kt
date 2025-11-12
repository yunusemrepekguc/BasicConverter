package com.yempe.financeapps.feature.setting.presentation.mvi

sealed interface SettingsScreenUIEvent {

    data class ShowToast(val message: String) : SettingsScreenUIEvent

}