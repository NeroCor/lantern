package com.example.androidthings.lantern.channels

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ImageReader
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import com.example.androidthings.lantern.hardware.Camera

object ImgCenterTopSingleton
{
    init {
        Log.d("ImgCenterTop", "init Img centertop singelton")
    }

    var centerTxt = "Turn me down"
    var sharedBitmap:Bitmap = Bitmap.createBitmap(1,1,Bitmap.Config.RGB_565)
}