package com.example.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.ControlsBar
import com.example.ui.components.GoldenRatioOverlay
import com.example.ui.components.SwissHud
import com.example.ui.theme.VoidBlack
import com.example.util.HapticHelper
import com.example.viewmodel.ZenoViewModel
import kotlinx.coroutines.flow.collectLatest

class ZenoWebBridge(private val onScaleChanged: (Double, Int, Int) -> Unit) {
    @JavascriptInterface
    fun onScaleChanged(gapMeters: Double, stepCount: Int, tierIndex: Int) {
        onScaleChanged.invoke(gapMeters, stepCount, tierIndex)
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ZenoScreen(
    viewModel: ZenoViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var webViewRef by remember { mutableStateOf<WebView?>(null) }

    LaunchedEffect(Unit) {
        val haptic = HapticHelper(context)
        viewModel.setHapticHelper(haptic)
    }

    // Evaluate JavaScript commands sent from ViewModel
    LaunchedEffect(webViewRef) {
        val webView = webViewRef ?: return@LaunchedEffect
        viewModel.jsCommands.collectLatest { jsCode ->
            webView.post {
                webView.evaluateJavascript(jsCode, null)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VoidBlack)
            .testTag("zeno_main_container")
    ) {
        // --- 3D THREE.JS WEBVIEW CANVAS ---
        AndroidView(
            factory = { ctx ->
                WebView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    setBackgroundColor(Color.parseColor("#0A0A0C"))
                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        allowFileAccess = true
                        cacheMode = WebSettings.LOAD_NO_CACHE
                        useWideViewPort = true
                        loadWithOverviewMode = true
                    }
                    webChromeClient = WebChromeClient()
                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            // Initialize language and golden ratio state
                            view?.evaluateJavascript("window.setLanguage && window.setLanguage('${uiState.language.code}');", null)
                            view?.evaluateJavascript("window.setGoldenRatioVisible && window.setGoldenRatioVisible(${uiState.showGoldenRatio});", null)
                        }
                    }

                    val bridge = ZenoWebBridge { gap, steps, tierIdx ->
                        post {
                            viewModel.onScaleChangedFromWeb(gap, steps, tierIdx)
                        }
                    }
                    addJavascriptInterface(bridge, "AndroidBridge")
                    loadUrl("file:///android_asset/zeno/zeno_view.html")
                    webViewRef = this
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .testTag("zeno_3d_canvas")
        )

        // Clean up WebView on disposal
        DisposableEffect(Unit) {
            onDispose {
                webViewRef?.destroy()
                webViewRef = null
            }
        }

        // --- GOLDEN RATIO OVERLAY GRID ---
        GoldenRatioOverlay(
            visible = uiState.showGoldenRatio,
            modifier = Modifier.fillMaxSize()
        )

        // --- SWISS HUD OVERLAY (TOP) ---
        SwissHud(
            state = uiState,
            onToggleLanguage = { viewModel.toggleLanguage() },
            onToggleGoldenRatio = { viewModel.toggleGoldenRatio() },
            onToggleHud = { viewModel.toggleHud() },
            onSelectTier = { viewModel.onJumpTier(it) },
            modifier = Modifier
                .align(Alignment.TopCenter)
                .windowInsetsPadding(WindowInsets.safeDrawing)
        )

        // --- CONTROLS BAR (BOTTOM) ---
        ControlsBar(
            state = uiState,
            onHalve = { viewModel.onHalve() },
            onDouble = { viewModel.onDouble() },
            onReset = { viewModel.onReset() },
            onSliderChanged = { viewModel.onSliderChanged(it) },
            onToggleAutoHalve = { viewModel.toggleAutoHalve() },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
        )
    }
}
