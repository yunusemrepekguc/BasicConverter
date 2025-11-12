package com.yempe.financeapps.feature.converter.presenter.ui.preview

import com.yempe.financeapps.core.presentation.R
import com.yempe.financeapps.feature.converter.presenter.model.AssetListUIItemModel
import com.yempe.financeapps.feature.converter.presenter.model.AssetListUIModel

object PreviewData {

    fun getAssetListUIModel(): AssetListUIModel {
        return AssetListUIModel(
            isRefreshing = false,
            maxDecimalDigit = 2,
            listItems = getAssetListUIItemModel()
        )
    }

    fun getAssetListUIItemModel(): List<AssetListUIItemModel> {
        return listOf(
            AssetListUIItemModel(
                assetShortName = "testShortName",
                assetSymbol = "testSymbol",
                assetLongName = "testLongNAme",
                convertedAmount = "2910.1923",
                baseRate = "1881",
                isFavorite = true,
                isBaseCurrency = true,
                assetImage = R.drawable.ic_asset_turkish_lira
            ),
            AssetListUIItemModel(
                assetShortName = "testShortName1",
                assetSymbol = "testSymbol1",
                assetLongName = "testLongNAme1",
                convertedAmount = "2",
                baseRate = "2",
                isFavorite = false,
                isBaseCurrency = false,
                assetImage = R.drawable.ic_asset_japan
            )
        )
    }
}