package com.xctech.paintpad.drawings;

/**
 * Created by an.pan on 2017/5/5.
 */
public class DrawingFactory {
    private Drawing mDrawing = null;

    /**
     * @param id
     *            The id of the drawing.
     * @return The Drawing instance with the id.
     */
    public Drawing createDrawing(int id) {
        switch (id) {
            case DrawingId.DRAWING_PATHLINE:
                mDrawing = new PathLine();
                break;
            case DrawingId.DRAWING_STRAIGHTLINE:
                mDrawing = new Arrow();
                break;
            case DrawingId.DRAWING_RECT:
                mDrawing = new Rectangle();
                break;
            case DrawingId.DRAWING_OVAL:
                mDrawing = new Oval();
                break;
            case DrawingId.DRAWING_CIRCLE:
                mDrawing = new Circle();
                break;
            case DrawingId.DRAWING_POINTS:
                mDrawing = new Points();
                break;
            case DrawingId.DRAWING_ERASER:
                mDrawing = new Eraser();
                break;
            case DrawingId.DRAWING_ARROW:
                mDrawing = new Arrow();
                break;
            case DrawingId.DRAWING_MOSAIC:
                mDrawing = new Mosaic();
                break;
            case DrawingId.DRAWING_PATHMOSAIC:
                mDrawing = new PathMosaic();
                break;
        }

        return mDrawing;
    }
}
