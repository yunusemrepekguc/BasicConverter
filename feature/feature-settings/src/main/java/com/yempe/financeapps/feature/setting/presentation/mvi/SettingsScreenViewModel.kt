package com.yempe.financeapps.feature.setting.presentation.mvi

import androidx.lifecycle.viewModelScope
import com.yempe.financeapps.core.domain.util.onError
import com.yempe.financeapps.core.domain.util.onSuccess
import com.yempe.financeapps.core.presentation.vm.BaseViewModel
import com.yempe.financeapps.feature.setting.domain.usecase.GetMaxDecimalDigitsUseCase
import com.yempe.financeapps.feature.setting.domain.usecase.SetMaxDecimalDigitsUseCase
import com.yempe.financeapps.feature.setting.presentation.intent.SettingScreenUIIntent
import com.yempe.financeapps.feature.setting.presentation.mapper.SettingsScreenUIMapper
import com.yempe.financeapps.feature.setting.presentation.model.SettingsScreenUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SettingsScreenViewModel @Inject constructor(
    private val uiMapper: SettingsScreenUIMapper,
    private val setMaxDecimalDigitsUseCase: SetMaxDecimalDigitsUseCase,
    private val getMaxDecimalDigitsUseCase: GetMaxDecimalDigitsUseCase
) : BaseViewModel<SettingsScreenState, Unit, SettingsScreenUIEvent>(
    initialState = SettingsScreenState()
) {

    val uiState: StateFlow<SettingsScreenUIModel> = state
        .map { uiMapper.stateToUIModel(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = uiMapper.stateToUIModel(initialState)
        )

    init {
        observeMaxDigitsCount()
    }

    fun onIntent(intent: SettingScreenUIIntent) {
        launchAll {
            when (intent) {
                SettingScreenUIIntent.OnMaxDigitIncrease -> {
                    val currentMaxDigit = state.value.maxDigitCount
                    if (currentMaxDigit + 1 <= 6) {
                        setMaxDecimalDigitsUseCase.invoke(currentMaxDigit + 1).onError { ex, msg ->
                            postEvent(SettingsScreenUIEvent.ShowToast(message = msg.toString()))
                        }
                    }
                }

                SettingScreenUIIntent.OnMaxDigitDecrease -> {
                    val currentMaxDigit = state.value.maxDigitCount
                    if (currentMaxDigit - 1 >= 0) {
                        setMaxDecimalDigitsUseCase.invoke(currentMaxDigit - 1).onError { ex, msg ->
                            postEvent(SettingsScreenUIEvent.ShowToast(message = msg.toString()))
                        }
                    }
                }
            }
        }
    }

    private fun observeMaxDigitsCount() {
        launchAll {
            getMaxDecimalDigitsUseCase()
                .catch { Timber.e(it) }
                .collect { maxDigit ->
                    maxDigit.onSuccess { digit ->
                        updateState { prev ->
                            prev.copy(
                                maxDigitCount = digit
                            )
                        }
                    }
                }
        }
    }
}