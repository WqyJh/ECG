package com.wqy.ecg.util;

/**
 * Created by wqy on 16-11-10.
 */

public class Common {
    public static byte[] subBytes(byte[] bytes, int from, int size) {
        if (bytes == null) {
            return null;
        }
        int len = bytes.length;
        if (from < 0 || from >=len || size < 0 || from + size - 1 >= len) {
            throw new IndexOutOfBoundsException();
        }
        byte[] newBytes = new byte[size];
        for (int i = 0; i < size; i++) {
            newBytes[i] = bytes[from + i];
        }
        return newBytes;
    }

    /**
     * Convert int range from 0 to 255 to byte.
     * @param i
     * @return
     */
    public static byte intToByte(int i) {
        return (byte) (i - 128);
    }
}
