package com.yempe.financeapps.feature.converter.presenter.mvi

import androidx.lifecycle.viewModelScope
import com.yempe.financeapps.core.domain.model.ResultWrapper
import com.yempe.financeapps.core.domain.util.onError
import com.yempe.financeapps.core.domain.util.onLoading
import com.yempe.financeapps.core.domain.util.onSuccess
import com.yempe.financeapps.core.presentation.vm.BaseViewModel
import com.yempe.financeapps.feature.converter.R
import com.yempe.financeapps.feature.converter.domain.usecase.ObserveAvailableAssetsUseCase
import com.yempe.financeapps.feature.converter.domain.usecase.ObserveConvertedRatesUseCase
import com.yempe.financeapps.feature.converter.domain.usecase.RefreshAvailableAssetsUseCase
import com.yempe.financeapps.feature.converter.domain.usecase.UpdateAssetFavoriteStateUseCase
import com.yempe.financeapps.feature.converter.presenter.intent.AssetListScreenUIIntent
import com.yempe.financeapps.feature.converter.presenter.mapper.AssetListScreenUIMapper
import com.yempe.financeapps.feature.converter.presenter.model.AssetListUIModel
import com.yempe.financeapps.feature.settings.api.SettingsModuleApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AssetListScreenViewModel @Inject constructor(
    private val uiMapper: AssetListScreenUIMapper,
    private val refreshAvailableAssetsUseCase: RefreshAvailableAssetsUseCase,
    private val observeAvailableAssetsUseCase: ObserveAvailableAssetsUseCase,
    private val observeConvertedRatesUseCase: ObserveConvertedRatesUseCase,
    private val updateAssetFavoriteStateUseCase: UpdateAssetFavoriteStateUseCase,
    private val settingsModuleApi: SettingsModuleApi
) : BaseViewModel<AssetListScreenState, Unit, AssetListUIEvent>(
    initialState = AssetListScreenState()
) {
    val uiModel: StateFlow<AssetListUIModel> = state
        .map { uiMapper.mapToUiModel(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = uiMapper.mapToUiModel(initialState)
        )

    init {
        observeConvertedAmounts()
        observeAndUpdateAvailableAssets()
        observeMaxDecimalDigits()
    }

    fun onIntent(intent: AssetListScreenUIIntent) {
        when (intent) {
            is AssetListScreenUIIntent.AssetAmountChanged -> {
                updateBaseCurrencyAndAmount(
                    baseCurrency = intent.assetCode,
                    amount = intent.amount
                )
            }

            is AssetListScreenUIIntent.OnAssetFavorite -> {
                updateAssetFavoriteState(
                    assetCode = intent.assetCode
                )
            }

        }
    }

    private fun updateBaseCurrencyAndAmount(
        baseCurrency: String,
        amount: Double
    ) {
        updateState { state ->
            state.copy(
                baseCurrency = baseCurrency,
                convertAmount = amount
            )
        }
    }

    private fun updateAssetFavoriteState(assetCode: String) {
        launchAll {
            val favState = updateAssetFavoriteStateUseCase(assetCode)
            favState.onSuccess { state ->
                postEvent(
                    event = AssetListUIEvent.ShowToastWithResId(
                        message = if (state) {
                            R.string.msg_added_to_favorites
                        } else {
                            R.string.msg_removed_from_favorites
                        }
                    )
                )
            }.onError { ex, msg ->
                postEvent(
                    event = AssetListUIEvent.ShowToast(message = msg.toString())
                )
            }
        }
    }

    private fun observeAndUpdateAvailableAssets() {
        launchAll {
            observeAvailableAssetsUseCase()
                .onStart { refreshAvailableAssetsUseCase() }
                .catch { Timber.e(it) }
                .collect { result ->
                    result
                        .onLoading {
                            updateState { it.copy(isLoading = true) }
                        }
                        .onSuccess { assets ->
                            updateState { state ->
                                val initialBaseCurrency =
                                    if (state.baseCurrency.isNullOrEmpty() || state.convertAmount != null) {
                                        assets.firstOrNull()?.code
                                    } else {
                                        state.baseCurrency
                                    }

                                state.copy(
                                    isLoading = false,
                                    assetList = assets,
                                    baseCurrency = initialBaseCurrency,
                                    convertAmount = if (initialBaseCurrency != state.baseCurrency) 0.0
                                    else state.convertAmount
                                )
                            }
                        }
                        .onError { ex, mes ->
                            updateState { it.copy(isLoading = false) }
                            postEvent(AssetListUIEvent.ShowToast(mes.toString()))
                        }
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private fun observeConvertedAmounts() {
        launchAll {
            state.map { it.baseCurrency to it.convertAmount }
                .distinctUntilChanged()
                .debounce(300L)
                .flatMapLatest { (baseCurrency, convertAmount) ->
                    if (baseCurrency != null && convertAmount != null) {
                        observeConvertedRatesUseCase(
                            param = ObserveConvertedRatesUseCase.Param(
                                baseCode = baseCurrency,
                                inputAmount = convertAmount
                            )
                        )
                    } else {
                        flowOf(value = ResultWrapper.Success(emptyList()))
                    }
                }
                .collectLatest { result ->
                    result.onSuccess { data ->
                        if (data.isNotEmpty()) {
                            updateState { currentState ->
                                currentState.copy(
                                    convertedAmounts = data,
                                    isLoading = false,
                                )
                            }
                        }
                    }.onError { ex, msg ->
                        updateState { it.copy(isLoading = false) }
                        postEvent(AssetListUIEvent.ShowToast(message = msg.toString()))
                    }
                }
        }
    }

    private fun observeMaxDecimalDigits() {
        launchAll {
            settingsModuleApi.observeMaxDecimalDigits()
                .catch { Timber.e(it) }
                .collectLatest { maxDigits ->
                    maxDigits.onSuccess { data ->
                        updateState { state ->
                            state.copy(
                                maxDecimalDigit = data
                            )
                        }
                    }
                }
        }
    }
}