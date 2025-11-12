package com.yempe.financeapps.feature.converter.domain.usecase

import com.yempe.financeapps.core.domain.model.AssetConvertedAmount
import com.yempe.financeapps.core.domain.model.ResultWrapper
import com.yempe.financeapps.core.domain.repository.asset.AssetRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveConvertedRatesUseCase @Inject constructor(
    private val assetRepository: AssetRepository
) {

    data class Param(
        val baseCode: String,
        val inputAmount: Double
    )

    operator fun invoke(param: Param): Flow<ResultWrapper<List<AssetConvertedAmount>>> {
        return assetRepository.streamConvertedAmounts(
            baseCode = param.baseCode,
            inputAmount = param.inputAmount
        )
    }
}