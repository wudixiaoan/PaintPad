package com.xctech.paintpad.drawings;

/**
 * Created by an.pan on 2017/5/5.
 */

import android.graphics.Canvas;
import android.util.Log;

import com.xctech.paintpad.tools.Brush;

/**
 * A rectangle.
 */
public class Rectangle extends Drawing {
    @Override
    public void draw(Canvas canvas) {
        Log.i("xxxx","Rectangle Drawing");
        canvas.drawRect(getStartRectX(), getStartRectY(), getStopRectX(), getStoptRectY(),
                Brush.getPen());
    }
    /*
                                |
                                |
                                |
   (stopX,stopY)(startX,startY) |(startX,stopY)(stopX,startY)
        ------------------------------------------------
   (stopX,startY)(startX,stopY) |(startX,startY)(stopX,stopY)
                                |
                                |
                                |
     */
    private float getStartRectX() {
        return this.stopX >= this.startX ? this.startX : this.stopX;
    }

    private float getStartRectY() {
        return this.stopY >= this.startY ? this.startY : this.stopY;
    }

    private float getStopRectX() {
        return this.stopX <= this.startX ? this.startX : this.stopX;
    }

    private float getStoptRectY() {
        return this.stopY <= this.startY ? this.startY : this.stopY;
    }
}
