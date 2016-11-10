package com.wqy.ecg;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.wqy.ecg.view.ECGView;
import com.wqy.ecg.view.ECGViewAdapterImpl;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ECGView ecgView = (ECGView) findViewById(R.id.ecg);
        final ECGViewAdapterImpl adapter = new ECGViewAdapterImpl(ecgView);
        ecgView.setAdapter(adapter);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            final double PI2 = Math.PI * 2;
            final double unit = PI2 / 300;
            {
                Log.d(TAG, "instance initializer: unit = " + unit);;
            }
            double arc = 0.0;
            @Override
            public void run() {
                // Send Data
                byte b = (byte) (Math.sin(arc) * 127);
//                Log.d(TAG, "run: b = " + b);
                adapter.onReceiveData(b);
                arc += unit;
                if (arc >= PI2) {
                    arc -= PI2;
                }
                handler.postDelayed(this, 5);
            }
        };
        handler.post(runnable);
    }
}
