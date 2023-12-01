package com.ztq.sdk.widget;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;

/**
 * 圆形的shape
 */
public class CircleDrawableShape extends ShapeDrawable {
    private int color;

    public void setRadius(int radius) {
        OvalShape shape = new OvalShape();
        shape.resize(radius, radius);
        setShape(shape);
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    protected void onDraw(Shape shape, Canvas canvas, Paint paint) {
        paint.setColor(color);
        super.onDraw(shape, canvas, paint);
    }
}
