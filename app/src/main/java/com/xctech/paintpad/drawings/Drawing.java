package com.xctech.paintpad.drawings;

/**
 * Created by an.pan on 2017/5/5.
 */
import android.graphics.Canvas;

public abstract class Drawing {
    protected float startX;
    protected float startY;
    protected float stopX;
    protected float stopY;

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
}

