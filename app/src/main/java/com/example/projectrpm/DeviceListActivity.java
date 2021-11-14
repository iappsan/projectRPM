package com.example.projectrpm;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class DeviceListActivity extends Activity {

    Button btnBack;
    private static final String EXTRA_DEVICE_ADDRESS = "device_address";
    private BluetoothAdapter btAdapter;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_device_list);

        btnBack = (Button) findViewById(R.id.btnBack);
        btAdapter = BluetoothAdapter.getDefaultAdapter();

        if (btAdapter == null) {
            Toast t = Toast.makeText(getApplicationContext(), "Este dispositivo no soporta BT", Toast.LENGTH_SHORT);
            finish();
        } else {
            if (!btAdapter.isEnabled()) {
                Toast t = Toast.makeText(getApplicationContext(), "Por favor activa tu BT", Toast.LENGTH_SHORT);
                finish();
            }
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}