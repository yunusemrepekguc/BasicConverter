package com.yempe.financeapps.feature.setting.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowDown
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yempe.financeapps.feature.setting.R
import com.yempe.financeapps.feature.setting.presentation.intent.SettingScreenUIIntent
import com.yempe.financeapps.feature.setting.presentation.mvi.SettingsScreenViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsScreenViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var maxDecimalDigits by remember { mutableIntStateOf(2) }

    LaunchedEffect(uiState.currentDecimalCount) {
        maxDecimalDigits = uiState.currentDecimalCount
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFF5F7FA),
                        Color(0xFFE8EDF2)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 24.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_setting),
                        contentDescription = null,
                        modifier = Modifier.size(36.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Settings",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Text(
                            text = "Max Decimal Count",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = Color(0xFF1A1A1A)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Select the max decimal count",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF707070)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(end = 12.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (maxDecimalDigits > 0) Color(0xFFE3F2FD) else Color(
                                            0xFFF5F5F5
                                        )
                                    )
                                    .clickable(enabled = maxDecimalDigits > 0) {
                                        viewModel.onIntent(SettingScreenUIIntent.OnMaxDigitDecrease)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.KeyboardArrowDown,
                                    contentDescription = "Decrease",
                                    tint = if (maxDecimalDigits > 0) Color(0xFF1976D2) else Color(
                                        0xFFBDBDBD
                                    ),
                                    modifier = Modifier.size(24.dp)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(60.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color(0xFFF5F7FA)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = maxDecimalDigits.toString(),
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.Bold
                                    ),
                                    color = Color(0xFF1976D2)
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .padding(start = 12.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (maxDecimalDigits < 6) Color(0xFFE3F2FD) else Color(
                                            0xFFF5F5F5
                                        )
                                    )
                                    .clickable(enabled = maxDecimalDigits < 6) {
                                        viewModel.onIntent(SettingScreenUIIntent.OnMaxDigitIncrease)
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.KeyboardArrowUp,
                                    contentDescription = "Increase",
                                    tint = if (maxDecimalDigits < 6) Color(0xFF1976D2) else Color(
                                        0xFFBDBDBD
                                    ),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Min: 0 - Max: 6",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF9E9E9E),
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFF3E0)
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 0.dp
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.msg_settings_auto_save),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFFE65100)
                        )
                    }
                }
            }
        }
    }
}