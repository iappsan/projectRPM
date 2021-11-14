package com.example.projectrpm;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Set;

public class DeviceListActivity extends Activity {

    Button btnBack;
    ListView pairedLV;
    ListView newDevicesLV;
    BroadcastReceiver mBroadcastReceiver;
    private BluetoothAdapter btAdapter;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_device_list);

        btnBack = (Button) findViewById(R.id.btnBack);
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        pairedLV = (ListView) findViewById(R.id.pairedLV);
        newDevicesLV = (ListView) findViewById(R.id.newDevicesLV);


        if (btAdapter == null) {
            Toast t = Toast.makeText(getApplicationContext(), "Este dispositivo no soporta BT", Toast.LENGTH_SHORT);
            t.show();
            finish();
        } else {
            if (!btAdapter.isEnabled()) {
                Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(intent, 1);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},2);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},4);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH_ADMIN},3);
        }

        Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
        ArrayAdapter<String> pdArrayAdapter = new ArrayAdapter<>(this, R.layout.item_device_name);
        pairedLV.setAdapter(pdArrayAdapter);

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                pdArrayAdapter.add(deviceName+'\n'+deviceHardwareAddress);
            }
        }

        btAdapter.startDiscovery();
        final ArrayList<String> arrayListDispositivos = new ArrayList<>();

        mBroadcastReceiver = new BroadcastReceiver() {
          @Override
          public void onReceive(Context context, Intent intent) {
              final String action = intent.getAction();
              Log.d("ACTION", "onReceive: ACTION FOUND.");

              if (action.equals(BluetoothDevice.ACTION_FOUND)){
                  BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                  String strDev = device.getName()+'\n'+device.getAddress();
                  if (arrayListDispositivos.indexOf(strDev) == -1) {
                      arrayListDispositivos.add(strDev);
                  }
                  if (device.getName() != null) {
                      Log.i("BT DEVICES", device.getName());
                  }
              }
              if (arrayListDispositivos.size() != 0) {
                  ArrayAdapter<String> itemAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.item_device_name, arrayListDispositivos);
                  newDevicesLV.setAdapter(itemAdapter);
              }
          }
        };

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mBroadcastReceiver, intentFilter);

        btnBack.setOnClickListener(v -> finish());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

}