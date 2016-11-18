package com.wqy.ecg;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.wqy.ecg.util.Common;
import com.wqy.ecg.view.ECGView;
import com.wqy.ecg.view.ECGViewAdapterImpl;
import com.wqy.ecg.view.ECGViewAdapterImpl2;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int FREQUENCY = 200;
    private static final int DT = 1000 / FREQUENCY;

//    private FloatingActionButton start;
    private ECGView ecgView;
    private ECGViewAdapterImpl adapter;
    private BluetoothSPP bt;
    private CoordinatorLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initBT();
//        createDataServer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) {
            makeSnackbar("Bluetooth is Disabled!");
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
            }
            bt.startService(BluetoothState.DEVICE_OTHER);
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
                bt.startService(BluetoothState.DEVICE_OTHER);
//                setup();
            } else {
                // Do something if user doesn't choose any device (Pressed back)
            }
        }
    }

    void handleData() {
        // TODO: 16-11-13
    }

    void createDataServer() {
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            final double PI2 = Math.PI * 2;
            final double unit = PI2 / 200;

            {
                Log.d(TAG, "instance initializer: unit = " + unit);
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

    public String bytesString(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("bytes:[");
        for (int i = 0, len = bytes.length; i < len; i++) {
            sb.append(bytes[i]);
            if (i < len - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }


    void initBT() {
        bt = new BluetoothSPP(MainActivity.this);
        bt.setOnByteReceivedListener(new BluetoothSPP.OnByteReceivedListener() {
            @Override
            public void onByteReceived(int i) {
                if (i < 0) {
                    adapter.onReceiveData((byte) 0);
                } else {
                    byte b = Common.intToByte(i);
                    adapter.onReceiveData(b);
                }
            }
        });
        bt.setOnServiceStopListener(new BluetoothSPP.OnServiceStopListener() {
            @Override
            public void onServiceStop() {
                if (adapter != null) {
                    adapter.reset();
                }
            }
        });
        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            @Override
            public void onDeviceConnected(String s, String s1) {
                makeSnackbar(String.format("Connect to %s@%s", s, s1));
                Log.d(TAG, "onDeviceConnected: Connected");
            }

            @Override
            public void onDeviceDisconnected() {
                makeSnackbar("Device disconnected");
                Log.d(TAG, "onDeviceDisconnected: Disconnected");
            }

            @Override
            public void onDeviceConnectionFailed() {
                Log.d(TAG, "onDeviceConnectionFailed: Connect failed");
                makeSnackbar("Connect failed");
            }
        });
        bt.setBluetoothStateListener(new BluetoothSPP.BluetoothStateListener() {
            @Override
            public void onServiceStateChanged(int state) {
                if (state == BluetoothState.STATE_CONNECTED) {
                    // Do something when successfully connected
//                    makeSnackbar("Service State Changed: Connected");
                } else if (state == BluetoothState.STATE_CONNECTING) {
                    // Do something while connecting
                    makeSnackbar("Service State Changed: Connecting");
                } else if (state == BluetoothState.STATE_LISTEN) {
                    // Do something when device is waiting for connection
                    makeSnackbar("Service State Changed: Waiting for connection");
                } else if (state == BluetoothState.STATE_NONE) {
                    // Do something when device don't have any connection
                    makeSnackbar("Service State Changed: None Connection");
                }
            }
        });
    }

    void initViews() {
        ecgView = (ECGView) findViewById(R.id.ecg);
        adapter = new ECGViewAdapterImpl2(ecgView);
        ecgView.setAdapter(adapter);
        container = (CoordinatorLayout) findViewById(R.id.container);
    }

    void makeSnackbar(String message) {
        Snackbar.make(container, message, Snackbar.LENGTH_SHORT).show();
    }

    void showDevices() {
        Intent intent = new Intent(getApplicationContext(), DeviceList.class);
        startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.connect:
                showDevices();
                return true;
            case R.id.disconnect:
                bt.stopService();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
