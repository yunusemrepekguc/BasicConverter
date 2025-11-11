package com.yempe.financeapps.feature.converter.domain.usecase

import com.yempe.financeapps.core.domain.repository.asset.AssetRepository
import javax.inject.Inject

class UpdateAssetFavoriteStateUseCase @Inject constructor(
    private val assetRepository: AssetRepository
) {

    suspend operator fun invoke(assetCode: String) {
        assetRepository.updateAssetFavoriteState(assetCode)
    }
}