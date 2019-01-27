package com.example.androidthings.lantern.channels;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidthings.lantern.Channel;
import com.example.androidthings.lantern.R;

public class RedLightChannel extends Channel {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_red_light, container, false);
    }

}
