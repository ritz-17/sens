package com.example.sens

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity

class bot : AppCompatActivity() {
    private lateinit var webView: WebView
    private val URL = "https://qmint.github.io/SeSay/"

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bot)
        webView = findViewById(R.id.webView)

        webView.apply {
            loadUrl(URL)
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if(webView.canGoBack()) {
            webView.goBack()
        }else {
            super.onBackPressed()
        }
    }
}