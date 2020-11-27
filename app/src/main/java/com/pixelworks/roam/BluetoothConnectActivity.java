package com.pixelworks.roam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.AdvertiseCallback;
import android.bluetooth.le.AdvertiseData;
import android.bluetooth.le.AdvertiseSettings;
import android.bluetooth.le.BluetoothLeAdvertiser;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.nio.ByteBuffer;

public class BluetoothConnectActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private LocationManager locationManager;

    //BLE advertiser stuff
    private BluetoothLeAdvertiser advertiser;
    private AdvertiseCallback advertiseCallback;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connect);

        //Ensure bluetooth and location are running
        prepareRadios();

        //start advertising
        advertiserPrimer();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Handle callback for turning on bluetooth.
        if(requestCode == 1) {
            if(resultCode != RESULT_OK) {
                finish();
            }
        }
    }

    //Prepare location and bluetooth radios before searching for nearby friends
    private void prepareRadios() {
        //Get our bluetooth object
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(bluetoothAdapter == null) {
            //Device doesn't have bluetooth, we're done here.
            finish();
        }

        //Turn on the adapter
        if(!bluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 1);
        }

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if(locationManager == null) {
            //Device doesn't have a GPS
            finish();
        }

        //Request location permissions
        if ( Build.VERSION.SDK_INT >= 23){
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED  ){
                requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
            }
        }

        //turn on location
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Intent enableLocation = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(enableLocation);
        }
    }

    //Primes our bluetooth low energy listener
    private void advertiserPrimer() {

        //Establish our transmission settings
        AdvertiseSettings advertiseSettings = new AdvertiseSettings.Builder()
                .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
                .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
                .setConnectable(false)
                .build();

        //Make services public, map data to service
        ParcelUuid puuid = ParcelUuid.fromString(getString(R.string.serviceSig));
        int id = SharedPreferencesHelper.getIntValue("id");
        AdvertiseData advertiseData = new AdvertiseData.Builder()
                .setIncludeDeviceName(false)
                .addServiceUuid(puuid)
                .addServiceData(puuid, ByteBuffer.allocate(4).putInt(id).array())
                .build();

        advertiseCallback = new AdvertiseCallback() {
            @Override
            public void onStartSuccess(AdvertiseSettings settingsInEffect) {
                Log.d("TEST", "BLE started advertising");
                super.onStartSuccess(settingsInEffect);
            }

            @Override
            public void onStartFailure(int errorCode) {
                Log.d("TEST", "BLE listener failed with code: " + errorCode);
                super.onStartFailure(errorCode);
            }
        };

        //Everything is prepared, let's start advertising.
        if(bluetoothAdapter.isEnabled()) {
            advertiser = bluetoothAdapter.getBluetoothLeAdvertiser();
            advertiser.startAdvertising(advertiseSettings, advertiseData, advertiseCallback);
        }

        //Define our callbacks
//        ScanCallback scanCallback = new ScanCallback() {
//            @Override
//            public void onScanResult(int callbackType, ScanResult result) {
//                super.onScanResult(callbackType, result);
//
//                //Ensure results aren't null
//                if(result == null) return;
//
//                Log.d("TEST", "Successfully received results from scan")
//            }
//
//            @Override
//            public void onScanFailed(int errorCode) {
//                Log.d("TEST", "Bluetooth error code returned:" + errorCode);
//                super.onScanFailed(errorCode);
//            }
//        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    finish();
                }
                else {
                    
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}