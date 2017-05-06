package com.xctech.paintpad.drawings;

/**
 * Created by an.pan on 2017/5/5.
 */

import android.graphics.Canvas;
import android.graphics.Path;

import com.xctech.paintpad.tools.Brush;

/**
 * A straight line.
 */
public class StraightLine extends Drawing {
    @Override
    public void draw(Canvas canvas) {

        //canvas.drawLine(this.startX, this.startY, this.stopX, this.stopY,
        //        Brush.getPen());
        drawAL(canvas, this.startX, this.startY, this.stopX, this.stopY, Brush.getPen());
    }

    public void drawAL(Canvas canvas, float fx, float fy, float sx, float sy, Brush brush) {
        double H = 8;
        double L = 3.5;
        int x3 = 0;
        int y3 = 0;
        int x4 = 0;
        int y4 = 0;
        double awrad = Math.atan(L / H);
        double arraow_len = Math.sqrt(L * L + H * H);
        double[] arrXY_1 = rotateVec(sx - fx, sy - fy, awrad, true, arraow_len);
        double[] arrXY_2 = rotateVec(sx - fx, sy - fy, -awrad, true, arraow_len);
        double x_3 = sx - arrXY_1[0];
        double y_3 = sy - arrXY_1[1];
        double x_4 = sx - arrXY_2[0];
        double y_4 = sy - arrXY_2[1];
        Double X3 = new Double(x_3);
        x3 = X3.intValue();
        Double Y3 = new Double(y_3);
        y3 = Y3.intValue();
        Double X4 = new Double(x_4);
        x4 = X4.intValue();
        Double Y4 = new Double(y_4);
        y4 = Y4.intValue();
        canvas.drawLine(fx, fy, sx, sy, brush);

        Path triangle = new Path();
        triangle.reset();
        triangle.moveTo(sx, sy);
        triangle.lineTo(x3, y3);
        triangle.lineTo(x4, y4);
        triangle.close();
        canvas.drawPath(triangle, brush);
    }

    public double[] rotateVec(float px, float py, double ang, boolean isChLen, double newLen) {
        double mathstr[] = new double[2];
        double vx = px * Math.cos(ang) - py * Math.sin(ang);
        double vy = px * Math.sin(ang) + py * Math.cos(ang);
        if (isChLen) {
            double d = Math.sqrt(vx * vx + vy * vy);
            vx = vx / d * newLen;
            vy = vy / d * newLen;
            mathstr[0] = vx;
            mathstr[1] = vy;
        }
        return mathstr;
    }
}
