package com.wqy.ecg;

import org.junit.Test;

import java.nio.ByteOrder;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            byte b = (byte) random.nextInt(256);
            assertTrue(b <= 127);
            assertTrue(b >= -128);
        }
    }


    public void intToByte() {
        int i1 = 0;
        int i2 = 127;
        int i3 = 128;
        int i4 = 255;
        int i5 = 256;
        System.out.println((byte)i1);
        System.out.println((byte)i2);
        System.out.println((byte)i3);
        System.out.println((byte)i4);
        System.out.println((byte)i5);
    }


    public void intToByte2() {
        int i1 = -128;
        int i2 = 0;
        int i3 = 127;
        int i4 = -129;
        int i5 = 128;
        System.out.println((byte)i1);
        System.out.println((byte)i2);
        System.out.println((byte)i3);
        System.out.println((byte)i4);
        System.out.println((byte)i5);
    }


}