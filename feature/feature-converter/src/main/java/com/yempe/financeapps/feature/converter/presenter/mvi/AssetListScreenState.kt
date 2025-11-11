package com.yempe.financeapps.feature.converter.presenter.mvi

import com.yempe.financeapps.core.domain.model.AssetConvertedAmount
import com.yempe.financeapps.core.domain.model.AssetModel

data class AssetListScreenState(
    val isLoading: Boolean = false,
    val baseCurrency: String? = null,
    val convertAmount: Double? = null,
    val maxDecimalDigit: Int = 2,
    val assetList: List<AssetModel> = emptyList(),
    val convertedAmounts: List<AssetConvertedAmount> = emptyList()
)