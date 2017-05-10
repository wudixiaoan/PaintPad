package com.xctech.paintpad.drawings;

/**
 * Created by an.pan on 2017/5/5.
 */

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public abstract class Drawing {
    protected float startX;
    protected float startY;
    protected float stopX;
    protected float stopY;

    protected Paint mPaint;

    public void reset() {
        this.startX = 0;
        this.startY = 0;
        this.stopX = 0;
        this.stopY = 0;
    }

    /**
     * A abstract method, that all the shapes must implement.
     *
     * @param canvas
     *            A canvas to draw on.
     */
    public abstract void draw(Canvas canvas);

    public void fingerDown(float x, float y, Canvas canvas) {
        this.reset();
        this.startX = x;
        this.startY = y;
    }

    public void fingerMove(float x, float y, Canvas canvas) {
        this.stopX = x;
        this.stopY = y;
    }

    @Override
    public String toString() {
        return "Drawing{" +
                "startX=" + startX +
                ", startY=" + startY +
                ", stopX=" + stopX +
                ", stopY=" + stopY +
                '}';
    }

    public void fingerUp(float x, float y, Canvas canvas) {
        this.stopX = x;
        this.stopY = y;

        this.draw(canvas);
        this.reset();
    }

    public void fingerUpWithRatio(float x, float y, Canvas canvas, Rect rect,int width) {
        float ratioup = (rect.right - rect.left)
                / (float) width;
        this.startX = (int) ((this.startX - rect.left) / ratioup);
        this.startY = (int) ((this.startY - rect.top) / ratioup);
        this.stopX = (int) ((this.stopX - rect.left) / ratioup);
        this.stopY = (int) ((this.stopY - rect.top) / ratioup);

        this.draw(canvas);
        this.reset();

    }

    public void fingerDownWithRatio(float x, float y, Canvas canvas, Rect rect,int width) {
        fingerDown(x, y, canvas);
    }

    public void fingerMoveWithRatio(float x, float y, Canvas canvas, Rect rect,int width) {
        fingerMove(x,y,canvas);
    }
}

