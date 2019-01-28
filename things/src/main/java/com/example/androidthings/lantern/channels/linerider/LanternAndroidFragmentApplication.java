package com.example.androidthings.lantern.channels.linerider;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Nullable;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.example.androidthings.lantern.Channel;

public class LanternAndroidFragmentApplication extends AndroidFragmentApplication {

    private Channel channel;

    public LanternAndroidFragmentApplication() {
    }

    @SuppressLint("ValidFragment")
    public LanternAndroidFragmentApplication(Channel channel) {
        this.channel = channel;
    }

    @Nullable
    @Override
    public Context getContext() {
        return channel.getContext();
    }

}
