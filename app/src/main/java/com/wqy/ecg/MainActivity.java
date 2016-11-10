package com.wqy.ecg;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.wqy.ecg.view.ECGView;
import com.wqy.ecg.view.ECGViewAdapterImpl;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private FloatingActionButton start;
    private ECGView ecgView;
    private ECGViewAdapterImpl adapter;
    private BluetoothSPP bt;
    private CoordinatorLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initUtils();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) {
            makeSnackbar("Bluetooth is Disabled!");
        } else {

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        bt.stopService();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d(TAG, "onActivityResult: bt == null ? " + (bt == null));
                bt.connect(data);
            }
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_ANDROID);
//                setup();
            } else {
                // Do something if user doesn't choose any device (Pressed back)
            }
        }
    }

    void createDataServer() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            final double PI2 = Math.PI * 2;
            final double unit = PI2 / 300;

            {
                Log.d(TAG, "instance initializer: unit = " + unit);
                ;
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

    void initUtils() {
        bt = new BluetoothSPP(MainActivity.this);
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            @Override
            public void onDataReceived(byte[] bytes, String s) {
                Log.d(TAG, "onDataReceived: bytes length = " + (bytes == null ? 0 : bytes.length));
                adapter.onReceiveData(bytes);
            }
        });
    }

    void initViews() {
        ecgView = (ECGView) findViewById(R.id.ecg);
        adapter = new ECGViewAdapterImpl(ecgView);
        ecgView.setAdapter(adapter);
        container = (CoordinatorLayout) findViewById(R.id.container);

        start = (FloatingActionButton) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDevices();
            }
        });
    }

    void makeSnackbar(String message) {
        Snackbar.make(container, message, Snackbar.LENGTH_SHORT).show();
    }

    void showDevices() {
        Intent intent = new Intent(getApplicationContext(), DeviceList.class);
        startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
    }
}
