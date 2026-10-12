package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.model.Language
import com.example.model.ScaleTier
import com.example.model.ZenoState
import com.example.util.HapticHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

class ZenoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ZenoState())
    val uiState: StateFlow<ZenoState> = _uiState.asStateFlow()

    private val _jsCommands = MutableSharedFlow<String>(extraBufferCapacity = 16)
    val jsCommands: SharedFlow<String> = _jsCommands.asSharedFlow()

    private var hapticHelper: HapticHelper? = null
    private var autoHalveJob: Job? = null

    fun setHapticHelper(helper: HapticHelper) {
        this.hapticHelper = helper
    }

    // Called from WebView JavaScript via @JavascriptInterface
    fun onScaleChangedFromWeb(gapMeters: Double, stepCount: Int, tierIndex: Int) {
        val newTier = when (tierIndex) {
            0 -> ScaleTier.MACRO
            1 -> ScaleTier.FINGERPRINT
            2 -> ScaleTier.CELLULAR
            else -> ScaleTier.ATOMIC
        }

        val previousTier = _uiState.value.tier

        _uiState.update { current ->
            current.copy(
                gapMeters = gapMeters,
                zenoStep = stepCount,
                tier = newTier
            )
        }

        if (previousTier != newTier) {
            hapticHelper?.tierBoundaryPulse()
        }
    }

    fun onHalve() {
        hapticHelper?.heavyHalveStep()
        _jsCommands.tryEmit("window.halveGap && window.halveGap();")
    }

    fun onDouble() {
        hapticHelper?.click()
        _jsCommands.tryEmit("window.doubleGap && window.doubleGap();")
    }

    fun onReset() {
        stopAutoHalving()
        hapticHelper?.click()
        _jsCommands.tryEmit("window.resetGap && window.resetGap();")
    }

    fun onSliderChanged(fraction: Float) {
        // Slider fraction from 0.0 (Atomic, 10^-10 m) to 1.0 (Macro, 20mm = 2.0e-2 m)
        // Logarithmic interpolation: exp from -10 to -1.69897
        val minLog = -10.0
        val maxLog = -1.69897 // log10(0.020)
        val targetLog = minLog + fraction * (maxLog - minLog)
        val targetMeters = Math.pow(10.0, targetLog)

        _jsCommands.tryEmit("window.setGap && window.setGap($targetMeters);")
    }

    fun onJumpTier(tier: ScaleTier) {
        hapticHelper?.tierBoundaryPulse()
        _jsCommands.tryEmit("window.jumpToTier && window.jumpToTier('${tier.id}');")
    }

    fun toggleGoldenRatio() {
        val next = !_uiState.value.showGoldenRatio
        _uiState.update { it.copy(showGoldenRatio = next) }
        hapticHelper?.tick()
        _jsCommands.tryEmit("window.setGoldenRatioVisible && window.setGoldenRatioVisible($next);")
    }

    fun toggleHud() {
        hapticHelper?.tick()
        _uiState.update { it.copy(showHud = !it.showHud) }
    }

    fun toggleLanguage() {
        val nextLang = if (_uiState.value.language == Language.EN) Language.JA else Language.EN
        _uiState.update { it.copy(language = nextLang) }
        hapticHelper?.tick()
        _jsCommands.tryEmit("window.setLanguage && window.setLanguage('${nextLang.code}');")
    }

    fun toggleAutoHalve() {
        if (_uiState.value.isAutoHalving) {
            stopAutoHalving()
        } else {
            startAutoHalving()
        }
    }

    private fun startAutoHalving() {
        _uiState.update { it.copy(isAutoHalving = true) }
        autoHalveJob?.cancel()
        autoHalveJob = viewModelScope.launch {
            while (_uiState.value.isAutoHalving) {
                delay(1100)
                if (_uiState.value.gapMeters <= 2.0e-10) {
                    stopAutoHalving()
                    break
                }
                onHalve()
            }
        }
    }

    private fun stopAutoHalving() {
        _uiState.update { it.copy(isAutoHalving = false) }
        autoHalveJob?.cancel()
        autoHalveJob = null
    }

    override fun onCleared() {
        super.onCleared()
        autoHalveJob?.cancel()
    }
}
