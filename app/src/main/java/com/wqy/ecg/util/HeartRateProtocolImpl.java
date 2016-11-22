package com.wqy.ecg.util;

import static com.wqy.ecg.util.Common.intToByte;

/**
 * Created by wqy on 16-11-22.
 */

public class HeartRateProtocolImpl extends HeartRateProtocol {

    private int[] stack = {-1, -1};
    private boolean flag1 = false;
    private boolean flag2 = false;

    public HeartRateProtocolImpl(OnReturnDataCallback callback) {
        super(callback);
    }

    private void reset() {
        flag1 = false;
        flag2 = false;
        stack[0] = -1;
        stack[1] = -1;
    }

    @Override
    public boolean isHeartRate(int i) {
        if (flag1 && flag2) {
            reset();
            return true;
        }
        if (flag1) {
            if (i == 255) {
                stack[1] = i;
                flag2 = true;
            } else {
                if (onReturnDataCallback != null) {
                    onReturnDataCallback.onData(stack[0], -1);
                }
                reset();
            }
        } else {
            if (i == 0) {
                stack[0] = i;
                flag1 = true;
                flag2 = false;
            }
        }

        return false;
    }
}
