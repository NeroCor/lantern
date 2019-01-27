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


/**
 * Makes a picture with the camera and projects it afterwards
 */
class Pomodoro : Channel() {

    val TAG = this::class.java.simpleName

    private lateinit var view: ImageView

    private val handler: Handler = Handler()

    private var mCamera: Camera = Camera.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        this.view = ImageView(context)
        this.view.scaleType = ImageView.ScaleType.CENTER_INSIDE

        val mCameraThread = HandlerThread("CameraBackground")
        mCameraThread.start()
        val mCameraHandler = Handler(mCameraThread.looper)

        mCamera.initializeCamera(this.activity, mCameraHandler, imageAvailableListener)

        handler.postDelayed({ mCamera.takePicture() }, 3000)
        return view
    }

    private val imageAvailableListener = ImageReader.OnImageAvailableListener { reader ->
        Log.d(TAG, "ImageAvailable!")
        val image = reader.acquireLatestImage()
        val imageBuffer = image.planes[0].buffer
        val imageBytes = ByteArray(imageBuffer.remaining())
        imageBuffer.get(imageBytes)
        image.close()
        val bitmap = getBitmapFromByteArray(imageBytes)
        //calculateLuminanceDifference(bitmap)
        handler.post({ this.view.setImageBitmap(bitmap) })
        handler.post({mCamera.takePicture()})
    }

    private fun calculateLuminanceDifference(bitmap: Bitmap): Float {
        val height = bitmap.height
        val width = bitmap.width
        val pixelCount = height*width
        var upperValue = 0.0f
        var lowerValue = 0.0f

        for(x in 0..(width-1)){
            for(y in 0..(height/2)){
                upperValue += (Color.valueOf(bitmap.getPixel(x,y)).luminance())/(pixelCount/2)
            }
            for(y in ((height/2)+1)..(height-1)){
                lowerValue += (Color.valueOf(bitmap.getPixel(x,y)).luminance())/(pixelCount/2)
            }
        }

        Log.d(TAG, ("Upper"+upperValue))
        Log.d(TAG, ("Lower"+lowerValue))
        Log.d(TAG, ("difference"+(upperValue - lowerValue).absoluteValue))

        return (upperValue - lowerValue).absoluteValue
    }

    private fun getBitmapFromByteArray(imageBytes: ByteArray): Bitmap {
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, Matrix(), true)
    }
}