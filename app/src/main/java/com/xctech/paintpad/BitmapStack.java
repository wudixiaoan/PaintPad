package com.xctech.paintpad;

import android.graphics.Bitmap;

/**
 * Created by an.pan on 2017/5/19.
 */
public class BitmapStack {
    private int drawMode;
    private Bitmap drawBitmap;

    public int getDrawMode() {
        return drawMode;
    }

    public Bitmap getDrawBitmap() {
        return drawBitmap;
    }

    public BitmapStack(int drawMode, Bitmap drawBitmap) {
        this.drawMode = drawMode;
        this.drawBitmap = drawBitmap;
    }
}
