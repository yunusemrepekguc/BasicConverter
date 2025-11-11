package com.yempe.financeapps.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreManager @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        private val MAX_DECIMAL_DIGIT = intPreferencesKey("maxDigits")
    }

    suspend fun setMaxDecimalDigitCount(digit: Int) {
        dataStore.edit { pref ->
            pref[MAX_DECIMAL_DIGIT] = digit
        }
    }

    fun getMaxDecimalDigitsCount(): Flow<Int> {
        return dataStore.data
            .map { prefs -> prefs[MAX_DECIMAL_DIGIT] ?: 2 }
    }

}