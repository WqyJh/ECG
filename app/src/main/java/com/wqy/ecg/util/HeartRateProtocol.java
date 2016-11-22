package com.wqy.ecg.util;

/**
 * Created by wqy on 16-11-22.
 */

public abstract class HeartRateProtocol {
    protected OnReturnDataCallback onReturnDataCallback = null;

    public HeartRateProtocol(OnReturnDataCallback callback) {
        this.onReturnDataCallback = callback;
    }

    public abstract boolean isHeartRate(int i);

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
        void onData(int data1, int data2);
    }
}
