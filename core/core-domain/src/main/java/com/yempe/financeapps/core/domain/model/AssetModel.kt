package com.yempe.financeapps.core.domain.model

data class AssetModel(
    val code: String,
    val name: String,
    val symbol: String,
    val isFavorite: Boolean
)
