package com.bignerdranch.android.camerafun;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EffectLab {

    private static EffectLab sEffectLab;

    private List<Effect> mEffects;

    public static EffectLab get() {
        if (sEffectLab == null) {
            sEffectLab = new EffectLab();
        }
        return sEffectLab;
    }

    private EffectLab() {
        mEffects = new ArrayList<>();
        // Add default effects
        mEffects.add(new Effect(1, 5, 5, 6, 0));
        mEffects.add(new Effect(2, 5, 5, 0, 10));
        mEffects.add(new Effect(3, 5, 0, 10, 0));
        mEffects.add(new Effect(4, 15, 5, 0, 10));
        mEffects.add(new Effect(5, 5, 10, 0, 0));
    }

    public List<Effect> getEffects() {
        return mEffects;
    }

    public Effect getEffect(UUID id) {
        for (Effect effect : mEffects) {
            if (effect.getId().equals(id)) {
                return effect;
            }
        }
        return null;
    }

    public Effect getEffect(String id) {
        for (Effect effect : mEffects) {
            if (effect.getId().toString().equals(id)) {
                return effect;
            }
        }
        return null;
    }

    public Bitmap applyEffect(String photoPath, Effect effect) {
        Bitmap src = (BitmapFactory.decodeFile(photoPath));

        int width = src.getWidth();
        int height = src.getHeight();

        int depth = effect.getDepth();
        double red = effect.getRed();
        double green = effect.getGreen();
        double blue = effect.getBlue();

        Bitmap result = Bitmap.createBitmap(width, height, src.getConfig());

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int pixel = src.getPixel(i, j);
                int pixelAlpha = Color.alpha(pixel);
                int pixelRed = Color.red(pixel);
                int pixelGreen = Color.green(pixel);
                int pixelBlue = Color.blue(pixel);

                pixelRed += depth * red;
                pixelRed = Math.min(pixelRed, 255);

                pixelGreen += depth * green;
                pixelGreen = Math.min(pixelGreen, 255);

                pixelBlue += depth * blue;
                pixelBlue = Math.min(pixelBlue, 255);

                result.setPixel(i, j, Color.argb(pixelAlpha, pixelRed, pixelGreen, pixelBlue));
            }
        }
        return result;
    }
}
