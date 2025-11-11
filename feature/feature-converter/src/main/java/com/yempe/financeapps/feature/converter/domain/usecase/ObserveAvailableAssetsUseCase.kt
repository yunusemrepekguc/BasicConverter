package com.yempe.financeapps.feature.converter.domain.usecase

import com.yempe.financeapps.core.domain.model.AssetModel
import com.yempe.financeapps.core.domain.repository.asset.AssetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveAvailableAssetsUseCase @Inject constructor(
    private val assetRepository: AssetRepository
) {

    operator fun invoke(): Flow<List<AssetModel>> {
        return assetRepository.observeAvailableAssets()
    }
}