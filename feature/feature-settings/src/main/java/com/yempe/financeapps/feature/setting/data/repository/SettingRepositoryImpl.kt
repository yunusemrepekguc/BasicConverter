package com.yempe.financeapps.feature.setting.data.repository

import com.yempe.financeapps.core.data.datastore.DataStoreManager
import com.yempe.financeapps.core.domain.repository.asset.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : SettingsRepository {

    override fun getMaxDecimalDigitsCount(): Flow<Int> {
        return dataStoreManager.getMaxDecimalDigitsCount()
    }

    override suspend fun setMaxDecimalDigitCount(maxDigits: Int) {
        dataStoreManager.setMaxDecimalDigitCount(maxDigits)
    }
}