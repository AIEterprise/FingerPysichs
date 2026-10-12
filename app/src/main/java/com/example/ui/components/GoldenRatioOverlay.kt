package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke

private const val PHI = 1.6180339887f

@Composable
fun GoldenRatioOverlay(
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    if (!visible) return

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // Golden cut coordinates
        val phiX1 = w * (1f - 1f / PHI) // ~0.382
        val phiX2 = w * (1f / PHI)      // ~0.618
        val phiY1 = h * (1f - 1f / PHI)
        val phiY2 = h * (1f / PHI)

        val gridColor = Color(0x22F5F5F7)
        val phiColor = Color(0x66E5A93C)
        val dashedEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 8f), 0f)

        // 1. Golden ratio dividing axes
        drawLine(
            color = phiColor,
            start = Offset(phiX1, 0f),
            end = Offset(phiX1, h),
            strokeWidth = 1f,
            pathEffect = dashedEffect
        )
        drawLine(
            color = phiColor,
            start = Offset(phiX2, 0f),
            end = Offset(phiX2, h),
            strokeWidth = 1f,
            pathEffect = dashedEffect
        )
        drawLine(
            color = phiColor,
            start = Offset(0f, phiY1),
            end = Offset(w, phiY1),
            strokeWidth = 1f,
            pathEffect = dashedEffect
        )
        drawLine(
            color = phiColor,
            start = Offset(0f, phiY2),
            end = Offset(w, phiY2),
            strokeWidth = 1f,
            pathEffect = dashedEffect
        )

        // 2. Center reticle & measurement ticks
        val cx = w / 2f
        val cy = h / 2f
        val tickLen = 16f
        val reticleColor = Color(0xAAFFFAF0)

        // Crosshair ticks
        drawLine(reticleColor, Offset(cx - tickLen, cy), Offset(cx + tickLen, cy), strokeWidth = 1.2f)
        drawLine(reticleColor, Offset(cx, cy - tickLen), Offset(cx, cy + tickLen), strokeWidth = 1.2f)

        // 3. Technical Corner Crop Marks (Swiss Bauhaus instrumentation)
        val cropSize = 24f
        val cropInset = 20f
        val cropColor = Color(0x44FFFFFF)

        // Top-left
        drawLine(cropColor, Offset(cropInset, cropInset), Offset(cropInset + cropSize, cropInset), strokeWidth = 1.5f)
        drawLine(cropColor, Offset(cropInset, cropInset), Offset(cropInset, cropInset + cropSize), strokeWidth = 1.5f)
        // Top-right
        drawLine(cropColor, Offset(w - cropInset, cropInset), Offset(w - cropInset - cropSize, cropInset), strokeWidth = 1.5f)
        drawLine(cropColor, Offset(w - cropInset, cropInset), Offset(w - cropInset, cropInset + cropSize), strokeWidth = 1.5f)
        // Bottom-left
        drawLine(cropColor, Offset(cropInset, h - cropInset), Offset(cropInset + cropSize, h - cropInset), strokeWidth = 1.5f)
        drawLine(cropColor, Offset(cropInset, h - cropInset), Offset(cropInset, h - cropInset - cropSize), strokeWidth = 1.5f)
        // Bottom-right
        drawLine(cropColor, Offset(w - cropInset, h - cropInset), Offset(w - cropInset - cropSize, h - cropInset), strokeWidth = 1.5f)
        drawLine(cropColor, Offset(w - cropInset, h - cropInset), Offset(w - cropInset, h - cropInset - cropSize), strokeWidth = 1.5f)
    }
}
