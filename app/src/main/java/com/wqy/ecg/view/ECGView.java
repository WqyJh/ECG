package com.wqy.ecg.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.wqy.ecg.R;
import com.wqy.ecg.util.ListByte;

/**
 * 以该View的左边中心点为原点，
 * 右为x轴正向， 上为y轴正向建立坐标系
 */

public class ECGView extends View {

    private static final String TAG = ECGView.class.getSimpleName();

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
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ECGView);
        lineColor = a.getColor(R.styleable.ECGView_lineColor, Color.GREEN);
        lineWidth = a.getDimension(R.styleable.ECGView_lineWidth, 2f);
        horizontalScales = a.getInt(R.styleable.ECGView_horizontalScales, 600);
        verticalScales = a.getInt(R.styleable.ECGView_verticalScales, 100);
        a.recycle();
        path = new Path();
        paint = new Paint();
        paint.setColor(lineColor);
        paint.setStrokeWidth(lineWidth);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (changed) {
            width = getWidth();
            height = getHeight();
            originX = 0f;
            originY = height / 2;
            dWidth = width / horizontalScales;
            dHeight = height / (2 * verticalScales);
            Log.d(TAG, "onLayout: width = " + width + ", height = " + height);
            Log.d(TAG, "onLayout: " + (originY - dHeight * (-128)));
        }
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        if (adapter != null) {
//            path.rewind();
//            float left = originX;
//            ListByte list = adapter.getList();
//            Log.d(TAG, "onDraw: " + (list == null ? 0 : list.drawSize()));
//            if (list != null) {
//                path.moveTo(left, originY - list.get(0) * dHeight);
//                for (int i = 1; i < list.drawSize(); i++) {
//                    left += dWidth;
//                    path.lineTo(left, originY - list.get(i) * dHeight);
//                }
//                canvas.drawPath(path, paint);
//            }
//        }
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (adapter != null) {
            ListByte list = adapter.getList();
            if (list != null) {
                byte last = list.get(0);
                float left = 0;
                for (int i = 1; i < list.size(); i++) {
                    byte b = list.get(i);
                    canvas.drawLine(left, originY - last * dHeight, left + dWidth, originY - b * dHeight, paint);
                    left += dWidth;
                    last = b;
                }
            }
        }
    }


//
//    final byte[] datas = {0, 64, 127, 64, 0, -64, -128, -64, 0};
//    {
//        for (int i = 0; i < datas.length; i++) {
//            Log.d(TAG, String.format("instance initializer: datas[%d] = %d", i, datas[i]));
//            Log.d(TAG, String.format("instance initializer: (int)datas[%d] = %d", i, (int) datas[i]));
//        }
//    }
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        float left = originX;
//        float dw = width / 8;
//        path.rewind();
//        path.moveTo(left, originY - datas[0] * dHeight);
//        for (int i = 1; i < datas.length; i++) {
//            left += dw;
//            path.lineTo(left, originY - datas[i] * dHeight);
//        }
//        canvas.drawPath(path, paint);
//    }

    //    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        path.moveTo(originX, originY);
//        path.lineTo(width / 3, 0);
//        path.lineTo(width / 3 * 2, height);
//        path.lineTo(width, originY);
//        canvas.drawPath(path, paint);
//    }

    public ECGViewAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ECGViewAdapter adapter) {
        this.adapter = adapter;
        this.invalidate();
    }
}
