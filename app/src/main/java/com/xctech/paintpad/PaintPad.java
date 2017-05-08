package com.xctech.paintpad;

/**
 * Created by an.pan on 2017/5/5.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.xctech.paintpad.drawings.Drawing;
import com.xctech.paintpad.tools.ScreenInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * This is our main View class.
 */
public class PaintPad extends View {
    private float tempX, tempY;
    private Bitmap mBitmap = null;
    private Canvas mCanvas = null;
    private boolean isMoving = false;
    private Drawing mDrawing = null;
    private int bgColor;

    private Context mContext;

    /**
     * Set the shape that is drawing.
     *
     * @param drawing Which shape to drawing current.
     */
    public void setDrawing(Drawing drawing) {
        this.mDrawing = drawing;
    }

    public PaintPad(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        // Get the information about the screen.
        this.isMoving = false;
    }

    public PaintPad(Context context) {
        super(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        ScreenInfo screenInfo = new ScreenInfo((Activity) mContext);

        /**
         * Create a bitmap with the size of the screen.
         */
        /*mBitmap = Bitmap.createBitmap(screenInfo.getWidthPixels(),
                screenInfo.getHeightPixels(), Bitmap.Config.ARGB_8888);*/
        Bitmap bgBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.screenshot).copy(Bitmap.Config.ARGB_8888, true);
        mBitmap = Bitmap.createBitmap(bgBitmap, 0, 0, screenInfo.getWidthPixels(), screenInfo.getHeightPixels());
        mCanvas = new Canvas(this.mBitmap);
        mCanvas.drawColor(Color.TRANSPARENT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the bitmap
        canvas.drawBitmap(mBitmap, 0, 0, new Paint(Paint.DITHER_FLAG));

        // Call the drawing's draw() method.
        if (mDrawing != null && this.isMoving == true) {
            mDrawing.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                fingerDown(x, y);
                reDraw();
                break;
            case MotionEvent.ACTION_MOVE:
                fingerMove(x, y);
                reDraw();
                break;
            case MotionEvent.ACTION_UP:
                fingerUp(x, y);
                reDraw();
                break;
        }

        return true;
    }

    /**
     * Refresh the view, the view's onDraw() method will be called.
     */
    private void reDraw() {
        invalidate();
    }

    /**
     * Handles the action of finger up.
     *
     * @param x coordinate
     * @param y coordinate
     */
    private void fingerUp(float x, float y) {
        this.tempX = 0;
        this.tempY = 0;

        mDrawing.fingerUp(x, y, mCanvas);
        this.isMoving = false;
    }

    /**
     * Handles the action of finger Move.
     *
     * @param x coordinate
     * @param y coordinate
     */
    private void fingerMove(float x, float y) {
        this.tempX = x;
        this.tempY = y;
        this.isMoving = true;

        mDrawing.fingerMove(x, y, mCanvas);
    }

    /**
     * Handles the action of finger down.
     *
     * @param x coordinate
     * @param y coordinate
     */
    private void fingerDown(float x, float y) {
        this.isMoving = false;
        mDrawing.fingerDown(x, y, mCanvas);
    }

    /**
     * Check the sdcard is available or not.
     */
    public void saveBitmap() {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            saveToSdcard();
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            /*Toast.makeText(this.mContext,
                    getResources().getString(R.string.tip_sdcard_is_read_only),
                    Toast.LENGTH_LONG).show();*/
        } else {
            /*Toast.makeText(
                    this.mContext,
                    getResources().getString(
                            R.string.tip_sdcard_is_not_available),
                    Toast.LENGTH_SHORT).show();*/
        }
    }

    public void changeBgColor(int color) {
        this.mCanvas.drawColor(color);
        this.reDraw();
    }

    /**
     * Clear the drawing in the canvas.
     */
    public void clearCanvas() {
        //this.mCanvas.drawColor(getResources().getColor(R.color.color_default_bg));
        this.reDraw();
    }

    /**
     * Save the bitmap to sdcard.
     */
    public void saveToSdcard() {
        File sdcard_path = Environment.getExternalStorageDirectory();
        String myFloder = "DCIM";/*getResources().getString(
                R.string.folder_name_in_sdcard);*/
        File paintpad = new File(sdcard_path + "/" + myFloder + "/");
        try {
            if (!paintpad.exists()) {
                paintpad.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set format
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss");

        // Get date
        Date date = Calendar.getInstance().getTime();

        // Get formatted time stamp
        String timeStamp = format.format(date);

        String suffixName = ".png";

        String fullPath = "";
        fullPath = sdcard_path + "/" + myFloder + "/" + timeStamp + suffixName;
        try {
            Toast.makeText(this.mContext,
                    getResources().getString(R.string.tip_save_to) + fullPath,
                    Toast.LENGTH_LONG).show();
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100,
                    new FileOutputStream(fullPath));
        } catch (FileNotFoundException e) {
            Toast.makeText(
                    this.mContext,
                    getResources().getString(R.string.tip_sava_failed)
                            + fullPath, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
