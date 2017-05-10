package com.xctech.paintpad.drawings;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by an.pan on 2017/5/9.
 */
public class PathMosaic extends Drawing {
    private Path mTouchPath;

    private List<Path> mTouchPaths;
    Bitmap bmMosaicLayer;
    Bitmap bmTouchLayer;
    public PathMosaic() {
        mTouchPaths = new ArrayList<Path>();
    }

    @Override
    public void draw(Canvas canvas) {
        //canvas.drawPath(this.mTouchPath, Brush.getPen());
    }

    @Override
    public void fingerDown(float x, float y, Canvas canvas) {
        super.fingerDown(x, y, canvas);
        mTouchPath = new Path();
        mTouchPaths.add(mTouchPath);
        mTouchPath.moveTo(x, y);
        bmMosaicLayer = Bitmap.createBitmap(480, 854,
                Bitmap.Config.ARGB_8888);

        bmTouchLayer = Bitmap.createBitmap(480, 854,
                Bitmap.Config.ARGB_8888);
    }

    @Override
    public void fingerMove(float x, float y, Canvas canvas) {
        super.fingerMove(x, y, canvas);
        mTouchPath.lineTo(x, y);
        updatePathMosaic();

    }

    private void updatePathMosaic() {
        long time = System.currentTimeMillis();

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(true);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setPathEffect(new CornerPathEffect(10));
        paint.setStrokeWidth(20);
        paint.setColor(Color.BLUE);

        Canvas canvas = new Canvas(bmTouchLayer);

        for (Path path : mTouchPaths) {
            canvas.drawPath(path, paint);
        }

        paint.setColor(Color.TRANSPARENT);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        canvas.setBitmap(bmMosaicLayer);

        paint.reset();
        paint.setAntiAlias(true);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawBitmap(bmTouchLayer, 0, 0, paint);
        paint.setXfermode(null);
        canvas.save();

        //bmTouchLayer.recycle();
        Log.d("xxxx", "updatePathMosaic " + (System.currentTimeMillis() - time));
    }
}
