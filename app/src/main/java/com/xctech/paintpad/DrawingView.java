package com.xctech.paintpad;

/**
 * Created by an.pan on 2017/5/4.
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Logan Garrett on 12/6/2016.
 */

public class DrawingView extends ImageView {

    private Path drawPath;
    private Paint drawPaint, canvasPaint;
    private int paintColor = 0xFF000000;
    private Canvas canvas;
    private Bitmap bitmap;

    public static final int PAINT_SIZE_SMALL = 8;
    public static final int PAINT_SIZE_MEDIUM = 16;
    public static final int PAINT_SIZE_LARGE = 24;

    private int mDrawMode = PAINT_MODE_BRUSH;
    public static final int PAINT_MODE_BRUSH = 0x01;
    public static final int PAINT_MODE_RECTANGLE = 0x02;
    public static final int PAINT_MODE_CIRCLE = 0x03;
    public static final int PAINT_MODE_ARROW = 0x04;
    public static final int PAINT_MODE_MOSAIC = 0x05;

    private static final float TOUCH_TOLERANCE = 8;

    private static List<DrawPath> savePath;
    private static List<DrawPath> deletePath;
    private DrawPath dp;
    private float mX, mY;

    private class DrawPath {
        public Path path;
        public Paint paint;
        public int mode;
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing() {
        // setting up the drawing area
        drawPaint = new Paint();

        drawPaint.setColor(paintColor);

        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(PAINT_SIZE_SMALL);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);

        savePath = new ArrayList<DrawPath>();
        deletePath = new ArrayList<DrawPath>();
    }

    public void setBrushSize(int newSize) {
        drawPaint.setStrokeWidth(newSize);
    }

    public void setPaintColor(int newColor) {
        drawPaint.setColor(newColor);
    }

    public void setPaintMode(int newMode) {
        this.mDrawMode = newMode;
    }

    public void clearCanvas() {
        bitmap = Bitmap.createBitmap(getScreenWidth(), getScreenHeight(), Bitmap.Config.ARGB_4444);
        canvas.setBitmap(bitmap);
        canvas.drawColor(Color.TRANSPARENT);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
        canvas = new Canvas(bitmap);
        canvas.drawColor(Color.TRANSPARENT);
        Log.i("INFO", "canvas has been instantiated");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, canvasPaint);
        drawPaint.setXfermode(null);
        if (drawPath != null) {
            canvas.drawPath(drawPath, drawPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // user has touched the screen!
        float touchX = e.getX();
        float touchY = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath = new Path();
                dp = new DrawPath();
                dp.paint = drawPaint;
                dp.mode = mDrawMode;
                dp.path = drawPath;
                paintTouchStart(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                paintTouchMove(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:

                paintTouchEnd(touchX, touchY);
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private void paintTouchStart(float x, float y) {
        drawPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void paintTouchMove(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(mY - y);
        switch (mDrawMode) {
            case PAINT_MODE_BRUSH:
                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                    drawPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                    mX = x;
                    mY = y;
                }
                break;
            case PAINT_MODE_RECTANGLE:
                drawPath.addRect(mX, mY, x, y, Path.Direction.CCW);
                break;
            case PAINT_MODE_CIRCLE:
                break;
            case PAINT_MODE_ARROW:
                break;
            case PAINT_MODE_MOSAIC:
                break;
        }
    }

    private void paintTouchEnd(float x, float y) {
        switch (mDrawMode) {
            case PAINT_MODE_BRUSH:
                drawPath.lineTo(mX, mY);
                canvas.drawPath(drawPath, drawPaint);
                break;
            case PAINT_MODE_RECTANGLE:
                //canvas.drawPath(drawPath, drawPaint);
                //canvas.drawRect(mX, mY, x, y, drawPaint);
                break;
            case PAINT_MODE_CIRCLE:
                break;
            case PAINT_MODE_ARROW:
                break;
            case PAINT_MODE_MOSAIC:
                break;
        }
        savePath.add(dp);
        drawPath = null;
    }

    public void undo() {
        bitmap = Bitmap.createBitmap(getScreenWidth(), getScreenHeight(),
                Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        if (savePath != null && savePath.size() > 0) {
            DrawPath path = savePath.get(savePath.size() - 1);
            deletePath.add(path);
            savePath.remove(savePath.size() - 1);
            Iterator<DrawPath> iter = savePath.iterator();
            while (iter.hasNext()) {
                DrawPath drawPath = iter.next();
                canvas.drawPath(drawPath.path, drawPath.paint);
            }
            invalidate();
        }
    }

    public void redo() {
        if (deletePath.size() > 0) {
            DrawPath dp = deletePath.get(deletePath.size() - 1);
            savePath.add(dp);
            canvas.drawPath(dp.path, dp.paint);
            deletePath.remove(deletePath.size() - 1);
            invalidate();
        }
    }

    public void save() {
        String fileUrl = Environment.getExternalStorageDirectory()
                .toString() + "/android/data/test.png";
        try {
            FileOutputStream fos = new FileOutputStream(new File(fileUrl));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

