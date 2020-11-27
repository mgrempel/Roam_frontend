package com.pixelworks.roam;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class BluetoothConnectActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter;
    private LocationManager locationManager;

    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_connect);

        prepareRadios();
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