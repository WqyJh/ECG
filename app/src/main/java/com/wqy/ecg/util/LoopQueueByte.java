package com.wqy.ecg.util;

import java.util.AbstractList;

/**
 * Created by wqy on 16-11-8.
 */

public class LoopQueueByte {

    private int front;
    private int rear;
    private int capacity;
    private int size;
    private byte[] data;

    public LoopQueueByte(int capacity) {
        this.capacity = capacity;
        data = new byte[capacity];
        size = 0;
        front = 0;
        rear = 0;
    }

    public boolean isFull() {
        return size == capacity;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public byte get(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("LoopQueueByte: get index == " + index);
        }
        int i = (front + index) % capacity;
        return data[i];
    }

    public boolean enqueue(byte b) {
        if (isFull()) {
            return false;
        } else {
            data[rear] = b;
            rear = (rear + 1) % capacity;
            size++;
            return true;
        }
    }

    public void offsetFront(int offset) {
        if (offset < size) {
            front = (front + offset) % size;
        }
    }

    /**
     * 只有当isEmpty()返回true时可以调用这个方法，
     * 否则得不到正确结果
     * @return
     */
    public byte dequeue() {
        if (isEmpty()) {
            return (byte)0;
        } else {
            byte b = data[front];
            front++;
            size--;
            return b;
        }
    }

//    public ListByte subList(int fromIndex, int toIndex) {
//        if (fromIndex < 0 || fromIndex >= size || toIndex < 0 || toIndex >= size) {
//            throw new IndexOutOfBoundsException("LoopQueueByte: subList");
//        }
//        return new SubList(this, fromIndex, toIndex - fromIndex + 1);
//    }

    public ListByte subList(int fromIndex, int size) {
        if (size == 0) {
            return null;
        }
        if (fromIndex < 0 || fromIndex >= size || size > this.size) {
            throw new IndexOutOfBoundsException(String.format("LoopQueueByte: subList fromIndex = %d, size = %d", fromIndex, size));
        }
        return new SubList(this, fromIndex, size);
    }

    public static class SubList implements ListByte {
        private final LoopQueueByte parent;
        private final int offset;
        private final int size;

        SubList(LoopQueueByte parent,
                int fromIndex,
                int size) {
            this.parent = parent;
            this.offset = fromIndex;
            this.size = size;
        }

        @Override
        public byte get(int index) {
            if (index < 0 || index >= this.size) {
                throw new IndexOutOfBoundsException("SubList: get index == " + index);
            }
            return parent.get(offset + index);
        }

        @Override
        public int size() {
            return this.size;
        }
    }
}