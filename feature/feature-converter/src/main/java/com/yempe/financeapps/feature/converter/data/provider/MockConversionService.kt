package com.yempe.financeapps.feature.converter.data.provider

import com.yempe.financeapps.core.domain.model.AssetConvertedAmount
import com.yempe.financeapps.core.domain.model.AssetModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import javax.inject.Inject

class MockConversionService @Inject constructor() {

    fun streamConvertedRates(
        assets: List<AssetModel>,
        baseCode: String,
        inputAmount: Double
    ): Flow<List<AssetConvertedAmount>> = flow {
        while (currentCoroutineContext().isActive) {
            val convertedList = assets.map { asset ->
                val value = if (asset.code == baseCode) {
                    inputAmount to 1.0
                } else {
                    val fluctuation = 1 + randomBetween(-0.005, 0.005) // +-0.5%
                    val rate = randomBetween(0.8, 1.2) * fluctuation
                    (inputAmount * rate) to rate
                }
                AssetConvertedAmount(
                    assetCode = baseCode,
                    targetAssetCode = asset.code,
                    convertedAmount = value.first,
                    baseRate = value.second,
                )
            }

            emit(convertedList)
            delay(5000L)
        }
    }.flowOn(Dispatchers.Default)

    private fun randomBetween(min: Double, max: Double): Double {
        return min + kotlin.random.Random.nextDouble() * (max - min)
    }
}
