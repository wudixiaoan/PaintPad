package com.xctech.paintpad;

/**
 * Created by an.pan on 2017/5/5.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.xctech.paintpad.drawings.Drawing;
import com.xctech.paintpad.drawings.DrawingId;
import com.xctech.paintpad.tools.BitmapUtil;
import com.xctech.paintpad.tools.Brush;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Stack;

/**
 * This is our main View class.
 */
public class PaintPad extends View {
    private float tempX, tempY, startX, startY;
    private Bitmap mBitmap = null;
    private Canvas mCanvas = null;
    private boolean isMoving = false;
    private Drawing mDrawing = null;
    private int bgColor;

    private Stack<BitmapStack> mUndoStack = UndoStack.getInstanc();
    private Stack<BitmapStack> mRedoStack = RedoStack.getInstanc();
    private Context mContext;
    private static int mMode = DrawingId.DRAWING_PATHLINE;

    /**
     * Set the shape that is drawing.
     *
     * @param drawing Which shape to drawing current.
     */
    public void setDrawing(Drawing drawing, int mode) {
        this.mDrawing = drawing;
        this.mMode = mode;
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
        mImageRect = new Rect();
        mPadding = BitmapUtil.dp2px(6, mContext);
        /**
         * Create a bitmap with the size of the screen.
         */
        /*mBitmap = Bitmap.createBitmap(screenInfo.getWidthPixels(),
                screenInfo.getHeightPixels(), Bitmap.Config.ARGB_8888);*/
        //Bitmap bgBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.screenshot).copy(Bitmap.Config.ARGB_8888, true);
        //mBitmap = Bitmap.createBitmap(bgBitmap, 0, 0, screenInfo.getWidthPixels(), screenInfo.getHeightPixels());
        /*
        * mCanvas ??????????
        * */
        if (this.mBitmap != null) {
            mCanvas = new Canvas(this.mBitmap);
            mCanvas.drawColor(Color.TRANSPARENT);
            BitmapUtil.storeTmpPic(BitmapUtil.STORE_ID, BitmapUtil.STORE_KEY + "_origin", mBitmap, getContext());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the bitmap
        //canvas.drawBitmap(mBitmap, 0, 0, new Paint(Paint.DITHER_FLAG));
        /*
        * canvas ???????
        * */
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap, null, mImageRect, new Paint(Paint.DITHER_FLAG));
            // Call the drawing's draw() method.
            if (mDrawing != null && this.isMoving == true) {
                if (mMode == DrawingId.DRAWING_PATHLINE) {
                    mDrawing.draw(mCanvas);
                } else {
                    mDrawing.draw(canvas);
                }
                //mDrawing.pathDraw(canvas, mImageRect, mImageHeight, mImageWidth);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        /*if (startX < mImageRect.left || startX > mImageRect.right || startY < mImageRect.top
                || startY > mImageRect.bottom) {
            return true;
        }*/

        /*if (x < mImageRect.left || x > mImageRect.right || y < mImageRect.top
                || y > mImageRect.bottom) {
            return true;
        }

        float ratio = (mImageRect.right - mImageRect.left)
                / (float) mImageWidth;
        x = (int) ((x - mImageRect.left) / ratio);
        y = (int) ((y - mImageRect.top) / ratio);*/

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = x;
                startY = y;
                fingerDown(x, y);
                reDraw();
                break;
            case MotionEvent.ACTION_MOVE:
                if (startX < mImageRect.left || startX > mImageRect.right || startY < mImageRect.top
                        || startY > mImageRect.bottom) {
                    return true;
                }
                fingerMove(x, y);
                reDraw();
                break;
            case MotionEvent.ACTION_UP:
                if (startX < mImageRect.left || startX > mImageRect.right || startY < mImageRect.top
                        || startY > mImageRect.bottom) {
                    return true;
                }
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
        if (mMode != DrawingId.DRAWING_PATHLINE) {
            Brush.recoveryBrushSize();
        }
        mDrawing.fingerUpWithRatio(x, y, mCanvas, mImageRect, mImageWidth);
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
        //mDrawing.fingerMove(x, y, mCanvas);
        mDrawing.fingerMoveWithRatio(x, y, mCanvas, mImageRect, mImageWidth);
    }

    /**
     * Handles the action of finger down.
     *
     * @param x coordinate
     * @param y coordinate
     */
    private void fingerDown(float x, float y) {
        this.isMoving = false;
        //mDrawing.fingerDown(x, y, mCanvas);
        if (mMode != DrawingId.DRAWING_PATHLINE) {
            Brush.ratioBrushSize(mImageRect, mImageWidth);
        }
        mDrawing.fingerDownWithRatio(x, y, mCanvas, mImageRect, mImageWidth);

        BitmapStack stack = new BitmapStack(mMode, mBitmap.copy(Bitmap.Config.ARGB_8888, true));
        mUndoStack.push(stack);
        RedoStack.clearStack();
        mRedoStack = RedoStack.getInstanc();
        Log.i("xxxx","push length = " + mUndoStack.size());
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
        BitmapUtil.deleteTmpFile(BitmapUtil.STORE_ID, BitmapUtil.STORE_KEY, mContext);
        Bitmap bitmap = BitmapUtil.getStoreTmpPic(BitmapUtil.STORE_ID, BitmapUtil.STORE_KEY + "_origin", mContext);
        if (bitmap != null) {
            mBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
            setSrcPath("/data/data/com.xctech.paintpad/files/tmp_key_origin");
            mCanvas = new Canvas(this.mBitmap);
            mCanvas.drawColor(Color.TRANSPARENT);
        }
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
            getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(fullPath))));
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

    private String inPath;
    private String outPath;

    private Rect mImageRect;
    private int mPadding;

    private int mImageWidth;
    private int mImageHeight;

    public void setSrcPath(String absPath) {
        File file = new File(absPath);
        if (file == null || !file.exists()) {
            return;
        }
        reset();
        inPath = absPath;
       /* String fileName = file.getName();
        String parent = file.getParent();
        int index = fileName.lastIndexOf(".");
        String stem = fileName.substring(0, index);
        String newStem = stem + "_mosaic";
        fileName = fileName.replace(stem, newStem);
        outPath = parent + "/" + fileName;*/

        BitmapUtil.Size size = BitmapUtil.getImageSize(inPath);
        mImageWidth = size.width;
        mImageHeight = size.height;
        mBitmap = BitmapUtil.getImage(absPath).copy(Bitmap.Config.ARGB_8888, true);

        requestLayout();
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        if (mImageWidth <= 0 || mImageHeight <= 0) {
            return;
        }

        int contentWidth = right - left;
        int contentHeight = bottom - top;
        int viewWidth = contentWidth - mPadding * 2;
        int viewHeight = contentHeight - mPadding * 2;
        float widthRatio = viewWidth / ((float) mImageWidth);
        float heightRatio = viewHeight / ((float) mImageHeight);
        float ratio = widthRatio < heightRatio ? widthRatio : heightRatio;
        int realWidth = (int) (mImageWidth * ratio);
        int realHeight = (int) (mImageHeight * ratio);

        int imageLeft = (contentWidth - realWidth) / 2;
        int imageTop = (contentHeight - realHeight) / 2;
        int imageRight = imageLeft + realWidth;
        int imageBottom = imageTop + realHeight;
        mImageRect.set(imageLeft, imageTop, imageRight, imageBottom);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE) {
            Bitmap bitmap = BitmapUtil.getStoreTmpPic(BitmapUtil.STORE_ID, BitmapUtil.STORE_KEY, mContext);
            if (bitmap != null) {
                mBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                setSrcPath("/data/data/com.xctech.paintpad/files/tmp_key");
                mCanvas = new Canvas(this.mBitmap);
                mCanvas.drawColor(Color.TRANSPARENT);
            } else {
                clearCanvas();
            }
        } else if (visibility == GONE) {
            BitmapUtil.storeTmpPic(BitmapUtil.STORE_ID, BitmapUtil.STORE_KEY, mBitmap, mContext);
        }
    }

    public boolean reset() {
        if (mBitmap != null) {
            mBitmap.recycle();
            mBitmap = null;
        }
        if (mCanvas != null) {
            mCanvas = null;
        }
        return true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.i("xxxx", "paint onDetachedFromWindow");
        reset();
    }

    public void undoPaintPad() {
        if (!mUndoStack.isEmpty()) {
            BitmapStack stack = mUndoStack.pop();
            Bitmap bitmap = stack.getDrawBitmap();
            mRedoStack.push(new BitmapStack(mMode, mBitmap.copy(Bitmap.Config.ARGB_8888, true)));
            Log.i("xxxx", "undo size = " + mUndoStack.size() + ";bitmap = " + bitmap + ";mode = " + stack.getDrawMode());
            setCurrentBitmap(bitmap);
        }
    }

    public void redoPaintPad() {
        if (!mRedoStack.isEmpty()) {
            BitmapStack stack = mRedoStack.pop();
            Bitmap bitmap = stack.getDrawBitmap();
            mUndoStack.push(new BitmapStack(mMode, mBitmap.copy(Bitmap.Config.ARGB_8888, true)));
            Log.i("xxxx", "redo size  = " + mRedoStack.size() + ";bitmap = " + bitmap+ ";mode = " + stack.getDrawMode());
            setCurrentBitmap(bitmap);
        }
    }

    private void setCurrentBitmap(Bitmap bitmap) {
        reset();
        if (bitmap == null || bitmap.isRecycled())
            return;
        if (mBitmap != null && mBitmap != bitmap) {
            mBitmap.recycle();
        }
        mBitmap = bitmap;
        mCanvas = new Canvas(this.mBitmap);
        requestLayout();
        invalidate();
    }
}
