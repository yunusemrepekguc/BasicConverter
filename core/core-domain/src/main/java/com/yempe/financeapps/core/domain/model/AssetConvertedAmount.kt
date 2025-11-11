package com.yempe.financeapps.core.domain.model

data class AssetConvertedAmount(
    val assetCode: String,
    val targetAssetCode: String,
    val baseRate: Double,
    val convertedAmount: Double,
)