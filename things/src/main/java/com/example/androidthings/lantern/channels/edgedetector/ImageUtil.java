package com.example.androidthings.lantern.channels.edgedetector;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.divae.lantern.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class ImageUtil {

    // sample rate: number of skipped pixels (horizontal and vertical)
    public static List<Pair<Integer, Integer>> extractLineEdge(Bitmap bmp, int skipX, int skipY, double tolerance, int lineHeight, boolean draw, int drawRadius) {
        if (drawRadius > skipX || drawRadius > skipY) {
            throw new IllegalArgumentException("drawradius can't be bigger than skipX/skipY");
        }

        List<Pair<Integer, Integer>> coords = new ArrayList<>();

        for (int x = 0; x < bmp.getWidth(); x = x + skipX) {
            for (int y = 0; y < bmp.getHeight(); y = y + skipY) {

                if (coordIsIn(bmp, x, y)) {

                    boolean upperIsEdge = false;
                    boolean lowerIsEdge = false;

                    // upper
                    if (coordIsIn(bmp, x, y + lineHeight)) {
                        int b1 = brightnessOf(bmp, x, y);
                        int b2 = brightnessOf(bmp, x, y + lineHeight);

                        upperIsEdge = (Math.abs(b1 - b2) > 255 * (1 - tolerance));
                    }

                    // lower
                    if (coordIsIn(bmp, x, y - lineHeight)) {
                        int b1 = brightnessOf(bmp, x, y);
                        int b2 = brightnessOf(bmp, x, y - lineHeight);

                        lowerIsEdge = (Math.abs(b1 - b2) > 255 * (1 - tolerance));
                    }

                    if (upperIsEdge && lowerIsEdge) {
                        if (draw) {
                            drawPoint(bmp, drawRadius, x, y);
                        }
                        coords.add(new Pair<>(x, y));
                    }
                }
            }
        }
        return coords;
    }

    private static void drawPoint(Bitmap bmp, int radius, int x, int y) {
        if (coordIsIn(bmp, x, y)) {
            for (int a = 0; a < radius; a++) {
                for (int b = 0; b < radius; b++) {
                    if (coordIsIn(bmp, x + a, y + b)) {
                        bmp.setPixel(x + a, y + b, Color.YELLOW);
                    }
                }
            }
        }
    }

    private static boolean coordIsIn(Bitmap bmp, int x, int y) {
        return x > 0 && y > 0 && x < bmp.getWidth() && y < bmp.getHeight();
    }

    private static int brightnessOf(Bitmap bmp, int x, int y) {
        int color = bmp.getPixel(x, y);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
        return (r + g + b) / 3;
    }

    public static Bitmap getMutableBitmap(Resources resources, int resId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inMutable = true;
        return BitmapFactory.decodeResource(resources, resId, options);
    }

    // 0-255
    public static void thresholdClear(Bitmap bmp, int threshold, int clearColor) {
        for (int x = 0; x < bmp.getWidth(); x++) {
            for (int y = 0; y < bmp.getHeight(); y++) {

                if (coordIsIn(bmp, x, y)) {
                    if (coordIsIn(bmp, x, y)) {
                        if (brightnessOf(bmp, x, y) > threshold) {
                            bmp.setPixel(x, y, clearColor);
                        }
                    }
                }
            }
        }
    }

}
