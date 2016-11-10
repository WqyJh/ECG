package com.wqy.ecg.view;

import com.wqy.ecg.util.ListByte;
import com.wqy.ecg.util.LoopQueueByte;

/**
 * Created by wqy on 16-11-9.
 */

public class ECGViewAdapterImpl implements ECGViewAdapter {

    private ECGView ecgView;
    private LoopQueueByte loopQueue;
    private final int size = 600;

    public ECGViewAdapterImpl(ECGView ecgView) {
        this.ecgView = ecgView;
        loopQueue = new LoopQueueByte(1200);

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
//        if (offset > 10) {
//            loopQueue.offsetFront(offset);
//            ecgView.invalidate();
//        }
        loopQueue.offsetFront(offset);
        ecgView.invalidate();
    }
}
