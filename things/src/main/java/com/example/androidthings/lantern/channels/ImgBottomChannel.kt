package com.example.androidthings.lantern.channels

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.example.androidthings.lantern.Channel

/**
 * A full black channel.
 *
 * Created by joerick on 23/01/18.
 */
class ImgBottomChannel() : Channel() {

    private lateinit var imgView: ImageView
    private val handler: Handler = Handler()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        imgView = ImageView(context)
        imgView.scaleType = ImageView.ScaleType.CENTER_INSIDE

        //imgView.setImageBitmap(ImgCenterTopSingleton.sharedBitmap)

        val view = TextView(this.context)
        view.text = "Bottom"
        view.textSize = 30F
        view.gravity = Gravity.CENTER
        view.setTextColor(Color.WHITE)
        view.setBackgroundColor(Color.BLACK)

        return imgView
    }

    override fun onChannelShow() {
        super.onChannelShow()
        Log.d("ImgTopChannel", "ImgTopShow")

        handler.post({ imgView.setImageBitmap(ImgCenterTopSingleton.sharedBitmap) })
    }

}
