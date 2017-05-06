package com.xctech.paintpad.drawings;

/**
 * Created by an.pan on 2017/5/5.
 */

import android.graphics.Canvas;
import android.graphics.Paint;

import com.xctech.paintpad.tools.Brush;

/**
 * Some points.
 */
public class Points extends Drawing {
    private Paint mPaint;

    public Points() {
        mPaint = new Paint(Brush.getPen());
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawCircle(stopX, stopY, Brush.getPen().getStrokeWidth() + 1,
                mPaint);
    }

    @Override
    public void fingerDown(float x, float y, Canvas canvas) {
        canvas.drawCircle(x, y, Brush.getPen().getStrokeWidth() + 1, mPaint);
    }

    @Override
    public void fingerMove(float x, float y, Canvas canvas) {
        canvas.drawCircle(x, y, Brush.getPen().getStrokeWidth() + 1, mPaint);
    }
}