package com.example.androidthings.lantern.channels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.androidthings.lantern.Channel
import com.example.androidthings.lantern.hardware.Camera
import kotlin.math.absoluteValue
import android.R.attr.bitmap
import android.content.Context
import android.opengl.ETC1.getWidth
import android.opengl.ETC1.getHeight
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.*
import android.R.attr.start
import java.lang.reflect.Array.getLength
import android.media.MediaPlayer
import android.content.res.AssetFileDescriptor




/**
 * Makes a picture with the camera and projects it afterwards
 */
class Pomodoro : Channel() {

    private var webView: WebView? = null

    private lateinit var view: ImageView

    private val handler: Handler = Handler()
    private val historySize = 50
    private val historyValidCount = 20
    private val pixelSpacing = 5
    private val differenceHighValue = 8
    private val differenceLowValue = 2
    private val topBrigthnessHistory = mutableListOf<Int>()
    private val bottomBrigthnessHistory = mutableListOf<Int>()

    private var mCamera: Camera = Camera.getInstance()

    private var isOpen = false

    private var isButtonDown = false



    override fun onChannelShow() {
        super.onChannelShow()
        isOpen = true
        handler.post({mCamera.takePicture()})

    }

    override fun onChannelHide() {
        super.onChannelHide()
        isOpen = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        isOpen = true
        this.view = ImageView(context)
        this.view.scaleType = ImageView.ScaleType.CENTER_INSIDE

        val mCameraThread = HandlerThread("CameraBackground")
        mCameraThread.start()
        val mCameraHandler = Handler(mCameraThread.looper)

        mCamera.initializeCamera(this.activity, mCameraHandler, imageAvailableListener)

        handler.post({ mCamera.takePicture() })
        //return view

        if (webView == null) {
            webView = ATWebView(activity!!)
            loadURL("file:///android_asset/pomodoro.html")
        }
        return webView
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.clearFindViewByIdCache()
    }

    private fun loadURL(url: String) {
        webView?.loadUrl(url)
    }

    private val imageAvailableListener = ImageReader.OnImageAvailableListener { reader ->
        val image = reader.acquireLatestImage()
        val imageBuffer = image.planes[0].buffer
        val imageBytes = ByteArray(imageBuffer.remaining())
        imageBuffer.get(imageBytes)
        image.close()
        val bitmap = getBitmapFromByteArray(imageBytes)
        checkIfButtonIsDown(bitmap)
        handler.post({ this.view.setImageBitmap(bitmap) })
        if (isOpen)
            handler.post({mCamera.takePicture()})
    }

    private fun checkIfButtonIsDown(bitmap: Bitmap): Int {
        val height = bitmap.height
        val width = bitmap.width
        var r = 0
        var g = 0
        var b = 0
        var n = 0
        var topBrightnessHistoryAvr = 0
        var bottomBrightnessHistoryAvr = 0
        var brightnessTop = 0
        var brightnessBottom = 0
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        var i = 0
        while (i < pixels.size/2) {
            val color = pixels[i]
            r += Color.red(color)
            g += Color.green(color)
            b += Color.blue(color)
            n++
            i += pixelSpacing
        }
        brightnessTop = (r + b + g) / (n * 3)
        i = pixels.size/2
        r = 0
        g = 0
        b = 0
        n = 0
        while (i < pixels.size) {
            val color = pixels[i]
            r += Color.red(color)
            g += Color.green(color)
            b += Color.blue(color)
            n++
            i += pixelSpacing
        }
        brightnessBottom = (r + b + g) / (n * 3)

        if(bottomBrigthnessHistory.size > historyValidCount && topBrigthnessHistory.size > historyValidCount){
            topBrightnessHistoryAvr = topBrigthnessHistory.sum()/topBrigthnessHistory.size
            bottomBrightnessHistoryAvr = bottomBrigthnessHistory.sum()/bottomBrigthnessHistory.size

            val isTopSame = (topBrightnessHistoryAvr - brightnessTop).absoluteValue <= differenceLowValue
            val isBottomDifferent = (bottomBrightnessHistoryAvr - brightnessBottom).absoluteValue >= differenceHighValue

            Log.d(TAG, "Top: "+brightnessTop+"\t avr: "+topBrightnessHistoryAvr)
            Log.d(TAG, "Bot: "+brightnessBottom+"\t avr: "+bottomBrightnessHistoryAvr)

            if (isBottomDifferent) {
                if(!isButtonDown) {
                    isButtonDown=true
                    handler.post({ loadURL("javascript:buttonDown()") })

                }
                Log.d(TAG, "Down - Button")
            } else {
                if(isButtonDown) {
                    isButtonDown=false
                    handler.post({ loadURL("javascript:buttonUp()") })
                }
                Log.d(TAG, "Up - Button")
            }
        }

        topBrigthnessHistory.add(brightnessTop)
        if (topBrigthnessHistory.size > historySize){
            topBrigthnessHistory.remove(1)
        }

        bottomBrigthnessHistory.add(brightnessBottom)
        if (bottomBrigthnessHistory.size > historySize){
            bottomBrigthnessHistory.remove(1)
        }

        return (brightnessBottom-brightnessTop).absoluteValue
    }

    private fun getBitmapFromByteArray(imageBytes: ByteArray): Bitmap {
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, Matrix(), true)
    }

    private inner class ATWebView constructor(context: Context) : WebView(context) {
        init {
            Log.d(TAG, "ATWebView init")

            webChromeClient = object : WebChromeClient() {
                // Grant permissions for cam
                override fun onPermissionRequest(request: PermissionRequest) {
                    Log.d(TAG, "On Permission Request "+ request.toString())
                    request.grant(request.resources)
                }

            }

            webViewClient = object : WebViewClient() {

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)

                    // scroll to specified location if specified
                    if(config.settings.has("scrollTo")) {
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