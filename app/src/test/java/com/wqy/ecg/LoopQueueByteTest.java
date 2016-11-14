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
        assertEquals(10, loopQueueByte.size());
    }

    @Test
    public void dequeue() {
        for (int i = 0; i < 10; i++) {
            loopQueueByte.dequeue();
        }
        assertTrue(loopQueueByte.isEmpty());
        assertEquals(0, loopQueueByte.size());
    }

    @Test
    public void get() {
        for (int i = 0; i < 10; i++) {
            assertEquals((byte) i, loopQueueByte.dequeue());
        }
        assertEquals(0, loopQueueByte.size());
    }

    @Test
    public void subList1() {
        ListByte listByte = loopQueueByte.subList(0, 5);
        for (int i = 0; i < 5; i++) {
            assertEquals((byte)i, listByte.get(i));
        }
        assertEquals(5, listByte.size());
    }

    @Test
    public void subList2() {
        for (int i = 0; i < 5; i++) {
            loopQueueByte.dequeue();
        }
        for (int i = 0; i < 5; i++) {
            loopQueueByte.enqueue((byte) i);
        }
        assertEquals(10, loopQueueByte.size());
        ListByte listByte = loopQueueByte.subList(4, 8);
        assertEquals((byte)9, listByte.get(0));
        assertEquals((byte)0, listByte.get(1));
        assertEquals((byte)2, listByte.get(3));
        assertEquals(8, listByte.size());
    }

    public int offsetPointer(int p, int n, int capacity) {
        return (p + n) % capacity;
    }

    @Test
    public void testOffsetPointer() {
        int capacity = 100;
        int p = 0;
        p = offsetPointer(p, 20, capacity);
        assertEquals(20, p);
        p = offsetPointer(p, 70, capacity);
        assertEquals(p, 90, capacity);
        p = offsetPointer(p, 10, capacity);
        assertEquals(p, 0, capacity);
    }

    @Test
    public void clear() {
        loopQueueByte.clear();
        assertEquals(0, loopQueueByte.size());
    }
}
