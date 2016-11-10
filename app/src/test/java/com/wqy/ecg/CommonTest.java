package com.wqy.ecg;

import com.wqy.ecg.util.Common;

import static org.junit.Assert.*;
import static com.wqy.ecg.util.Common.*;

import org.junit.Test;

/**
 * Created by wqy on 16-11-10.
 */

public class CommonTest {
    @Test
    public void testSubBytes() {
        byte[] b = {1, 4, 5, 6};
        byte[] sb = subBytes(b, 1, 2);
        assertNull(subBytes(null, 0, 5));
        assertArrayEquals(new byte[]{4, 5}, sb);
        assertArrayEquals(b, subBytes(b, 0, b.length));
    }
}
