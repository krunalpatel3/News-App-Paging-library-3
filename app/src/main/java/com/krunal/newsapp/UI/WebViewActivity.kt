package com.krunal.newsapp.UI

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.krunal.newsapp.Globel.Utility.Companion.URL
import com.krunal.newsapp.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
    lateinit var binding: ActivityWebViewBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val url = intent.getStringExtra(URL)

        val webSettings: WebSettings = binding.webView.settings
        webSettings.javaScriptEnabled = true

        binding.progressBarSyncing.show()
        binding.webView.loadUrl(url?:"")

        binding.webView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                binding.progressBarSyncing.hide()
                binding.progressBarSyncing.visibility = View.GONE
            }
        }
    }
}