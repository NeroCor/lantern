package com.example.androidthings.lantern.channels;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.divae.lantern.util.Pair;
import com.example.androidthings.lantern.Channel;
import com.example.androidthings.lantern.R;
import com.example.androidthings.lantern.channels.edgedetector.ImageUtil;
import com.example.androidthings.lantern.hardware.Camera;

import java.nio.ByteBuffer;
import java.util.List;



public class EdgeDetectorChannel extends Channel implements ImageReader.OnImageAvailableListener {

    private static final int DELAY_MS = 500;

    private final Camera camera = Camera.getInstance();

    private ImageView imageView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        HandlerThread handlerThread = new HandlerThread("BackgroundThread");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        camera.initializeCamera(getContext(), handler, this);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                camera.takePicture();
            }
        });

        // TODO show countdown to take photo
        View layout = inflater.inflate(R.layout.edge_detector, container, false);
        imageView = layout.findViewById(R.id.image_view);
        return layout;
    }

    @Override
    public void onImageAvailable(ImageReader reader) {
        final Bitmap photo = getBitmap(reader);

//        photo.eraseColor(Color.BLACK);

        ImageUtil.extractLineEdge(photo, 10, 30, 0.93, 5, true, 8);
        imageView.post(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(photo);
            }
        });

        imageView.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setImageBitmap(null);
                camera.takePicture();
            }
        }, DELAY_MS);
    }

    private Bitmap getBitmap(ImageReader reader) {
        final Image image = reader.acquireLatestImage();
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        image.close();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        return BitmapFactory.decodeByteArray(bytes,0, bytes.length,options);
    }

}
