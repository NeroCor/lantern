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
import android.content.Context
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.*




class ColorPicker : Channel() {

    private var webView: WebView? = null

    private lateinit var view: ImageView

    private val handler: Handler = Handler()
    private val historySize = 50
    private val historyValidCount = 10
    private val pixelSpacing = 5
    private val differenceHighValue = 3
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
            loadURL("file:///android_asset/colorPicker/colorPicker.html")
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
        getCentralColor(bitmap)
        handler.post({ this.view.setImageBitmap(bitmap) })
        if (isOpen)
            handler.post({mCamera.takePicture()})
    }
    private fun getCentralColor(bitmap: Bitmap) {
        val cordX = bitmap.width/2
        val cordY = bitmap.height/2

        val centralPixel = bitmap.getPixel(cordX ,cordY).toString()
        val centralColor = String.format("#%06X", 0xFFFFFF and centralPixel.toInt())
        Log.d(TAG, "Bitmap "+bitmap.toString())
        Log.d(TAG, "CentralColor " + centralColor)
        Log.d(TAG, "Function " + centralColor)
        handler.post({ loadURL("javascript:setCentralColor(\"" + centralColor+ "\")") })
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