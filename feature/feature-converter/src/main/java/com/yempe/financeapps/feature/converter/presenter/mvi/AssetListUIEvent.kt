package com.yempe.financeapps.feature.converter.presenter.mvi

import androidx.annotation.StringRes

sealed interface AssetListUIEvent {

    data class ShowToast(val message: String) : AssetListUIEvent
    data class ShowToastWithResId(@StringRes val message: Int) : AssetListUIEvent
}