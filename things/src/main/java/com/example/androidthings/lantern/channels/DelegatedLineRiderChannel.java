package com.example.androidthings.lantern.channels;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.divae.lantern.LineRider;
import com.example.androidthings.lantern.Channel;
import com.example.androidthings.lantern.channels.linerider.LanternAndroidFragmentApplication;

import org.jetbrains.annotations.NotNull;

public class DelegatedLineRiderChannel extends Channel {

    private final LanternAndroidFragmentApplication app = new LanternAndroidFragmentApplication(this);

    @Override
    public void onViewCreated(@NotNull View view, @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        app.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        app.onAttach(context);
    }

    @Override
    public void onDetach() {
        app.onDetach();
        super.onDetach();
    }

    @Override
    public void onPause() {
        app.onDetach();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        app.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        app.onActivityResult(requestCode, resultCode, data);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        return app.initializeForView(new LineRider(), config);
    }

}
