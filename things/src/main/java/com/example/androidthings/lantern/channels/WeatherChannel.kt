package com.example.androidthings.lantern.channels

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.androidthings.lantern.Channel


/**
 * A full black channel.
 *
 * Created by joerick on 23/01/18.
 */
class WeatherChannel() : Channel() {
    private var webView: WebView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        if (webView == null) {
            webView = ATWebView(activity!!)
            loadURL("file:///android_asset/weather_site/weather.html")
        }
        return webView
    }

    private fun loadURL(url: String) {
        webView?.loadUrl(url)
    }

    private inner class ATWebView constructor(context: Context) : WebView(context) {
        init {
            Log.d(TAG, "ATWebView init")

            webChromeClient = object : WebChromeClient() {
                // Grant permissions for cam
                override fun onPermissionRequest(request: PermissionRequest) {
                    Log.d(TAG, "On Permission Request " + request.toString())
                    request.grant(request.resources)
                }

            }

            webViewClient = object : WebViewClient() {

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)

                    // scroll to specified location if specified
                    if (config.settings.has("scrollTo")) {
                        scrollTo(0, config.settings.getInt("scrollTo"));
                    }
                }
            }

            settings.allowFileAccessFromFileURLs = true
            settings.allowFileAccess = true
            settings.allowUniversalAccessFromFileURLs = true
            settings.allowContentAccess = true
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.builtInZoomControls = true
        }
    }
}
