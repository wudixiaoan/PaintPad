package com.xctech.paintpad;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.xctech.paintpad.drawings.Drawing;
import com.xctech.paintpad.drawings.DrawingFactory;
import com.xctech.paintpad.drawings.DrawingId;
import com.xctech.paintpad.tools.Brush;

public class PaintPadActivity extends Activity {

    private PaintPad mMainPad;

    private RadioGroup mRadioGroupSize;
    private RadioGroup mRadioGroupColor;
    private RadioGroup mRadioGroupMode;

    private Button btnUndo, btnRedo;

    private Button btnDrop, btnSave;

    private PaintPad mPaintPad;
    private Context mContext;
    private Drawing mDrawing;
    private Paint mPaint;
    private DrawingFactory mDrawingFactory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_pad);
        initActionbar();
        initSelectPad();
        setDefaultDrawing();
    }

    private void initActionbar() {
        btnUndo = (Button) findViewById(R.id.undo);
        btnRedo = (Button) findViewById(R.id.redo);
        btnDrop = (Button) findViewById(R.id.drop);
        btnSave = (Button) findViewById(R.id.save);
        btnUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mPaintPad.undo();
            }
        });
        btnRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // mPaintPad.redo();
            }
        });

        btnDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPaintPad.clearCanvas();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mPaintPad.save();
            }
        });
    }

    private void initSelectPad() {
        mPaintPad = (PaintPad) findViewById(R.id.main_pad);
        mRadioGroupSize = (RadioGroup) findViewById(R.id.radio_size);
        mRadioGroupColor = (RadioGroup) findViewById(R.id.radio_color);
        mRadioGroupMode = (RadioGroup) findViewById(R.id.radio_mode);
        mRadioGroupSize.check(R.id.sizebtn1);
        mRadioGroupColor.check(R.id.colorbtn1);
        mRadioGroupMode.check(R.id.paintbtn);
        mRadioGroupSize.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int paintSize = Brush.PAINT_SIZE_SMALL;
                switch (mRadioGroupSize.getCheckedRadioButtonId()) {
                    case R.id.sizebtn1:
                        paintSize = Brush.PAINT_SIZE_SMALL;
                        break;
                    case R.id.sizebtn2:
                        paintSize = Brush.PAINT_SIZE_MEDIUM;
                        break;
                    case R.id.sizebtn3:
                        paintSize = Brush.PAINT_SIZE_LARGE;
                        break;
                }
                setBrushSize(paintSize);

            }
        });
        mRadioGroupColor.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int paintColor = getResources().getColor(R.color.paint_color1);
                switch (mRadioGroupColor.getCheckedRadioButtonId()) {
                    case R.id.colorbtn1:
                        paintColor = getResources().getColor(R.color.paint_color1);
                        break;
                    case R.id.colorbtn2:
                        paintColor = getResources().getColor(R.color.paint_color2);
                        break;
                    case R.id.colorbtn3:
                        paintColor = getResources().getColor(R.color.paint_color3);
                        break;
                    case R.id.colorbtn4:
                        paintColor = getResources().getColor(R.color.paint_color4);
                        break;
                    case R.id.colorbtn5:
                        paintColor = getResources().getColor(R.color.paint_color5);
                        break;
                }
                setBrushColor(paintColor);
            }
        });

        mRadioGroupMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int paintMode = DrawingView.PAINT_MODE_BRUSH;
                switch (mRadioGroupMode.getCheckedRadioButtonId()) {
                    case R.id.paintbtn:
                        paintMode = DrawingId.DRAWING_PATHLINE;
                        break;
                    case R.id.rectanglebtn:
                        paintMode = DrawingId.DRAWING_RECT;
                        break;
                    case R.id.circlebtn:
                        paintMode = DrawingId.DRAWING_CIRCLE;
                        break;
                    case R.id.arrowbtn:
                        paintMode = DrawingId.DRAWING_STRAIGHTLINE;
                        break;
                    case R.id.mosaicbtn:
                        paintMode = DrawingId.DRAWING_OVAL;
                        break;
                }
                setWhatToDraw(paintMode);
            }
        });
    }
    /**
     * Set the default drawing
     */
    private void setDefaultDrawing() {
        mDrawingFactory = new DrawingFactory();
        mDrawing = mDrawingFactory.createDrawing(DrawingId.DRAWING_PATHLINE);
        mPaintPad.setDrawing(mDrawing);
        resetBrush();
        setBrushSize(Brush.PAINT_SIZE_SMALL);
        setBrushColor(getResources().getColor(R.color.paint_color1));
    }

    private void setWhatToDraw(int which) {
        mDrawing = mDrawingFactory.createDrawing(which);

        if (mDrawing != null) {
            mPaintPad.setDrawing(mDrawing);
        }
    }

    private void setBrushColor(int color){
        Brush.getPen().setColor(color);
    }

    private void setBrushSize(int size){
        Brush.getPen().setStrokeWidth(size);
    }

    private void resetBrush(){
        Brush.getPen().reset();
    }
}
