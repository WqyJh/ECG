package com.wqy.ecg;


import com.wqy.ecg.util.HeartRateProtocol;
import com.wqy.ecg.util.HeartRateProtocolImpl;

import org.junit.Test;

import static org.junit.Assert.*;
import static com.wqy.ecg.util.Common.intToByte;

/**
 * Created by wqy on 16-11-22.
 */
public class HeartRateProtocolImplTest {

    @Test
    public void checkValidateData() {
        int[] datas = {0, 255, 111};
        HeartRateProtocol protocol = new HeartRateProtocolImpl(null);
        assertFalse(protocol.isHeartRate(datas[0]));
        assertFalse(protocol.isHeartRate(datas[1]));
        assertTrue(protocol.isHeartRate(datas[2]));

        assertFalse(protocol.isHeartRate(datas[0]));
        assertFalse(protocol.isHeartRate(datas[1]));
        assertTrue(protocol.isHeartRate(datas[2]));

        assertFalse(protocol.isHeartRate(datas[0]));
        assertFalse(protocol.isHeartRate(datas[1]));
        assertTrue(protocol.isHeartRate(datas[2]));
    }

    @Test
    public void checkInvalidateData() {
        int[] datas = {1, 2, 3};
        HeartRateProtocol protocol = new HeartRateProtocolImpl(null);
        assertFalse(protocol.isHeartRate(datas[0]));
        assertFalse(protocol.isHeartRate(datas[1]));
        assertFalse(protocol.isHeartRate(datas[2]));

        assertFalse(protocol.isHeartRate(datas[0]));
        assertFalse(protocol.isHeartRate(datas[1]));
        assertFalse(protocol.isHeartRate(datas[2]));

        assertFalse(protocol.isHeartRate(datas[0]));
        assertFalse(protocol.isHeartRate(datas[1]));
        assertFalse(protocol.isHeartRate(datas[2]));
    }

    @Test
    public void testReturnData() {
        final int[] datas = {0, 1, 2};
        HeartRateProtocol protocol = new HeartRateProtocolImpl(new HeartRateProtocol.OnReturnDataCallback() {
            @Override
            public void onData(int data1, int data2) {
                assertEquals(datas[0], data1);
                assertEquals(datas[1], data2);
            }
        });
        assertFalse(protocol.isHeartRate(datas[0]));
        assertFalse(protocol.isHeartRate(datas[1]));
        assertFalse(protocol.isHeartRate(datas[2]));
    }
}