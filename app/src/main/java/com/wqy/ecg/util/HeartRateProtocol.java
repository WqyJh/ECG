package com.wqy.ecg.util;

/**
 * Created by wqy on 16-11-22.
 */

public abstract class HeartRateProtocol {
    public static final int HEART_RATE = 1;
    public static final int OTHER_DATA = 2;
    protected OnReturnDataCallback onReturnDataCallback = null;

    public HeartRateProtocol(OnReturnDataCallback callback) {
        this.onReturnDataCallback = callback;
    }

    public abstract boolean isHeartRate(int i);

    public abstract void checkData(int input);

    public void setOnReturnDataCallback(OnReturnDataCallback callback) {
        this.onReturnDataCallback = callback;
    }

    public interface OnReturnDataCallback {
        /**
         * Range 0~255
         * -1 means invalidate data
         * @param data1
         * @param data2
         */
        void onData(int type, int data1, int data2);
    }
}
