package com.wqy.ecg;

import com.wqy.ecg.util.ListByte;
import com.wqy.ecg.util.LoopQueueByte;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Created by wqy on 16-11-8.
 */

public class LoopQueueByteTest {
    LoopQueueByte loopQueueByte = new LoopQueueByte(10);

    {
        for (int i = 0; i < 10; i++) {
            loopQueueByte.enqueue((byte) i);
        }
    }

    @Test
    public void enqueue() {
        assertTrue(loopQueueByte.isFull());

        for (int i = 0; i < 5; i++) {
            loopQueueByte.dequeue();
        }
        for (int i = 0; i < 5; i++) {
            loopQueueByte.enqueue((byte) i);
        }
        for (int i = 0; i < 5; i++) {
            assertEquals((byte) (i + 5), loopQueueByte.get(i));
        }
        for (int i = 5; i < 10; i++) {
            assertEquals((byte) (i - 5), loopQueueByte.get(i));
        }
    }

    @Test
    public void dequeue() {
        for (int i = 0; i < 10; i++) {
            loopQueueByte.dequeue();
        }
        assertTrue(loopQueueByte.isEmpty());
    }

    @Test
    public void get() {
        for (int i = 0; i < 10; i++) {
            assertEquals((byte) i, loopQueueByte.dequeue());
        }
    }

    @Test
    public void subList1() {
        ListByte listByte = loopQueueByte.subList(0, 5);
        for (int i = 0; i < 5; i++) {
            assertEquals((byte)i, listByte.get(i));
        }
    }

    @Test
    public void subList2() {
        for (int i = 0; i < 5; i++) {
            loopQueueByte.dequeue();
        }
        for (int i = 0; i < 5; i++) {
            loopQueueByte.enqueue((byte) i);
        }
        ListByte listByte = loopQueueByte.subList(4, 8);
        assertEquals((byte)9, listByte.get(0));
        assertEquals((byte)0, listByte.get(1));
        assertEquals((byte)2, listByte.get(3));
    }
}
