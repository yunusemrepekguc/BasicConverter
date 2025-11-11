package com.yempe.financeapps.feature.converter.presenter.mvi

import androidx.lifecycle.viewModelScope
import com.yempe.financeapps.core.presentation.vm.BaseViewModel
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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AssetListScreenViewModel @Inject constructor(
    private val uiMapper: AssetListScreenUIMapper,
    private val refreshAvailableAssetsUseCase: RefreshAvailableAssetsUseCase,
    private val observeAvailableAssetsUseCase: ObserveAvailableAssetsUseCase,
    private val observeConvertedRatesUseCase: ObserveConvertedRatesUseCase,
    private val updateAssetFavoriteState: UpdateAssetFavoriteStateUseCase,
    private val settingsModuleApi: SettingsModuleApi
) : BaseViewModel<AssetListScreenState, AssetListNavigationEvent, AssetListScreenUIIntent>(
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
        launchAll { updateAssetFavoriteState.invoke(assetCode) }
    }

    private fun observeAndUpdateAvailableAssets() {
        launchAll {
            observeAvailableAssetsUseCase()
                .onStart { refreshAvailableAssetsUseCase() }
                .collect { assets ->
                    updateState { state ->
                        val initialBaseCurrency =
                            if (state.baseCurrency.isNullOrEmpty() || state.convertAmount != null) {
                                assets.firstOrNull()?.code
                            } else {
                                state.baseCurrency
                            }

                        state.copy(
                            assetList = assets,
                            baseCurrency = initialBaseCurrency,
                            convertAmount = if (initialBaseCurrency != state.baseCurrency) 0.0 else state.convertAmount
                        )
                    }
                }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun observeConvertedAmounts() {
        launchAll {
            state.map { it.baseCurrency to it.convertAmount }
                .distinctUntilChanged()
                .flatMapLatest { (baseCurrency, convertAmount) ->
                    if (baseCurrency != null && convertAmount != null) {
                        observeConvertedRatesUseCase(
                            param = ObserveConvertedRatesUseCase.Param(
                                baseCode = baseCurrency,
                                inputAmount = convertAmount
                            )
                        )
                    } else {
                        flowOf(emptyList())
                    }
                }
                .filter { it.isNotEmpty() }
                .onEach { println("streamConvertedAmounts > $it") }
                .collectLatest { convertedAmounts ->
                    updateState { currentState ->
                        currentState.copy(convertedAmounts = convertedAmounts)
                    }
                }
        }
    }

    private fun observeMaxDecimalDigits() {
        launchAll {
            settingsModuleApi.observeMaxDecimalDigits().collectLatest { maxDigits ->
                updateState { state ->
                    state.copy(
                        maxDecimalDigit = maxDigits
                    )
                }
            }
        }
    }
}