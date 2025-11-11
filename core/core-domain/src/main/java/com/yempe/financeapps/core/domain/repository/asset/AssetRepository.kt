package com.yempe.financeapps.core.domain.repository.asset

import com.yempe.financeapps.core.domain.model.AssetConvertedAmount
import com.yempe.financeapps.core.domain.model.AssetModel
import kotlinx.coroutines.flow.Flow

interface AssetRepository {

    suspend fun refreshAvailableAssets()

    suspend fun updateAssetFavoriteState(assetCode: String)

    fun observeAvailableAssets(): Flow<List<AssetModel>>

    fun streamConvertedAmounts(baseCode: String, inputAmount: Double): Flow<List<AssetConvertedAmount>>
}