package com.rafi.movementplan

import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.SystemBarStyle
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    private lateinit var webView: WebView
    private var pageReady = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(SCRIM, SCRIM),
            navigationBarStyle = SystemBarStyle.light(SCRIM, SCRIM),
        )
        setContentView(R.layout.activity_main)
        webView = findViewById(R.id.webView)
        ViewCompat.setOnApplyWindowInsetsListener(webView) { view, insets ->
            val bars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or
                    WindowInsetsCompat.Type.displayCutout() or
                    WindowInsetsCompat.Type.ime(),
            )
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
        if (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0) {
            WebView.setWebContentsDebuggingEnabled(true)
        }
        webView.setBackgroundColor(SCRIM)
        webView.webChromeClient = WebChromeClient()
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                pageReady = true
                view?.evaluateJavascript(BACK_STACK_SCRIPT, null)
            }

            override fun shouldOverrideUrlLoading(
                view: WebView,
                request: WebResourceRequest,
            ): Boolean {
                return request.url.scheme != "file"
            }
        }
        webView.settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            allowFileAccess = true
            allowContentAccess = false
            builtInZoomControls = false
            displayZoomControls = false
            setSupportZoom(false)
            loadWithOverviewMode = true
            useWideViewPort = true
            mediaPlaybackRequiresUserGesture = true
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            webView.settings.isAlgorithmicDarkeningAllowed = false
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            @Suppress("DEPRECATION")
            webView.settings.forceDark = WebSettings.FORCE_DARK_OFF
        }
        val restored = savedInstanceState?.let { webView.restoreState(it) } != null
        if (!restored) {
            webView.loadUrl(ASSET_URL)
        }
        onBackPressedDispatcher.addCallback(this) {
            if (!pageReady) {
                finish()
                return@addCallback
            }
            webView.evaluateJavascript(BACK_QUERY) { raw ->
                if (raw != "true") finish()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        webView.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        webView.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onDestroy() {
        if (::webView.isInitialized) {
            (webView.parent as? ViewGroup)?.removeView(webView)
            webView.destroy()
        }
        super.onDestroy()
    }

    companion object {
        private const val SCRIM = 0xFFF4F1EB.toInt()
        private const val ASSET_URL = "file:///android_asset/movement_plan.html"
        private const val BACK_STACK_SCRIPT = """
            (function () {
              if (window.__movementBack) return;
              var stack = ['home'];
              var applying = false;
              var nav = document.getElementById('nav');
              if (!nav) {
                window.__movementBack = function () { return false; };
                return;
              }
              nav.addEventListener('click', function (e) {
                var tab = e.target && e.target.dataset && e.target.dataset.tab;
                if (!tab || applying) return;
                if (stack[stack.length - 1] !== tab) stack.push(tab);
                if (stack.length > 32) stack.shift();
              }, true);
              window.__movementBack = function () {
                if (stack.length <= 1) return false;
                stack.pop();
                var prev = stack[stack.length - 1];
                var btn = document.querySelector('nav button[data-tab="' + prev + '"]');
                if (!btn) return false;
                applying = true;
                try { btn.click(); } finally { applying = false; }
                return true;
              };
            })();
        """
        private const val BACK_QUERY =
            "(function(){try{return !!(window.__movementBack&&window.__movementBack());}catch(e){return false;}})()"
    }
}
