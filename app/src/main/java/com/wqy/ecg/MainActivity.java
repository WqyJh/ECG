package com.wqy.ecg;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.wqy.ecg.view.ECGView;
import com.wqy.ecg.view.ECGViewAdapterImpl;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ECGView ecgView = (ECGView) findViewById(R.id.ecg);
        final ECGViewAdapterImpl adapter = new ECGViewAdapterImpl(ecgView);
        ecgView.setAdapter(adapter);

        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            Random random = new Random();
            @Override
            public void run() {
                // Send Data
                byte b = (byte) random.nextInt(256);
                adapter.onReceiveData(b);
                handler.postDelayed(this, 5);
            }
        };
        handler.post(runnable);
    }
}
