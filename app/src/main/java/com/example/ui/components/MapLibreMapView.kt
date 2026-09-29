package com.example.ui.components

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.View
import android.webkit.ConsoleMessage
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.example.ui.home.GymLocation
import org.json.JSONArray
import org.json.JSONObject

/**
 * MapLibre GL JS + OpenFreeMap Dark Theme Map for Byce.
 *
 * - Renders OpenFreeMap vector tiles on WebGL2 hardware-accelerated canvas
 * - Bundled local MapLibre GL JS engine + exact OpenFreeMap Dark Style JSON
 * - Glowing Cult.fit neon lime markers & pulsing GPS radar
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun MapLibreMapView(
    gyms: List<GymLocation>,
    selectedGym: GymLocation?,
    recenterTrigger: Int,
    onGymClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var webViewRef by remember { mutableStateOf<WebView?>(null) }
    var isMapLoaded by remember { mutableStateOf(false) }

    // Serialize gyms to JSON array
    val gymsJson = remember(gyms) {
        val jsonArray = JSONArray()
        gyms.forEach { gym ->
            val obj = JSONObject().apply {
                put("id", gym.id)
                put("name", gym.name)
                put("distance", gym.distance)
                put("cityArea", gym.cityArea)
                put("statusText", gym.statusText)
                put("rating", gym.rating)
                put("latitude", gym.latitude)
                put("longitude", gym.longitude)
            }
            jsonArray.put(obj)
        }
        jsonArray.toString()
    }

    // Sync selected gym
    LaunchedEffect(selectedGym?.id, isMapLoaded) {
        if (isMapLoaded && selectedGym != null) {
            webViewRef?.evaluateJavascript(
                "if (window.selectGym) { window.selectGym('${selectedGym.id}'); }",
                null
            )
        }
    }

    // Sync recenter trigger
    LaunchedEffect(recenterTrigger, isMapLoaded) {
        if (isMapLoaded && recenterTrigger > 0) {
            webViewRef?.evaluateJavascript(
                "if (window.recenterMap) { window.recenterMap(); }",
                null
            )
        }
    }

    // Sync gyms update
    LaunchedEffect(gymsJson, isMapLoaded) {
        if (isMapLoaded) {
            webViewRef?.evaluateJavascript(
                "if (window.updateGyms) { window.updateGyms($gymsJson); }",
                null
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0C0C0C))
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    setLayerType(View.LAYER_TYPE_HARDWARE, null)
                    setBackgroundColor(android.graphics.Color.parseColor("#0C0C0C"))

                    WebView.setWebContentsDebuggingEnabled(true)

                    settings.apply {
                        javaScriptEnabled = true
                        domStorageEnabled = true
                        databaseEnabled = true
                        allowFileAccess = true
                        allowContentAccess = true
                        loadWithOverviewMode = true
                        useWideViewPort = true
                        cacheMode = WebSettings.LOAD_DEFAULT
                        mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
                        setSupportZoom(false)
                        builtInZoomControls = false
                        displayZoomControls = false
                    }

                    addJavascriptInterface(
                        object {
                            @JavascriptInterface
                            fun onGymClick(gymId: String) {
                                Handler(Looper.getMainLooper()).post {
                                    onGymClicked(gymId)
                                }
                            }

                            @JavascriptInterface
                            fun onMapReady() {
                                Handler(Looper.getMainLooper()).post {
                                    isMapLoaded = true
                                    evaluateJavascript("if (window.updateGyms) { window.updateGyms($gymsJson); }", null)
                                    if (selectedGym != null) {
                                        evaluateJavascript("if (window.selectGym) { window.selectGym('${selectedGym.id}'); }", null)
                                    }
                                }
                            }
                        },
                        "AndroidBridge"
                    )

                    webChromeClient = object : WebChromeClient() {
                        override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                            android.util.Log.d("MapLibreJS", "${consoleMessage?.message()} [line ${consoleMessage?.lineNumber()}]")
                            return true
                        }
                    }

                    webViewClient = object : WebViewClient() {
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            view?.evaluateJavascript("if (window.updateGyms) { window.updateGyms($gymsJson); }", null)
                        }
                    }

                    loadUrl("file:///android_asset/map.html")
                    webViewRef = this
                }
            },
            update = { webView ->
                webViewRef = webView
            }
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            webViewRef?.destroy()
            webViewRef = null
        }
    }
}


