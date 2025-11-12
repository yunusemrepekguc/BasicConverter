package com.yempe.financeapps.core.domain.repository.asset

import com.yempe.financeapps.core.domain.model.AssetConvertedAmount
import com.yempe.financeapps.core.domain.model.AssetModel
import com.yempe.financeapps.core.domain.model.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface AssetRepository {

    suspend fun refreshAvailableAssets(): ResultWrapper<Unit>

    suspend fun updateAssetFavoriteState(assetCode: String): ResultWrapper<Boolean>

    fun observeAvailableAssets(): Flow<ResultWrapper<List<AssetModel>>>

    fun streamConvertedAmounts(
        baseCode: String,
        inputAmount: Double
    ): Flow<ResultWrapper<List<AssetConvertedAmount>>>
}