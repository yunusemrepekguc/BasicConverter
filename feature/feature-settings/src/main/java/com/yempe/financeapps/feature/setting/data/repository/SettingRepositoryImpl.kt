package com.yempe.financeapps.feature.setting.data.repository

import com.yempe.financeapps.core.data.datastore.DataStoreManager
import com.yempe.financeapps.core.domain.model.ResultWrapper
import com.yempe.financeapps.core.domain.repository.asset.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : SettingsRepository {

    override fun getMaxDecimalDigitsCount(): Flow<ResultWrapper<Int>> {
        return dataStoreManager.getMaxDecimalDigitsCount()
            .map<Int, ResultWrapper<Int>> { maxDigits ->
                ResultWrapper.Success(maxDigits)
            }
            .onStart {
                emit(ResultWrapper.Loading)
            }
            .catch { e ->
                emit(ResultWrapper.Error(exception = e, message = e.message))
            }
    }

    override suspend fun setMaxDecimalDigitCount(maxDigits: Int): ResultWrapper<Unit> {
        return try {
            dataStoreManager.setMaxDecimalDigitCount(maxDigits)
            ResultWrapper.Success(Unit)
        } catch (e: Exception) {
            ResultWrapper.Error(exception = e, message = e.message)
        }
    }
}