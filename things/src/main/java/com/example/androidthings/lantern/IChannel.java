package com.example.androidthings.lantern;

import android.os.Bundle;

import com.example.androidthings.lantern.shared.ChannelConfiguration;

public interface IChannel {

    ChannelConfiguration getConfig();

    ChannelConfiguration setConfig(ChannelConfiguration config);


    ChannelConfiguration getArguments();

    ChannelConfiguration setArguments(Bundle args);

}
