package com.example.androidthings.lantern.channels.edgedetector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;

import com.divae.lantern.LineRider;
import com.divae.lantern.util.Pair;
import com.example.androidthings.lantern.hardware.Camera;

import java.nio.ByteBuffer;
import java.util.List;

public class EdgeDetector implements ImageReader.OnImageAvailableListener {

    private static final int DELAY_MS = 3000;

    private final Camera camera = Camera.getInstance();

    private Context context;

    private LineRider lineRider;

    Handler handler = new Handler();

    public EdgeDetector(Context context, LineRider lineRider) {
        this.context = context;
        this.lineRider = lineRider;
        HandlerThread handlerThread = new HandlerThread("BackgroundThread");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        camera.initializeCamera(context, handler, this);
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                camera.takePicture();
            }
        });
    }

    @Override
    public void onImageAvailable(ImageReader reader) {
        final Bitmap photo = getBitmap(reader);

//        photo.eraseColor(Color.BLACK);

        final List<Pair<Integer, Integer>> coords = ImageUtil.extractLineEdge(photo, 10, 30, 0.93, 5, false, 8);

        handler.post(new Runnable() {
            @Override
            public void run() {
                lineRider.onExtracted(photo.getWidth(), photo.getHeight(), coords);
            }
        });

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
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
