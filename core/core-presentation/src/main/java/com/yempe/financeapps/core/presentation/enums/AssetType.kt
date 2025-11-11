package com.yempe.financeapps.core.presentation.enums

import com.yempe.financeapps.core.presentation.R

enum class AssetType(
    val code: String,
    val iconRes: Int
) {

    TRY(code = "TRY", iconRes = R.drawable.ic_asset_turkish_lira),
    USD(code = "USD", iconRes = R.drawable.ic_asset_usa_dollar),
    EUR(code = "EUR", iconRes = R.drawable.ic_asset_euro),
    GBP(code = "GBP", iconRes = R.drawable.ic_asset_england_pound),
    BTC(code = "BTC", iconRes = R.drawable.ic_asset_bitcoin),
    ETH(code = "ETH", iconRes = R.drawable.ic_asset_ethereum),
    JPY(code = "JPY", iconRes = R.drawable.ic_asset_ethereum),
    AUD(code = "AUD", iconRes = R.drawable.ic_asset_ethereum),
    CAD(code = "CAD", iconRes = R.drawable.ic_asset_ethereum),
    CHF(code = "CHF", iconRes = R.drawable.ic_asset_ethereum),
    CNY(code = "CNY", iconRes = R.drawable.ic_asset_ethereum),
    NZD(code = "NZD", iconRes = R.drawable.ic_asset_ethereum),
    SEK(code = "SEK", iconRes = R.drawable.ic_asset_ethereum),
    NOK(code = "NOK", iconRes = R.drawable.ic_asset_ethereum),
    SGD(code = "SGD", iconRes = R.drawable.ic_asset_ethereum),
    KRW(code = "KRW", iconRes = R.drawable.ic_asset_ethereum);

    companion object {
        fun fromCode(code: String): AssetType {
            return AssetType.entries.find { it.code == code } ?: TRY
        }
    }
}
