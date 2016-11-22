package com.wqy.ecg.view;

import android.util.Log;

import com.wqy.ecg.util.ListByte;
import com.wqy.ecg.util.LoopQueueByte;

/**
 * Created by wqy on 16-11-9.
 */

public class ECGViewAdapterImpl implements ECGViewAdapter {

    private ECGView ecgView;
    private LoopQueueByte loopQueue;
    private int size = 600;

    public ECGViewAdapterImpl(ECGView ecgView) {
        this.ecgView = ecgView;
        loopQueue = new LoopQueueByte(1200);
    }

    public ECGViewAdapterImpl(ECGView ecgView, int size) {
        this(ecgView);
        if (size > 0) {
            this.size = size;
        }
    }

    @Override
    public ListByte getList() {
        int listSize = loopQueue.size();
        return loopQueue.subList(0, listSize > size ? size : listSize);
    }

    public void onReceiveData(byte b) {
        if (loopQueue.isFull()) {
            return;
        }
        loopQueue.enqueue(b);
        int offset = loopQueue.size() - size;
        if (offset > 0) {
            move(offset);
        }
//        if (offset > 10) {
//            loopQueue.dequeue(offset);
//            ecgView.invalidate();
//        }
    }

    public void onReceiveData(byte[] bytes) {
        if (loopQueue.isFull()) {
            return;
        }
        int offset = loopQueue.enqueue(bytes);
        move(offset);
    }

    public void move(int offset) {
        loopQueue.dequeue(offset);
        ecgView.invalidate();
    }

    public void reset() {
        this.loopQueue.clear();
        ecgView.invalidate();
    }
}
