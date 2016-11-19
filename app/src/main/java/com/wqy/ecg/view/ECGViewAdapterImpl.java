package com.wqy.ecg.view;

import com.wqy.ecg.util.ListByte;
import com.wqy.ecg.util.LoopQueueByte;

/**
 * Created by wqy on 16-11-9.
 */

public class ECGViewAdapterImpl implements ECGViewAdapter {

    protected ECGView ecgView;
    protected LoopQueueByte loopQueue;
    protected int drawSize = 600;
    protected static final int QUEUE_SIZE = 2400;

    public ECGViewAdapterImpl(ECGView ecgView) {
        this(ecgView, 0);

    }

    public ECGViewAdapterImpl(ECGView ecgView, int drawSize) {
        this.ecgView = ecgView;
        loopQueue = new LoopQueueByte(QUEUE_SIZE);
        if (drawSize > 0) {
            this.drawSize = drawSize;
        }
    }

    @Override
    public ListByte getList() {
        int listSize = loopQueue.size();
        return loopQueue.subList(0, listSize > drawSize ? drawSize : listSize);
    }

    public void onReceiveData(byte b) {
        if (loopQueue.isFull()) {
            return;
        }
        loopQueue.enqueue(b);
        int offset = loopQueue.size() - drawSize;
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
