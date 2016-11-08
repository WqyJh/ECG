package com.wqy.ecg.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.wqy.ecg.R;

/**
 * Created by wqy on 16-11-7.
 */

public class BackgroundView extends View {

    private int horizontalColumns = 6;
    private int verticleColumns = 4;
    private float lineWidth = 2f;
    private int lineColor = Color.GREEN;
    private float gridWidth = 0;
    private float gridHeight = 0;
    private float width = 0;
    private float height = 0;

    private Paint linePaint;


    public BackgroundView(Context context) {
        this(context, null);
    }

    public BackgroundView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BackgroundView);
        horizontalColumns = a.getInteger(R.styleable.BackgroundView_horizontalColumns, 6);
        verticleColumns = a.getInteger(R.styleable.BackgroundView_verticleColumns, 4);
        lineWidth = a.getDimension(R.styleable.BackgroundView_lineWidth, 2f);
        lineColor = a.getColor(R.styleable.BackgroundView_lineColor, Color.GREEN);
        linePaint = new Paint();
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setColor(lineColor);
        a.recycle();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            width = getWidth();
            height = getHeight();
            gridWidth = width / horizontalColumns;
            gridHeight = height / verticleColumns;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float top = 0;
        for (int i = 0; i <= verticleColumns; i++) {
            top = i * gridHeight;
            canvas.drawLine(0, top, width, top, linePaint);
        }
        float left = 0;
        for (int i = 0; i <= horizontalColumns; i++) {
            left = i * gridWidth;
            canvas.drawLine(left, 0, left, height, linePaint);
        }
    }
}
