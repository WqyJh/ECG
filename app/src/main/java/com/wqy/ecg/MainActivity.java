package com.wqy.ecg;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.wqy.ecg.util.Common;
import com.wqy.ecg.util.HeartRateProtocol;
import com.wqy.ecg.util.HeartRateProtocolImpl;
import com.wqy.ecg.view.ECGView;
import com.wqy.ecg.view.ECGViewAdapterImpl;

import java.util.Random;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

import static com.wqy.ecg.util.Common.intToByte;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int FREQUENCY = 200;
    private static final int DT = 1000 / FREQUENCY;

//    private FloatingActionButton start;
    private ECGView ecgView;
    private ECGViewAdapterImpl ecgViewAdapter;
    private BluetoothSPP bt;
    private CoordinatorLayout container;
    private HeartRateProtocol protocol;
    private TextView heartRateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initBT();
        initUtils();
        createDataServer();
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

    static final int TEST_DATA = 100;
    void createDataServer() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == TEST_DATA) {
                    handleData(msg.arg1);
                }
            }
        };
        Runnable runnable = new Runnable() {
            final double PI2 = Math.PI * 2;
            final double unit = PI2 / 200;
            Random r = new Random();

            double arc = 0.0;
            @Override
            public void run() {
                // Send Data
                int data = (int) ((Math.sin(arc) + 1) / 2 * 255);
//                Log.d(TAG, "run: b = " + b);
                handler.obtainMessage(TEST_DATA, data, -1).sendToTarget();
                arc += unit;
                if (arc >= PI2) {
                    arc -= PI2;
                    // 发送心率
                    handler.obtainMessage(TEST_DATA, 0, -1).sendToTarget();
                    handler.obtainMessage(TEST_DATA, 255, -1).sendToTarget();
                    int rate = r.nextInt(256);
                    handler.obtainMessage(TEST_DATA, rate, -1).sendToTarget();
                }
                handler.postDelayed(this, 5);
            }
        };
        handler.post(runnable);

    }

    private void handleData(int i) {
        if (i < 0) {
            ecgViewAdapter.onReceiveData((byte) 0);
        } else {
            if (protocol.isHeartRate(i)) {
                heartRateView.setText(String.valueOf(i));
            } else {
                byte b = intToByte(i);
                ecgViewAdapter.onReceiveData(b);
            }
        }
    }

    void initUtils() {
        protocol = new HeartRateProtocolImpl(new HeartRateProtocol.OnReturnDataCallback() {
            @Override
            public void onData(int data1, int data2) {
                if (data1 > -1) {
                    ecgViewAdapter.onReceiveData(intToByte(data1));
                }
                if (data2 > -1) {
                    ecgViewAdapter.onReceiveData(intToByte(data2));
                }
            }
        });
    }

    void initBT() {
        bt = new BluetoothSPP(MainActivity.this);
        bt.setOnByteReceivedListener(new BluetoothSPP.OnByteReceivedListener() {
            @Override
            public void onByteReceived(int i) {
                handleData(i);
            }
        });
        bt.setOnServiceStopListener(new BluetoothSPP.OnServiceStopListener() {
            @Override
            public void onServiceStop() {
                if (ecgViewAdapter != null) {
                    ecgViewAdapter.reset();
                }
                heartRateView.setText("");
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
        ecgViewAdapter = new ECGViewAdapterImpl(ecgView);
        ecgView.setAdapter(ecgViewAdapter);
        container = (CoordinatorLayout) findViewById(R.id.container);
        heartRateView = (TextView) findViewById(R.id.heart_rate);
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
