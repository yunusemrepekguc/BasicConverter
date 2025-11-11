package com.yempe.financeapps.feature.converter.presenter.mvi

sealed interface AssetListNavigationEvent {

    data object NavigateToConverterBottomSheet : AssetListNavigationEvent
}