package com.yempe.financeapps.feature.converter.presenter.ui

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yempe.financeapps.core.presentation.R
import com.yempe.financeapps.feature.converter.presenter.intent.AssetListScreenUIIntent
import com.yempe.financeapps.feature.converter.presenter.model.AssetListUIModel
import com.yempe.financeapps.feature.converter.presenter.mvi.AssetListScreenViewModel
import com.yempe.financeapps.feature.converter.presenter.mvi.AssetListUIEvent
import com.yempe.financeapps.feature.converter.presenter.ui.parts.AssetItem
import com.yempe.financeapps.feature.converter.presenter.ui.parts.AssetItemShimmer
import com.yempe.financeapps.feature.converter.presenter.ui.preview.PreviewData

@Composable
fun AssetListScreen(
    viewModel: AssetListScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiModel.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AssetListUIEvent.ShowToastWithResId -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                is AssetListUIEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    ScreenContent(
        uiState = uiState,
        onAssetAmountChanged = { assetCode, amount ->
            viewModel.onIntent(
                intent = AssetListScreenUIIntent.AssetAmountChanged(
                    assetCode = assetCode,
                    amount = amount
                )
            )
        },
        onAssetFavourite = { assetCode ->
            viewModel.onIntent(
                intent = AssetListScreenUIIntent.OnAssetFavorite(
                    assetCode = assetCode
                )
            )
        }
    )
}

@Composable
private fun ScreenContent(
    uiState: AssetListUIModel,
    onAssetAmountChanged: (String, Double) -> Unit,
    onAssetFavourite: (String) -> Unit
) {

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF5F7FA),
                        Color(0xFFE8EDF2)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    focusManager.clearFocus()
                }
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(com.yempe.financeapps.feature.converter.R.string.text_currency_converter),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_app_converter),
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color.Unspecified
                    )
                }
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                }

                if (uiState.listItems.isNotEmpty()) {
                    items(
                        items = uiState.listItems,
                        key = { it.assetShortName })
                    { item ->
                        AssetItem(
                            modifier = Modifier.animateItem(),
                            item = item,
                            maxDecimalDigit = uiState.maxDecimalDigit,
                            onValueChanged = { newAmount ->
                                onAssetAmountChanged.invoke(item.assetShortName, newAmount)
                            },
                            onFavorite = { assetCode ->
                                onAssetFavourite.invoke(assetCode)
                            }
                        )
                    }
                } else {
                    repeat(12) {
                        item {
                            AssetItemShimmer()
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
@Preview()
private fun Preview() {
    ScreenContent(
        uiState = PreviewData.getAssetListUIModel(),
        onAssetFavourite = {},
        onAssetAmountChanged = { x, y -> }
    )
}
