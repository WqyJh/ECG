package com.wqy.ecg;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.test.runner.AndroidJUnit4;

import com.wqy.ecg.util.Common;
import com.wqy.ecg.util.ListByte;
import com.wqy.ecg.util.LoopQueueByte;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

/**
 * Created by wqy on 16-11-15.
 */
@RunWith(AndroidJUnit4.class)
public class LoopQueueTest {
    private static final int CAPACITY = 1200;
    private static final int SIZE = 600;
    private static final int GET_DATA = 10;
    LoopQueueByte loopQueue = new LoopQueueByte(CAPACITY);
    final CountDownLatch signal = new CountDownLatch(4000);
    private byte[] source = null;
    private byte[] expected = null;

    private Handler handler = null;
    private Runnable runnable = new Runnable() {
        int i = 0;

        @Override
        public void run() {
            handler.obtainMessage(GET_DATA, i, -1).sendToTarget();
            i = (i + 1) % 256;
            handler.postDelayed(this, 1);
        }
    };

    @Before
    public void setup() {
        handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == GET_DATA) {
                    expected = getList() == null ? null : getList().toBytes();
                    byte b = Common.intToByte(msg.arg1);
                    loopQueue.enqueue(b);

                    int offset = loopQueue.size() - SIZE;
                    if (offset > 0) {
                        move(offset);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void move(int offset) {
        loopQueue.dequeue(offset);
        invalidate();
    }

    public void invalidate() {
//        expected = source;
        source = getList() == null ? null : getList().toBytes();
        if (source != null && expected != null) {
            assertListByteEquals(expected, 1, SIZE - 1,
                    source, 0);
            signal.countDown();
        }
    }

    public ListByte getList() {
        int listSize = loopQueue.size();
        return loopQueue.subList(0, listSize > SIZE ? SIZE : listSize);
    }

    @Test
    public void test() throws InterruptedException {
        handler.post(runnable);
        signal.await();
    }

    public void assertListByteEquals(ListByte expected, ListByte source) {
        assertNotNull(expected);
        assertNotNull(source);
        int size = expected.size();
        assertEquals(size, source.size());
        for (int i = 0; i < size; i++) {
            assertEquals(expected.get(i), source.get(i));
        }
    }

    public void assertListByteEquals(byte[] expected, int fromIndex1, int size,
                                     byte[] source, int fromIndex2) {
        assertNotNull(expected);
        assertNotNull(source);

        for (int i = 0; i < size; i++) {
            byte a = expected[fromIndex1 + i];
            byte b = source[fromIndex2 + i];
            System.out.println("a: " + a + ", b:" + b);
            assertEquals(a, b);
        }
    }
}
