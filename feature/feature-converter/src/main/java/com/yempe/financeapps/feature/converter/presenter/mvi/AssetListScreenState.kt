package com.yempe.financeapps.feature.converter.presenter.mvi

import com.yempe.financeapps.core.common.constant.ConverterConstants
import com.yempe.financeapps.core.domain.model.AssetConvertedAmount
import com.yempe.financeapps.core.domain.model.AssetModel

data class AssetListScreenState(
    val isLoading: Boolean = false,
    val baseCurrency: String? = null,
    val convertAmount: Double? = null,
    val maxDecimalDigit: Int = ConverterConstants.DEFAULT_MAX_DECIMAL,
    val assetList: List<AssetModel> = emptyList(),
    val convertedAmounts: List<AssetConvertedAmount> = emptyList()
)