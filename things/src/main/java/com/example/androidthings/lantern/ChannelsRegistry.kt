package com.example.androidthings.lantern

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.androidthings.lantern.channels.*
import com.example.androidthings.lantern.channels.nowplaying.NowPlayingChannel
import com.example.androidthings.lantern.channels.spaceporthole.SpacePortholeChannel
import com.example.androidthings.lantern.shared.ChannelConfiguration
import com.example.androidthings.lantern.shared.ChannelInfo


/**
 * Stores a the available channels with corresponding info for them.
 *
 * Created by joerick on 25/01/18.
 */
object ChannelsRegistry {
    val channelsWithInfo = arrayOf<Pair<() -> Fragment, ChannelInfo>>(
            Pair(::CalendarChannel, ChannelInfo(
                    "calendar-clock",
                    "Augmented clock",
                    "Projects your Google calendar events around a real-world clock",
                    Uri.parse("android.resource://com.example.androidthings.lantern/drawable/banner_clock"),
                    customizable = true
            )),
            // default
            Pair(::BahnAnzeigeChannel, ChannelInfo(
                    "bahn-anzeige",
                    "Bahn Anzeige",
                    "Shows the Departures of a DB Train Station",
                    Uri.parse("android.resource://com.example.androidthings.lantern/drawable/bahn"),
                    customizable = true
            )),
            Pair(::ImgBottomChannel, ChannelInfo(
                    "img-bottom",
                    "Img Bottom Channel",
                    "Shows the center generated img",
                    Uri.parse("android.resource://com.example.androidthings.lantern/drawable/bahn"),
                    customizable = true
            )),
            Pair(::ImgCenterChannel, ChannelInfo(
                    "img-center",
                    "Img Center Channel",
                    "Animates the user to turn the latern and get surprised with an image of himself",
                    Uri.parse("android.resource://com.example.androidthings.lantern/drawable/bahn"),
                    customizable = true
            )),
            Pair(::WeatherChannel, ChannelInfo(
                    "weather-biergarten-anzeige",
                    "Weather Screen",
                    "Shows the actual weather and at some point an biergarden around with a nice background.",
                    Uri.parse("android.resource://com.example.androidthings.lantern/drawable/mood_biergarten_low"),
                    customizable = true
            )),
            Pair(::ScreenShot, ChannelInfo(
                    "ScreenShot",
                    "Take a picture",
                    "Take a picture of something that is under lantern and project it to any surface.",
                    Uri.parse("android.resource://com.example.androidthings.lantern/drawable/banner_screenshot")
                    )),
            Pair(::Pomodoro, ChannelInfo(
                    "pomodoro",
                    "Runs a Pomodoro Timer",
                    "Work with Pomodoro",
                    Uri.parse("android.resource://com.example.androidthings.lantern/drawable/pomodoro")
            )),
            Pair(::ColorPicker, ChannelInfo(
                    "ColorPicker",
                    "ColorPicker",
                    "Get the Color Code of an Object",
                    Uri.parse("android.resource://com.example.androidthings.lantern/drawable/color")
            )),
            Pair(::NowPlayingChannel, ChannelInfo(
                    "now-playing",
                    "Now playing",
                    "Displays song information for whatever's playing through your Cast-enabled speaker",
                    Uri.parse("android.resource://com.example.androidthings.lantern/drawable/banner_now_playing"),
                    customizable = true
            )),
            Pair(::AmbientWeatherChannel, ChannelInfo(
                    "ambient-weather",
                    "Weather caustics",
                    "Ambient water reflections react to open weather data for a chosen location",
                    Uri.parse("android.resource://com.example.androidthings.lantern/drawable/banner_weather"),
                    customizable = true,
                    rotationDisabled = true
                    )),
            Pair(::SpacePortholeChannel, ChannelInfo(
                    "space-porthole",
                    "Space porthole",
                    "Explore the galaxy with this virtual telescope. Look out for Orion!",
                    Uri.parse("android.resource://com.example.androidthings.lantern/drawable/banner_space"),
                    customizable = true,
                    rotationDisabled = true
            )),
            Pair(::LampChannel, ChannelInfo(
                    "lamp",
                    "Spotlight",
                    "Bring a little drama to your desktop",
                    Uri.parse("android.resource://com.example.androidthings.lantern/drawable/banner_spotlight")
            )),
            Pair(::WebViewChannel, ChannelInfo(
                    "webview",
                    "Web view",
                    "Load a URL for your favourite site, a recipe or a fun web app you’re building",
                    Uri.parse("android.resource://com.example.androidthings.lantern/drawable/banner_web_page"),
                    customizable = true
            )),
            Pair(::BlankChannel, ChannelInfo(
                    "blank",
                    "Blank",
                    "Hide projections for this direction",
                    Uri.parse("android.resource://com.example.androidthings.lantern/drawable/banner_blank")
            )),
            Pair(::BatSignalChannel, ChannelInfo(
                    "bat-signal",
                    "Android-Signal",
                    "Summon Android when troubles about…",
                    Uri.parse("android.resource://com.example.androidthings.lantern/drawable/banner_bat_signal")
            )),
            Pair(::InfoChannel, ChannelInfo(
                    "info",
                    "Lantern Info",
                    "Projects some useful information about the projector",
                    Uri.parse("android.resource://com.example.androidthings.lantern/drawable/banner_info")
            )),
            Pair(::RedLightChannel, ChannelInfo(
                    "red-light-channel",
                    "Red Light Channel",
                    "Ruhe jetzt"
            )),
            Pair(::HuyChannel, ChannelInfo(
                    "huy-channel",
                    "Huys Channel",
                    "The greatest Channel!"
            )),
            Pair(::LineRiderChannel, ChannelInfo(
                    "linerider",
                    "LineRider Channel",
                    "Let's ride some lines!"
            )),
            Pair(::EdgeDetectorChannel, ChannelInfo(
                    "edge-detector",
                    "Edge Detector Channel",
                    "Detects and shows upper edges on camera"
            ))
    )

    val channelsInfo = channelsWithInfo.map { it.second }

    fun newChannelForConfig(config: ChannelConfiguration): Fragment {
        val args = Bundle()
        args.putParcelable(Channel.ARG_CONFIG, config)
        var channel: Fragment? = null
        var rotationDisabled = false

        for ((channelConstructor, info) in ChannelsRegistry.channelsWithInfo) {
            if (info.id == config.type) {
                channel = channelConstructor()
                rotationDisabled = info.rotationDisabled
                break
            }
        }

        if (rotationDisabled) {
            args.putBoolean(Channel.ARG_ROTATION_DISABLED, rotationDisabled)
        }

        if (channel == null) {
            channel = ErrorChannel()
            args.putString(ErrorChannel.ARG_MESSAGE, "Unknown channel type '${config.type}'")
        }

        channel.arguments = args
        return channel
    }
}
