package com.wqy.ecg.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.wqy.ecg.R;
import com.wqy.ecg.util.ListByte;

/**
 * 以该View的左边中心点为原点，
 * 右为x轴正向， 上为y轴正向建立坐标系
 */

public class ECGView extends View {

    private float lineWidth = 2f;
    private int lineColor = Color.GREEN;
    private ECGViewAdapter adapter;
    private Path path;
    private Paint paint;

    /**
     * 水平正向刻度
     */
    private int horizontalScales;

    /**
     * 垂直正向刻度
     */
    private int verticalScales;

    private float originX;
    private float originY;
    private float width;
    private float height;
    private float dWidth;
    private float dHeight;

    public ECGView(Context context) {
        this(context, null);
    }

    public ECGView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ECGView);
            lineColor = a.getColor(R.styleable.ECGView_lineColor, Color.GREEN);
            lineWidth = a.getDimension(R.styleable.ECGView_lineWidth, 2f);
            horizontalScales = a.getInt(R.styleable.ECGView_horizontalScales, 600);
            verticalScales = a.getInt(R.styleable.ECGView_verticalScales, 100);
            a.recycle();
        }
        path = new Path();
        paint = new Paint();
        paint.setColor(lineColor);
        paint.setStrokeWidth(lineWidth);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            width = getWidth();
            height = getHeight();
            originX = left;
            originY = height / 2;
            dWidth = width / horizontalScales;
            dHeight = height / (2 * verticalScales);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (adapter != null) {
            float left = 0;
            ListByte list = adapter.getList();
            if (list != null) {
                path.reset();
                path.moveTo(left, list.get(0) * dHeight);
                for (int i = 1; i < list.size(); i++) {
                    left += dWidth;
                    path.lineTo(left, list.get(i) * dHeight);
                }
                canvas.drawPath(path, paint);
            }
        }
    }

    public ECGViewAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ECGViewAdapter adapter) {
        this.adapter = adapter;
        this.invalidate();
    }
}
