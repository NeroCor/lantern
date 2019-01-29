package com.example.androidthings.lantern.channels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Matrix
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.androidthings.lantern.Channel
import com.example.androidthings.lantern.hardware.Camera

/**
 * A full black channel.
 *
 * Created by joerick on 23/01/18.
 */
class ImgCenterChannel() : Channel() {
    private var mCamera: Camera = Camera.getInstance()
    private val handler: Handler = Handler()
    private var isOpen = false
    private lateinit var imgView: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        imgView = ImageView(context)
        imgView.scaleType = ImageView.ScaleType.CENTER_INSIDE

        val mCameraThread = HandlerThread("CameraBackground")
        mCameraThread.start()
        val mCameraHandler = Handler(mCameraThread.looper)

        mCamera.initializeCamera(this.activity, mCameraHandler, imageAvailableListener)

        isOpen = true

        takePicture()

        val view = TextView(this.context)
        view.text = ImgCenterTopSingleton.centerTxt;
        view.textSize = 30F
        view.gravity = Gravity.CENTER
        view.setTextColor(Color.WHITE)
        view.setBackgroundColor(Color.BLACK)

        return view
    }

    override fun onStart() {
        super.onStart()


    }

    private val imageAvailableListener = ImageReader.OnImageAvailableListener { reader ->
        val image = reader.acquireLatestImage()
        val imageBuffer = image.planes[0].buffer
        val imageBytes = ByteArray(imageBuffer.remaining())
        imageBuffer.get(imageBytes)
        image.close()
        Log.d("---imgmaker----", "wooop wooop ")
        val bitmap = getBitmapFromByteArray(imageBytes)

        ImgCenterTopSingleton.sharedBitmap = bitmap;

        //handler.post({ this.imgView.setImageBitmap(bitmap) })
    }

    private fun getBitmapFromByteArray(imageBytes: ByteArray): Bitmap {
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, Matrix(), true)
    }

    override fun onChannelShow() {
        super.onChannelShow()

        isOpen = true
        takePicture()
    }

    override fun onChannelHide() {
        super.onChannelHide()
        isOpen = false
    }

    private fun takePicture() {
        if(isOpen) {
            Log.d("---imgmaker---", "take pic")
            mCamera.takePicture()

            handler.postDelayed({ takePicture() }, 3000)
        }
    }

}
