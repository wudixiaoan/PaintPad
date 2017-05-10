package com.xctech.paintpad.tools;

/**
 * Created by an.pan on 2017/5/5.
 */

import android.graphics.Paint;
import android.graphics.Rect;

/**
 * Use Singleton mode to create Brush class
 */
public class Brush extends Paint {


    public static final int PAINT_SIZE_SMALL = 8;
    public static final int PAINT_SIZE_MEDIUM = 16;
    public static final int PAINT_SIZE_LARGE = 24;
    public static int realBrushSize = PAINT_SIZE_SMALL;
    /**
     * Generate the instance when the class is loaded
     */
    private static final Brush brush = new Brush();

    /**
     * Make the constructor private, to stop others to create instance by the
     * default constructor
     */
    private Brush() {
    }

    /**
     * Provide a static method that can be access by others.
     *
     * @return the single instance
     */
    public static Brush getPen() {
        return brush;
    }

    /**
     * reset the brush
     */
    public void reset() {
        brush.setAntiAlias(true);
        brush.setDither(true);
        brush.setColor(0xFF000000);
        brush.setStyle(Paint.Style.STROKE);
        brush.setStrokeJoin(Paint.Join.ROUND);
        brush.setStrokeCap(Paint.Cap.ROUND);
        brush.setStrokeWidth(realBrushSize);
    }

    public void setBrushSize(int size) {
        realBrushSize = size;
        brush.setStrokeWidth(realBrushSize);
    }

    public static void ratioBrushSize(Rect rect, int width) {
        float ratio = (rect.right - rect.left)
                / (float) width;
        brush.setStrokeWidth(realBrushSize * ratio);
    }

    public static void recoveryBrushSize() {
        brush.setStrokeWidth(realBrushSize);
    }
}