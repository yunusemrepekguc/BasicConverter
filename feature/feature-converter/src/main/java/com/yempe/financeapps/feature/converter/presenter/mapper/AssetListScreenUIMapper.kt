package com.yempe.financeapps.feature.converter.presenter.mapper

import androidx.annotation.DrawableRes
import com.yempe.financeapps.core.domain.model.AssetConvertedAmount
import com.yempe.financeapps.core.domain.model.AssetModel
import com.yempe.financeapps.core.presentation.enums.AssetType
import com.yempe.financeapps.feature.converter.presenter.model.AssetListUIItemModel
import com.yempe.financeapps.feature.converter.presenter.model.AssetListUIModel
import com.yempe.financeapps.feature.converter.presenter.mvi.AssetListScreenState
import java.util.Locale
import javax.inject.Inject

class AssetListScreenUIMapper @Inject constructor() {

    fun mapToUiModel(state: AssetListScreenState): AssetListUIModel = with(state) {
        val items = state.assetList.map { asset ->
            val assetConvertedAmount =
                convertedAmounts.firstOrNull { it.targetAssetCode == asset.code }
            AssetListUIItemModel(
                assetShortName = asset.getShortName(),
                assetSymbol = asset.getSymbol(),
                assetLongName = asset.getLongName(),
                baseRate = assetConvertedAmount?.getBaseRate(
                    maxDecimalDigits = state.maxDecimalDigit
                ) ?: "",
                convertedAmount = assetConvertedAmount?.getConvertedAmountText(
                    maxDecimalDigits = state.maxDecimalDigit
                ) ?: "",
                assetImage = asset.getAssetImage(),
                isFavorite = asset.isFavorite,
                isBaseCurrency = asset.code == state.baseCurrency
            )
        }

        return AssetListUIModel(
            isLoading = state.isLoading,
            listItems = items,
            maxDecimalDigit = state.maxDecimalDigit
        )
    }

    private fun AssetModel.getShortName() = code

    private fun AssetModel.getSymbol() = symbol

    private fun AssetModel.getLongName() = name

    private fun AssetConvertedAmount.getBaseRate(
        maxDecimalDigits: Int
    ): String {
        val formattedAmount = "%.${maxDecimalDigits}f".format(Locale.getDefault(), baseRate)
        return "1 $assetCode ≈ $formattedAmount $targetAssetCode"
    }

    @DrawableRes
    private fun AssetModel.getAssetImage(): Int {
        return AssetType.fromCode(code).iconRes
    }

    private fun AssetConvertedAmount.getConvertedAmountText(
        maxDecimalDigits: Int
    ): String {
        return "%.${maxDecimalDigits}f".format(Locale.getDefault(), convertedAmount)
    }

}