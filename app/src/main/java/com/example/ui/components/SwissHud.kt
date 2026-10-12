package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Grid4x4
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Language
import com.example.model.ScaleTier
import com.example.model.ZenoState
import com.example.ui.theme.GoldenRatioAmber
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceVariant
import com.example.ui.theme.RobotoMono
import com.example.ui.theme.TextMutedGray
import com.example.ui.theme.TextTechnicalWhite

@Composable
fun SwissHud(
    state: ZenoState,
    onToggleLanguage: () -> Unit,
    onToggleGoldenRatio: () -> Unit,
    onToggleHud: () -> Unit,
    onSelectTier: (ScaleTier) -> Unit,
    modifier: Modifier = Modifier
) {
    var showInsightDetail by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // --- TOP BAR: TITLE & TOGGLES ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (state.language == Language.EN) "ZENO'S PARADOX" else "ゼノンの逆説",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextTechnicalWhite,
                        modifier = Modifier.testTag("app_title")
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(GoldenRatioAmber.copy(alpha = 0.15f), RoundedCornerShape(4.dp))
                            .border(0.8.dp, GoldenRatioAmber, RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "φ = 1.618",
                            fontFamily = RobotoMono,
                            fontSize = 10.sp,
                            color = GoldenRatioAmber
                        )
                    }
                }
                Text(
                    text = if (state.language == Language.EN) "The Infinite Dichotomy · d → 0" else "無限分割 · 指先から原子の狭間へ",
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMutedGray
                )
            }

            // Controls buttons
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Language Toggle
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(ObsidianSurfaceVariant)
                        .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(6.dp))
                        .clickable { onToggleLanguage() }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("lang_toggle"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Toggle Language",
                            tint = GoldenRatioAmber,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = state.language.label,
                            fontFamily = RobotoMono,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextTechnicalWhite
                        )
                    }
                }

                Spacer(modifier = Modifier.width(6.dp))

                // Golden Ratio Grid Toggle
                IconButton(
                    onClick = onToggleGoldenRatio,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (state.showGoldenRatio) GoldenRatioAmber.copy(alpha = 0.2f) else ObsidianSurfaceVariant)
                        .border(1.dp, if (state.showGoldenRatio) GoldenRatioAmber else Color(0x33FFFFFF), RoundedCornerShape(6.dp))
                        .testTag("phi_toggle")
                ) {
                    Icon(
                        imageVector = Icons.Default.Grid4x4,
                        contentDescription = "Toggle Golden Ratio Grid",
                        tint = if (state.showGoldenRatio) GoldenRatioAmber else TextMutedGray,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // HUD Visibility Toggle
                IconButton(
                    onClick = onToggleHud,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(6.dp))
                        .background(ObsidianSurfaceVariant)
                        .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(6.dp))
                        .testTag("hud_toggle")
                ) {
                    Icon(
                        imageVector = if (state.showHud) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle HUD",
                        tint = TextTechnicalWhite,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // --- SCIENTIFIC METRIC DISPLAY ---
        AnimatedVisibility(
            visible = state.showHud,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ObsidianSurface.copy(alpha = 0.92f), RoundedCornerShape(10.dp))
                        .border(1.dp, Color(0x28FFFFFF), RoundedCornerShape(10.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        // Metrics row: Distance and Step
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column {
                                Text(
                                    text = if (state.language == Language.EN) "CURRENT GAP DISTANCE" else "指先の間隔",
                                    fontFamily = RobotoMono,
                                    fontSize = 10.sp,
                                    letterSpacing = 1.sp,
                                    color = TextMutedGray
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = state.formattedDistance(),
                                    fontFamily = RobotoMono,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextTechnicalWhite,
                                    modifier = Modifier.testTag("gap_value")
                                )
                                Text(
                                    text = state.formattedScientific(),
                                    fontFamily = RobotoMono,
                                    fontSize = 11.sp,
                                    color = GoldenRatioAmber
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = if (state.language == Language.EN) "ZENO HALVINGS" else "分割回数",
                                    fontFamily = RobotoMono,
                                    fontSize = 10.sp,
                                    letterSpacing = 1.sp,
                                    color = TextMutedGray
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "n = ${state.zenoStep}",
                                    fontFamily = RobotoMono,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldenRatioAmber,
                                    modifier = Modifier.testTag("step_value")
                                )
                                Text(
                                    text = "1 / 2^${state.zenoStep}",
                                    fontFamily = RobotoMono,
                                    fontSize = 11.sp,
                                    color = TextMutedGray
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Tier benchmark indicator
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(ObsidianSurfaceVariant, RoundedCornerShape(6.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (state.language == Language.EN) state.tier.titleEn else state.tier.titleJa,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = GoldenRatioAmber
                            )
                            Text(
                                text = if (state.language == Language.EN) state.tier.benchmarkEn else state.tier.benchmarkJa,
                                fontFamily = RobotoMono,
                                fontSize = 11.sp,
                                color = TextTechnicalWhite
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // --- SCALE TIER SELECTOR CHIPS ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ScaleTier.values().forEach { tier ->
                        val isSelected = state.tier == tier
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) GoldenRatioAmber else ObsidianSurface)
                                .border(
                                    1.dp,
                                    if (isSelected) GoldenRatioAmber else Color(0x28FFFFFF),
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable { onSelectTier(tier) }
                                .padding(vertical = 8.dp)
                                .testTag("tier_chip_${tier.id}"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (state.language == Language.EN) tier.name else tier.titleJa.substringBefore(" "),
                                fontFamily = RobotoMono,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) ObsidianSurface else TextTechnicalWhite
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // --- ZENO PHILOSOPHICAL INSIGHT BANNER ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(ObsidianSurface.copy(alpha = 0.85f))
                        .border(1.dp, Color(0x1FFFFFFF), RoundedCornerShape(8.dp))
                        .clickable { showInsightDetail = !showInsightDetail }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .testTag("paradox_insight_card")
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (state.language == Language.EN) "PARADOX INSIGHT" else "パラドックス考察",
                                fontFamily = RobotoMono,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = GoldenRatioAmber
                            )
                            Text(
                                text = if (showInsightDetail) "▲" else "▼",
                                fontFamily = RobotoMono,
                                fontSize = 10.sp,
                                color = TextMutedGray
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = state.getParadoxCommentary(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextTechnicalWhite,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}
