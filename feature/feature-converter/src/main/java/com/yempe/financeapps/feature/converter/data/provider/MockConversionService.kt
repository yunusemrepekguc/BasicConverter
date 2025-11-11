package com.yempe.financeapps.feature.converter.data.provider

import com.yempe.financeapps.core.common.constant.ConverterConstants
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
import kotlin.random.Random

class MockConversionService @Inject constructor() {

    fun streamConvertedRates(
        assets: List<AssetModel>,
        baseCode: String,
        inputAmount: Double
    ): Flow<List<AssetConvertedAmount>> = flow {
        require(assets.isNotEmpty()) { "Assets list empty" }
        require(assets.any { it.code == baseCode }) { "Base code '$baseCode' not found" }
        require(inputAmount >= 0) { "Input amount negative" }

        val lastRates = assets.associate { asset ->
            asset.code to if (asset.code == baseCode) 1.0 else randomBetween(1.0, 40.0)
        }.toMutableMap()

        while (currentCoroutineContext().isActive) {
            val convertedList = assets.map { asset ->
                val rate = if (asset.code == baseCode) {
                    1.0
                } else {
                    val prevRate = lastRates[asset.code] ?: 1.0
                    val fluctuation = prevRate * randomBetween(-0.05, 0.05)
                    val newRate = (prevRate + fluctuation).coerceIn(1.0, 40.0)
                    lastRates[asset.code] = newRate
                    newRate
                }

                AssetConvertedAmount(
                    assetCode = baseCode,
                    targetAssetCode = asset.code,
                    convertedAmount = inputAmount * rate,
                    baseRate = rate,
                )
            }

            emit(convertedList)
            delay(ConverterConstants.MOCK_SERVICE_DELAY_TIME.toLong())
        }
    }.flowOn(Dispatchers.Default)

    private fun randomBetween(min: Double, max: Double): Double {
        return min + Random.nextDouble() * (max - min)
    }
}
