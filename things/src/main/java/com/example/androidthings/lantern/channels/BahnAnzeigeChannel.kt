package com.example.androidthings.lantern.channels

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.androidthings.lantern.Channel
import com.example.androidthings.lantern.R
import kotlinx.android.synthetic.*
import android.graphics.BitmapFactory



/**
 * Shows a DB Train info using the Android WebView.
 *
 * Config parameters:
 *   - "bhfid"
 *       The Id of the Station to display.
 *
 * Created by dingxu on 2/5/18.
 */
class BahnAnzeigeChannel: Channel() {
    private var webView: WebView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        if (webView == null) {
            webView = ATWebView(activity!!)
            loadURL(config.settings.optString("bhfid"))
        }
        return webView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.clearFindViewByIdCache()
    }

    private fun loadURL(bhfid: String = "MH") {

        webView?.loadUrl("https://iris.noncd.db.de/wbt/js/index.html?typ=ab&bhf="+( if (bhfid.length > 0) bhfid else "MH")+"&zugtyp=&platform=&bhfname=&style=ab&via=1&impressum=0&lang=de&SecLang=&zeilen=8&paging=&pagingdauer=&impressum=0")
    }

    private inner class ATWebView constructor(context: Context) : WebView(context) {
        init {
            webChromeClient = WebChromeClient()

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)

                    // scroll to specified location if specified
                    if(config.settings.has("scrollTo")) {
                        scrollTo(0, config.settings.getInt("scrollTo"));
                    }
                }
            }

            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.builtInZoomControls = true
        }
    }
}