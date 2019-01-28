package com.example.androidthings.lantern.channels;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidthings.lantern.Channel;

public class HuyChannel extends Channel {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        TextView text = new TextView(getContext());
        text.setText("Huy: Hello Team Lantern!");
        text.setTextSize(30f);
        text.setGravity(Gravity.CENTER);
        return text;
    }

}
