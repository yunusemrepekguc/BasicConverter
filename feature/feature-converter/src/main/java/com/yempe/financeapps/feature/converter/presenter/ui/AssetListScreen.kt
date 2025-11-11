package com.yempe.financeapps.feature.converter.presenter.ui

import android.widget.Toast
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yempe.financeapps.core.presentation.R
import com.yempe.financeapps.feature.converter.presenter.intent.AssetListScreenUIIntent
import com.yempe.financeapps.feature.converter.presenter.model.AssetListUIItemModel
import com.yempe.financeapps.feature.converter.presenter.mvi.AssetListScreenViewModel
import com.yempe.financeapps.feature.converter.presenter.mvi.AssetListUIEvent

@Composable
fun AssetListScreen(
    viewModel: AssetListScreenViewModel = hiltViewModel()
) {
    val state by viewModel.uiModel.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is AssetListUIEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

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

                if (state.listItems.isNotEmpty()) {
                    items(
                        state.listItems,
                        key = { it.assetShortName })
                    { item ->
                        AssetItem(
                            modifier = Modifier.animateItem(),
                            item = item,
                            maxDecimalDigit = state.maxDecimalDigit,
                            onValueChanged = { newAmount ->
                                viewModel.onIntent(
                                    intent = AssetListScreenUIIntent.AssetAmountChanged(
                                        assetCode = item.assetShortName,
                                        amount = newAmount
                                    )
                                )
                            },
                            onFavorite = { assetCode ->
                                viewModel.onIntent(
                                    intent = AssetListScreenUIIntent.OnAssetFavorite(
                                        assetCode = assetCode
                                    )
                                )
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AssetItem(
    modifier: Modifier,
    item: AssetListUIItemModel,
    maxDecimalDigit: Int,
    onValueChanged: (Double) -> Unit,
    onFavorite: (String) -> Unit
) {
    var inputText by remember {
        mutableStateOf(
            item.convertedAmount.takeIf { it != "0" && it != "0.0" } ?: ""
        )
    }
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(item.convertedAmount) {
        inputText = item.convertedAmount
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = if (isFocused) 8.dp else 2.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color(0x1A000000)
            )
            .combinedClickable(
                onClick = {
                    isFocused = true
                },
                onLongClick = {
                    onFavorite.invoke(item.assetShortName)
                }
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    other = if (item.isBaseCurrency) {
                        modifier.background(
                            color = Color(0x2000B500)
                        )
                    } else modifier
                )
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (item.isFavorite) {
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = null,
                        tint = Color(0xFFEFBF04),
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                onFavorite.invoke(item.assetShortName)
                            },
                    )
                    Spacer(modifier = Modifier.width(width = 4.dp))
                }
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFFF0F4F8),
                                    Color(0xFFE1E8ED)
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = item.assetImage),
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = Color.Unspecified
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = item.assetShortName,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = Color(0xFF1A1A1A)
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.assetLongName,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF707070)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                // Symbol and Input in same row
                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isFocused) Color(0xFFF0F4F8) else Color.Transparent
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item.assetSymbol,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = Color(0xFF1A1A1A)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            BasicTextField(
                                value = inputText,
                                onValueChange = { newValue ->
                                    val filtered =
                                        newValue.filter { it.isDigit() || it == '.' || it == ',' }
                                    val normalized = filtered.replace(',', '.')

                                    val dotCount = normalized.count { it == '.' }
                                    if (dotCount <= 1) {
                                        val parts = normalized.split('.')

                                        val limited =
                                            if (parts.size == 2 && parts[1].length > maxDecimalDigit) {
                                                parts[0] + "." + parts[1].take(maxDecimalDigit)
                                            } else {
                                                normalized
                                            }

                                        inputText = limited

                                        val doubleValue = limited.toDoubleOrNull() ?: 0.0
                                        onValueChanged(doubleValue)
                                    }
                                },
                                singleLine = true,
                                textStyle = LocalTextStyle.current.copy(
                                    color = Color(0xFF1A1A1A),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    textAlign = TextAlign.End
                                ),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Decimal
                                ),
                                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                                modifier = Modifier
                                    .widthIn(min = 60.dp, max = 140.dp)
                                    .focusRequester(focusRequester)
                                    .onFocusChanged { focusState ->
                                        isFocused = focusState.isFocused
                                    },
                                decorationBox = { innerTextField ->
                                    if (inputText.isEmpty()) {
                                        Text(
                                            text = "0",
                                            style = LocalTextStyle.current.copy(
                                                color = Color(0xFF707070),
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.SemiBold
                                            ),
                                            textAlign = TextAlign.End
                                        )
                                    }
                                    innerTextField()
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = item.baseRate,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF707070)
                )
            }
        }
    }

    LaunchedEffect(isFocused) {
        if (isFocused) {
            focusRequester.requestFocus()
        }
    }
}

@Composable
fun AssetItemShimmer() {
    val shimmerColors = listOf(
        Color(0xFFE0E0E0),
        Color(0xFFF5F5F5),
        Color(0xFFE0E0E0),
    )

    val transition = rememberInfiniteTransition(label = "shimmerTransition")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslate"
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset(translateAnim - 1000f, 0f),
        end = Offset(translateAnim, 0f)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .height(72.dp)
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = Color(0x1A000000)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(brush)
        )
    }
}
