package com.yempe.financeapps.feature.converter.presenter.intent

sealed class AssetListScreenUIIntent {

    data class OnAssetFavorite(val assetCode: String) : AssetListScreenUIIntent()

    data class AssetAmountChanged(val assetCode: String, val amount: Double): AssetListScreenUIIntent()
}