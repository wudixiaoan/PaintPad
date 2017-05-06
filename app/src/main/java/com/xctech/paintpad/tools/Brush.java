package com.xctech.paintpad.tools;

/**
 * Created by an.pan on 2017/5/5.
 */
import android.graphics.Paint;

/**
 * Use Singleton mode to create Brush class
 */
public class Brush extends Paint {


    public static final int PAINT_SIZE_SMALL = 8;
    public static final int PAINT_SIZE_MEDIUM = 16;
    public static final int PAINT_SIZE_LARGE = 24;
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
        brush.setStrokeWidth(2);
    }
}