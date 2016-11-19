package com.wqy.ecg.view;

import android.os.Handler;

import com.wqy.ecg.util.ListByte;
/**
 * Created by wqy on 16-11-18.
 */

public class ECGViewAdapterImpl2 extends ECGViewAdapterImpl {
    private static final String TAG = ECGViewAdapterImpl2.class.getSimpleName();

    private Handler handler = new Handler();
    private Runnable refresh = new Runnable() {
        @Override
        public void run() {
            if (loopQueue.size() > drawSize) {
                loopQueue.dequeue(1);
            }
            ecgView.invalidate();
            handler.postDelayed(this, 5);
        }
    };

    public ECGViewAdapterImpl2(ECGView ecgView) {
        this(ecgView, 0);
    }

    public ECGViewAdapterImpl2(final ECGView ecgView, int size) {
        super(ecgView, size);
        handler.post(refresh);
    }

    /**
     * Get SubList from the tail of the loopQueue.
     * @return
     */
//    @Override
//    public ListByte getList() {
//        int listSize = loopQueue.drawSize();
//        if (listSize > drawSize) {
//            return loopQueue.subList(listSize - drawSize, drawSize);
//        } else {
//            return loopQueue.subList(0, listSize);
//        }
//    }

    @Override
    public void onReceiveData(byte b) {
        loopQueue.enqueue(b);
    }
}
