package com.yempe.financeapps.feature.converter.data.repository

import com.yempe.financeapps.core.database.dao.AssetDao
import com.yempe.financeapps.core.domain.model.AssetConvertedAmount
import com.yempe.financeapps.core.domain.model.AssetModel
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
import kotlinx.coroutines.flow.map
import timber.log.Timber

class AssetRepositoryImpl @Inject constructor(
    private val assetMapper: AssetMapper,
    private val assetDao: AssetDao,
    private val assetApi: AssetApi,
    private val mockConversionService: MockConversionService
) : AssetRepository {

    override suspend fun refreshAvailableAssets() {
        val remoteAssets = assetApi.getAssets()
            .map(assetMapper::dtoToEntityModel)

        val mergedAssets = remoteAssets.map { remote ->
            val local = assetDao.getAllAssets().first().find { it.code == remote.code }
            if (local != null) {
                remote.copy(isFavorite = local.isFavorite)
            } else {
                remote
            }
        }

        assetDao.insertAssets(mergedAssets)
    }


    override suspend fun updateAssetFavoriteState(assetCode: String): Boolean {
        val asset = assetDao.getAssetByCode(assetCode)
        assetDao.updateAssetFavoriteState(assetCode, isFavorite = asset.isFavorite.not())
        return asset.isFavorite.not()
    }

    override fun observeAvailableAssets(): Flow<List<AssetModel>> {
        return assetDao.getAllAssets()
            .map { entities ->
                entities
                    .map(transform = assetMapper::entityToDomainModel)
                    .sortedByDescending { it.isFavorite }
            }.catch { Timber.e(t = it) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun streamConvertedAmounts(
        baseCode: String,
        inputAmount: Double
    ): Flow<List<AssetConvertedAmount>> {
        return observeAvailableAssets()
            .flatMapLatest { assets ->
                mockConversionService.streamConvertedRates(
                    assets = assets,
                    baseCode = baseCode,
                    inputAmount = inputAmount
                )
            }.catch { Timber.e(t = it) }
    }
}