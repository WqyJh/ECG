package com.wqy.ecg;


import com.wqy.ecg.util.HeartRateProtocol;
import com.wqy.ecg.util.HeartRateProtocolImpl;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static com.wqy.ecg.util.Common.intToByte;

/**
 * Created by wqy on 16-11-22.
 */
public class HeartRateProtocolImplTest {

    @Test
    public void checkValidateData() {
        int[] datas = {244, 0, 255, 111, 98};
        HeartRateProtocol protocol = new HeartRateProtocolImpl(null);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < datas.length; j++) {
                if (j == 3) {
                    assertTrue(protocol.isHeartRate(datas[j]));
                }
                assertFalse(protocol.isHeartRate(datas[j]));
            }
        }
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
    public void testCheckData() {
        int[] datas = {244, 0, 255, 111, 98};
        int[] expected = {244, 98};
        final List list = new ArrayList(4);
        HeartRateProtocol protocol = new HeartRateProtocolImpl(new HeartRateProtocol.OnReturnDataCallback() {
            @Override
            public void onData(int type, int data1, int data2) {
                if (type == HeartRateProtocol.HEART_RATE) {
                    assertEquals(111, data1);
                } else {
                    list.add(data1);
                }
            }
        });
        for (int i = 0; i < datas.length; i++) {
            protocol.checkData(datas[i]);
        }
        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], list.get(i));
        }
    }
}