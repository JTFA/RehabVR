package com.DPmiklos.rehabVR;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.thalmic.myo.Hub;


public class ConnectActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        //opytat povolenie pre zistenie lokacie, aby ukazalo sparovane zariadenie, ficura android 23+
        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);


        // First, we initialize the Hub singleton with an application identifier.
        Hub hub = Hub.getInstance();
        if (!hub.init(this, getPackageName())) {
            // We can't do anything with the Myo device if the Hub can't be initialized, so exit.
            Toast.makeText(this, "Couldn't initialize Hub", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        final Button mBlButton = (Button) findViewById(R.id.blButton);
        final Button mPlayButton = (Button) findViewById(R.id.playButton);
        final Button mConnectButton = (Button) findViewById(R.id.connectButton);
        final Button mWifiButton = (Button) findViewById(R.id.wifiButton);

//        mWifiButton.setEnabled(false);
//        mConnectButton.setEnabled(false);
//        mPlayButton.setEnabled(false);

        //BLUETOOTH cast
        final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivity(enableBtIntent);
                }
            }
        });


        mConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Hub.getInstance().attachToAdjacentMyo();//pripoji sa na prilahle myo
            }
        });

        //WIFI cast
        final WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        mWifiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifiManager.setWifiEnabled(true);
                mPlayButton.setEnabled(true);
            }
        });

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UnityPlayerNativeActivity.class);
                startActivity(intent);
            }
        });
    }
}
