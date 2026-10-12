package com.example.ui.components

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
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Language
import com.example.model.ZenoState
import com.example.ui.theme.GoldenRatioAmber
import com.example.ui.theme.ObsidianSurface
import com.example.ui.theme.ObsidianSurfaceVariant
import com.example.ui.theme.RobotoMono
import com.example.ui.theme.TextMutedGray
import com.example.ui.theme.TextTechnicalWhite
import com.example.ui.theme.VoidBlack
import kotlin.math.log10

@Composable
fun ControlsBar(
    state: ZenoState,
    onHalve: () -> Unit,
    onDouble: () -> Unit,
    onReset: () -> Unit,
    onSliderChanged: (Float) -> Unit,
    onToggleAutoHalve: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Current logarithmic fraction (0 = atom 10^-10, 1 = macro 0.02m)
    val minLog = -10.0
    val maxLog = -1.69897
    val currentLog = log10(state.gapMeters.coerceIn(1.0e-10, 0.020))
    val sliderValue = ((currentLog - minLog) / (maxLog - minLog)).toFloat().coerceIn(0f, 1f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(ObsidianSurface.copy(alpha = 0.95f), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // --- LOGARITHMIC CONTINUOUS SLIDER ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (state.language == Language.EN) "PRECISION ANALOG SLIDER" else "連続アナログスライダー",
                fontFamily = RobotoMono,
                fontSize = 10.sp,
                letterSpacing = 0.8.sp,
                color = TextMutedGray
            )
            Text(
                text = if (sliderValue < 0.2f) "⚛ Atom" else if (sliderValue < 0.5f) "🦠 Cell" else if (sliderValue < 0.8f) "🖐 Print" else "🖐 Macro",
                fontFamily = RobotoMono,
                fontSize = 11.sp,
                color = GoldenRatioAmber
            )
        }

        Slider(
            value = sliderValue,
            onValueChange = { onSliderChanged(it) },
            colors = SliderDefaults.colors(
                thumbColor = GoldenRatioAmber,
                activeTrackColor = GoldenRatioAmber,
                inactiveTrackColor = ObsidianSurfaceVariant
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("scale_slider")
        )

        // Rulers markings under slider
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("0.1 nm", fontFamily = RobotoMono, fontSize = 9.sp, color = TextMutedGray)
            Text("1 µm", fontFamily = RobotoMono, fontSize = 9.sp, color = TextMutedGray)
            Text("100 µm", fontFamily = RobotoMono, fontSize = 9.sp, color = TextMutedGray)
            Text("1 mm", fontFamily = RobotoMono, fontSize = 9.sp, color = TextMutedGray)
            Text("20 mm", fontFamily = RobotoMono, fontSize = 9.sp, color = TextMutedGray)
        }

        Spacer(modifier = Modifier.height(10.dp))

        // --- DIGITAL BUTTONS ROW ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Main Halving Button: ÷ 2
            Button(
                onClick = onHalve,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GoldenRatioAmber,
                    contentColor = VoidBlack
                ),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .weight(1.8f)
                    .height(52.dp)
                    .testTag("halve_button")
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (state.language == Language.EN) "÷ 2  HALVE" else "÷ 2  半分にする",
                        fontFamily = RobotoMono,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "d / 2",
                        fontFamily = RobotoMono,
                        fontSize = 10.sp,
                        color = VoidBlack.copy(alpha = 0.8f)
                    )
                }
            }

            // Doubling Button: × 2
            OutlinedButton(
                onClick = onDouble,
                shape = RoundedCornerShape(8.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(Color(0x55FFFFFF))),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = ObsidianSurfaceVariant,
                    contentColor = TextTechnicalWhite
                ),
                modifier = Modifier
                    .weight(1.2f)
                    .height(52.dp)
                    .testTag("double_button")
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = if (state.language == Language.EN) "× 2" else "× 2",
                        fontFamily = RobotoMono,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = if (state.language == Language.EN) "Double" else "2倍",
                        fontFamily = RobotoMono,
                        fontSize = 10.sp,
                        color = TextMutedGray
                    )
                }
            }

            // Auto-halving toggle button
            OutlinedButton(
                onClick = onToggleAutoHalve,
                shape = RoundedCornerShape(8.dp),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(
                        if (state.isAutoHalving) GoldenRatioAmber else Color(0x33FFFFFF)
                    )
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (state.isAutoHalving) GoldenRatioAmber.copy(alpha = 0.2f) else ObsidianSurfaceVariant,
                    contentColor = if (state.isAutoHalving) GoldenRatioAmber else TextTechnicalWhite
                ),
                modifier = Modifier
                    .weight(1.2f)
                    .height(52.dp)
                    .testTag("auto_step_button")
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = if (state.isAutoHalving) Icons.Default.Stop else Icons.Default.PlayArrow,
                        contentDescription = "Auto",
                        modifier = Modifier.size(16.dp),
                        tint = if (state.isAutoHalving) GoldenRatioAmber else TextTechnicalWhite
                    )
                    Text(
                        text = if (state.isAutoHalving) (if (state.language == Language.EN) "Stop" else "停止")
                               else (if (state.language == Language.EN) "Auto" else "連続"),
                        fontFamily = RobotoMono,
                        fontSize = 10.sp
                    )
                }
            }

            // Reset Button
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(ObsidianSurfaceVariant)
                    .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(8.dp))
                    .clickable { onReset() }
                    .testTag("reset_button"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reset",
                    tint = TextMutedGray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
