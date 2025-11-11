package com.yempe.financeapps.feature.converter.presenter.mvi

interface AssetListUIEvent {

    data class ShowToast(val message: String) : AssetListUIEvent
}