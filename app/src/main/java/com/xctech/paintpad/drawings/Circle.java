package com.xctech.paintpad.drawings;

/**
 * Created by an.pan on 2017/5/5.
 */

import android.graphics.Canvas;

import com.xctech.paintpad.tools.Brush;

/**
 * A circle.
 */
public class Circle extends Drawing {
    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(this.startX + (this.stopX - this.startX) / 2,
                this.startY + (this.stopY - this.startY) / 2,
                Math.abs(this.startX - this.stopX) / 2, Brush.getPen());
    }
}