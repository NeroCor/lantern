package com.example.androidthings.lantern.channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.badlogic.gdx.backends.android.AndroidFragmentApplication
import com.divae.lantern.LineRider
import com.example.androidthings.lantern.shared.ChannelConfiguration
import com.example.androidthings.lantern.shared.Rotation

/**
 * Projects a simple message.
 *
 * Config parameters:
 *   - "message"
 *         The message to display.
 */
class LineRiderChannel : AndroidFragmentApplication() {

    val config: ChannelConfiguration by lazy {
        arguments!!.getParcelable<ChannelConfiguration>(ARG_CONFIG)
    }

    private val rotationDisabled: Boolean by lazy {
        arguments!!.getBoolean(ARG_ROTATION_DISABLED)
    }

    companion object {
        const val ARG_CONFIG = "config"
        const val ARG_ROTATION_DISABLED = "rotationDisabled"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!rotationDisabled) {
            view.rotation = when(config.rotation) {
                Rotation.LANDSCAPE -> 0F
                Rotation.LANDSCAPE_UPSIDE_DOWN -> 180F
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val config = AndroidApplicationConfiguration()
        return initializeForView(LineRider(), config)
    }

}
