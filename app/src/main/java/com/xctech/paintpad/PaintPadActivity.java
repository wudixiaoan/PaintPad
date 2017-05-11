package com.xctech.paintpad;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.xctech.paintpad.drawings.Drawing;
import com.xctech.paintpad.drawings.DrawingFactory;
import com.xctech.paintpad.drawings.DrawingId;
import com.xctech.paintpad.tools.BitmapUtil;
import com.xctech.paintpad.tools.Brush;

public class PaintPadActivity extends Activity {
    private RadioGroup mRadioGroupSize;
    private RadioGroup mRadioGroupColor;
    private RadioGroup mRadioGroupMode;

    private Button btnUndo, btnRedo;

    private Button btnDrop, btnSave;

    private PaintPad mPaintPad;
    private MosaicView mMosaicView;
    private Drawing mDrawing;
    private DrawingFactory mDrawingFactory;

    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

    private boolean isPaint = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_pad);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            if (i != PackageManager.PERMISSION_GRANTED) {
                startRequestPermission();
            }
        }
        initActionbar();
        initSelectPad();
        setDefaultDrawing();
        initData();
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
                if (isPaint) {
                    mPaintPad.clearCanvas();
                } else {
                    mMosaicView.clearCanvas();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPaint) {
                    mPaintPad.saveToSdcard();
                } else {
                    mMosaicView.saveToSdcard();
                }
                finish();
            }
        });
    }

    private void initSelectPad() {
        mPaintPad = (PaintPad) findViewById(R.id.main_pad);
        mMosaicView = (MosaicView) findViewById(R.id.mosaic_main_pad);
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
                        paintMode = DrawingId.DRAWING_ARROW;
                        break;
                    case R.id.mosaicbtn:
                        paintMode = DrawingId.DRAWING_PATHMOSAIC;
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
        mPaintPad.setDrawing(mDrawing, DrawingId.DRAWING_PATHLINE);
        //mPaintPad.setSrcPath(Environment.getExternalStorageDirectory() + "/Screenshot.png");
        resetBrush();
        setBrushSize(Brush.PAINT_SIZE_SMALL);
        setBrushColor(getResources().getColor(R.color.paint_color1));
    }

    private void setWhatToDraw(int which) {
        mDrawing = mDrawingFactory.createDrawing(which);
        if (which == DrawingId.DRAWING_PATHMOSAIC) {
            mPaintPad.setVisibility(View.GONE);
            mMosaicView.setVisibility(View.VISIBLE);
            isPaint = false;
            return;
        } else {
            if (!isPaint) {
                mMosaicView.setVisibility(View.GONE);
                mPaintPad.setVisibility(View.VISIBLE);
                isPaint = true;
            }
        }
        if (mDrawing != null) {
            mPaintPad.setDrawing(mDrawing, which);
        }
    }

    private void setBrushColor(int color) {
        Brush.getPen().setColor(color);
    }

    private void setBrushSize(int size) {
        Brush.getPen().setBrushSize(size);
    }

    private void resetBrush() {
        Brush.getPen().reset();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BitmapUtil.deleteTmpFile(BitmapUtil.STORE_ID, BitmapUtil.STORE_KEY, this);
        BitmapUtil.deleteTmpFile(BitmapUtil.STORE_ID, BitmapUtil.STORE_KEY + "_origin", this);
    }

    private void initData() {
        Intent intent = getIntent();
        boolean mIsFromInner = intent.getBooleanExtra("extra_from_inner", false);
        Uri target = null;
        String action = intent.getAction();
        if (mIsFromInner) {
            target = intent.getData();
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            if (i == PackageManager.PERMISSION_GRANTED) {
                String path = BitmapUtil.getRealFilePath(getApplicationContext(), target);
                mPaintPad.setSrcPath(path);
            }
        }
    }

    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 321);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i("xxxx", "onRequestPermissionsResult -- " );
        int i = ContextCompat.checkSelfPermission(this, permissions[0]);
        if (i == PackageManager.PERMISSION_GRANTED) {
            initActionbar();
            initSelectPad();
            setDefaultDrawing();
            initData();
        }
        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    Log.i("xxxx", "onRequestPermissionsResult -- " + b);
                    if (b) {
                        finish();
                    }
                }
            }
        }
    }
}