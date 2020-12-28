package com.bignerdranch.android.camerafun;

import java.util.UUID;

public class Effect {

    private UUID mId;
    private int mNumber;
    private int mDepth;
    private double mRed;
    private double mGreen;
    private double mBlue;

    public Effect(int number, int depth, double red, double green, double blue) {
        mId = UUID.randomUUID();
        mNumber = number;
        mDepth = depth;
        mRed = red;
        mGreen = green;
        mBlue = blue;
    }

    public UUID getId() {
        return mId;
    }

    public int getDepth() {
        return mDepth;
    }

    public double getRed() {
        return mRed;
    }

    public double getGreen() {
        return mGreen;
    }

    public double getBlue() {
        return mBlue;
    }

    public int getNumber() {
        return mNumber;
    }

}
