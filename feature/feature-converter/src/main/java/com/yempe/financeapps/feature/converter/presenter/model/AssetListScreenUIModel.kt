package com.yempe.financeapps.feature.converter.presenter.model

import androidx.annotation.DrawableRes

data class AssetListUIModel(
    val isLoading: Boolean,
    val maxDecimalDigit: Int,
    val listItems: List<AssetListUIItemModel> = emptyList()
)

data class AssetListUIItemModel(
    val assetShortName: String,
    val assetSymbol: String,
    val assetLongName: String,
    val convertedAmount: String,
    val baseRate: String,
    val isFavorite: Boolean,
    val isBaseCurrency: Boolean,
    @DrawableRes val assetImage: Int
)
