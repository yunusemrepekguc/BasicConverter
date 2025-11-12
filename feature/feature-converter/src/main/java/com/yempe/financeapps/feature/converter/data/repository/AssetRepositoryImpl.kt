package com.yempe.financeapps.feature.converter.data.repository

import com.yempe.financeapps.core.database.dao.AssetDao
import com.yempe.financeapps.core.domain.model.AssetConvertedAmount
import com.yempe.financeapps.core.domain.model.AssetModel
import com.yempe.financeapps.core.domain.model.ResultWrapper
import com.yempe.financeapps.core.domain.repository.asset.AssetRepository
import com.yempe.financeapps.feature.converter.data.mapper.AssetMapper
import com.yempe.financeapps.feature.converter.data.provider.MockConversionService
import com.yempe.financeapps.feature.converter.data.remote.AssetApi
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class AssetRepositoryImpl @Inject constructor(
    private val assetMapper: AssetMapper,
    private val assetDao: AssetDao,
    private val assetApi: AssetApi,
    private val mockConversionService: MockConversionService
) : AssetRepository {

    override suspend fun refreshAvailableAssets(): ResultWrapper<Unit> {
        return try {
            val entityAssets = assetApi.getAssets()
                .map(assetMapper::dtoToEntityModel)

            val mergedAssets = entityAssets.map { remote ->
                val local = assetDao.getAllAssets().first().find { it.code == remote.code }
                if (local != null) {
                    remote.copy(isFavorite = local.isFavorite)
                } else {
                    remote
                }
            }

            assetDao.insertAssets(mergedAssets)
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            ResultWrapper.Error(exception = e, message = e.message)
        }
    }

    override suspend fun updateAssetFavoriteState(assetCode: String): ResultWrapper<Boolean> {
        return try {
            val asset = assetDao.getAssetByCode(assetCode)
            val newFavoriteState = asset.isFavorite.not()
            assetDao.updateAssetFavoriteState(assetCode, isFavorite = newFavoriteState)
            ResultWrapper.Success(newFavoriteState)
        } catch (e: Exception) {
            ResultWrapper.Error(exception = e, message = e.message)
        }
    }

    override fun observeAvailableAssets(): Flow<ResultWrapper<List<AssetModel>>> {
        return flow {
            emit(value = ResultWrapper.Loading)
            assetDao.getAllAssets()
                .map { entities ->
                    entities
                        .map(transform = assetMapper::entityToDomainModel)
                        .sortedByDescending { it.isFavorite }
                }
                .collect { assets ->
                    emit(ResultWrapper.Success(assets))
                }
        }.catch { e ->
            emit(ResultWrapper.Error(exception = e, message = e.message))
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun streamConvertedAmounts(
        baseCode: String,
        inputAmount: Double
    ): Flow<ResultWrapper<List<AssetConvertedAmount>>> {
        return observeAvailableAssets()
            .flatMapLatest { assetsResult ->
                when (assetsResult) {
                    is ResultWrapper.Loading -> {
                        flow {
                            emit(ResultWrapper.Loading)
                        }
                    }

                    is ResultWrapper.Success -> {
                        mockConversionService.streamConvertedRates(
                            assets = assetsResult.data,
                            baseCode = baseCode,
                            inputAmount = inputAmount
                        )
                            .map<List<AssetConvertedAmount>, ResultWrapper<List<AssetConvertedAmount>>> { convertedAmounts ->
                                ResultWrapper.Success(data = convertedAmounts)
                            }
                            .catch { e ->
                                emit(ResultWrapper.Error(exception = e, message = e.message))
                            }
                    }

                    is ResultWrapper.Error -> {
                        flow {
                            emit(
                                ResultWrapper.Error(
                                    exception = assetsResult.exception,
                                    message = assetsResult.message
                                )
                            )
                        }
                    }
                }
            }.onStart {
                emit(ResultWrapper.Loading)
            }.catch { ex ->
                emit(ResultWrapper.Error(exception = ex, message = ex.message))
            }
    }
}