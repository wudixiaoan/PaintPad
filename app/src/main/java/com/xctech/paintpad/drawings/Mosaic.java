package com.xctech.paintpad.drawings;

/**
 * Created by an.pan on 2017/5/5.
 */

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;

import com.xctech.paintpad.tools.Brush;

/**
 * Track the finger's movement on the screen.
 */
public class Mosaic extends Drawing {
    private Path mPath = null;
    private Bitmap bmMosaicLayer;
    private float mX, mY;

    @Override
    public void draw(Canvas canvas) {
        canvas.drawPath(this.mPath, Brush.getPen());
    }

    @Override
    public void fingerDown(float x, float y, Canvas canvas) {
        mPath = new Path();
        mPath.moveTo(x, y);
    }

    @Override
    public void fingerMove(float x, float y, Canvas canvas) {
        mPath.lineTo(x, y);
        updatePathMosaic();
        this.draw(canvas);
    }

    @Override
    public void fingerUp(float x, float y, Canvas canvas) {
        this.draw(canvas);
    }

    private void updatePathMosaic() {

        long time = System.currentTimeMillis();
        if (bmMosaicLayer != null) {
            bmMosaicLayer.recycle();
        }
        bmMosaicLayer = Bitmap.createBitmap(screenInfo.getWidthPixels(),
                screenInfo.getHeightPixels(),Bitmap.Config.ARGB_8888);

        Bitmap bmTouchLayer = Bitmap.createBitmap(mImageWidth, mImageHeight,
                Bitmap.Config.ARGB_8888);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setPathEffect(new CornerPathEffect(10));
        paint.setStrokeWidth(mPathWidth);
        paint.setColor(Color.BLUE);

        Canvas canvas = new Canvas(bmTouchLayer);

        for (Path path : mTouchPaths) {
            canvas.drawPath(path, paint);
        }

        paint.setColor(Color.TRANSPARENT);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        for (Path path : mErasePaths) {
            canvas.drawPath(path, paint);
        }

        canvas.setBitmap(bmMosaicLayer);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawBitmap(bmCoverLayer, 0, 0, null);

        paint.reset();
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(bmTouchLayer, 0, 0, paint);
        paint.setXfermode(null);
        canvas.save();

        bmTouchLayer.recycle();
        Log.d(TAG, "updatePathMosaic " + (System.currentTimeMillis() - time));
    }
}
