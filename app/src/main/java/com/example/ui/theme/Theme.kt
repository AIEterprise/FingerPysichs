package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val SwissDarkColorScheme = darkColorScheme(
    primary = GoldenRatioAmber,
    onPrimary = VoidBlack,
    primaryContainer = ObsidianSurfaceVariant,
    onPrimaryContainer = GoldenRatioAmber,
    secondary = TextTechnicalWhite,
    onSecondary = VoidBlack,
    secondaryContainer = ObsidianSurface,
    onSecondaryContainer = TextTechnicalWhite,
    tertiary = QuantumCyan,
    onTertiary = VoidBlack,
    background = VoidBlack,
    onBackground = TextTechnicalWhite,
    surface = ObsidianSurface,
    onSurface = TextTechnicalWhite,
    surfaceVariant = ObsidianSurfaceVariant,
    onSurfaceVariant = TextMutedGray,
    outline = BorderMuted
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SwissDarkColorScheme,
        typography = SwissTypography,
        content = content
    )
}
