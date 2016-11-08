package com.wqy.ecg;

import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            byte b = (byte) random.nextInt(256);
            System.out.println(b);
            assertTrue(b <= 127);
            assertTrue(b >= -128);
        }
    }
}