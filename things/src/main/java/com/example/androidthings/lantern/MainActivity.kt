package com.example.androidthings.lantern

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.androidthings.lantern.channels.ErrorChannel
import com.example.androidthings.lantern.shared.ChannelConfiguration
import com.example.androidthings.lantern.shared.Direction
import java.util.*

/**
 * The main activity coordinates the display of channels, depending on the current orientation
 * and the config.
 */
class MainActivity : AppCompatActivity() {
    private val TAG = MainActivity::class.java.simpleName

    private val accelerometerObserver = Observer { _, _ -> accelerometerUpdated() }
    private val appConfigObserver = Observer { _, _ -> appConfigUpdated() }
    private val channels = mutableMapOf<Direction, Channel>()
    private var visibleChannel: Channel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        App.instance.accelerometer.addObserver(accelerometerObserver)
        App.instance.config.addObserver(appConfigObserver)

        setContentView(R.layout.main_activity_layout)
        updateChannels()
        Log.d(TAG, "Main activity created.")
    }

    override fun onDestroy() {
        super.onDestroy()
        App.instance.accelerometer.deleteObserver(accelerometerObserver)
        App.instance.config.deleteObserver(appConfigObserver)
    }

    override fun onStart() {
        super.onStart()
        updateVisibleChannel()
        Log.d(TAG, "Main activity started.")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "Main activity stopped.")
    }

    private fun accelerometerUpdated() {
        Log.d(TAG, "accelerometer direction updated to ${App.instance.accelerometer.direction}")
        updateVisibleChannel()
    }

    private fun appConfigUpdated() {
        updateChannels()
        updateVisibleChannel()
    }

    private fun updateChannels() {
        for (direction in Direction.values()) {
            val incomingChannelConfig = App.instance.config.planes[direction]!!
            val prevChannel = channels[direction]

            val needsRefresh =
                    if (prevChannel == null) {
                        true
                    } else {
                        (incomingChannelConfig != prevChannel.config)
                    }

            if (needsRefresh) {
                val newChannel = ChannelsRegistry.newChannelForConfig(incomingChannelConfig)
                channels[direction] = newChannel
                Log.i(TAG, "Channel for $direction is now $newChannel")
            }
        }
        updateVisibleChannel()
        cleanupRemovedChannels()
    }

    private fun updateVisibleChannel() {
        val newVisibleChannel = channels[App.instance.accelerometer.direction]
        val oldVisibleChannel = visibleChannel

        if (oldVisibleChannel == newVisibleChannel) {
            return
        }

        visibleChannel = newVisibleChannel

        val transaction = supportFragmentManager.beginTransaction()

        if (oldVisibleChannel != null) {
            transaction.hide(oldVisibleChannel)
            oldVisibleChannel.onChannelHide()
        }
        if (newVisibleChannel != null) {
            if (supportFragmentManager.fragments.contains(newVisibleChannel)) {
                transaction.show(newVisibleChannel)
                newVisibleChannel.onChannelShow()
            } else {
                transaction.add(R.id.viewGroup, newVisibleChannel)
            }
        }

        try {
            transaction.commitNowAllowingStateLoss()
        }
        catch (e: Exception) {
            val errorMessage = "Failed to make channel $newVisibleChannel visible.\n\n$e"
            Log.e(TAG, errorMessage, e)

            supportFragmentManager.beginTransaction()
                    .replace(R.id.viewGroup, ErrorChannel.newInstance(errorMessage))
                    .commitAllowingStateLoss()
        }
    }

    private fun cleanupRemovedChannels() {
        // removes previously hidden channels from the fragment manager
        val fragmentsToRemove = supportFragmentManager.fragments.subtract(channels.values)
        val transaction = supportFragmentManager.beginTransaction()

        for (fragment in fragmentsToRemove) {
            transaction.remove(fragment)
        }

        transaction.commitAllowingStateLoss()
    }
}
