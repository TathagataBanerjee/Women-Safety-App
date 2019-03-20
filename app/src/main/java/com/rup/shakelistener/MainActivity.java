package com.rup.shakelistener;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SensorManager sm;
    private LocationManager locationManager;
    private float aceVal;
    private float aceLast;
    private float shake;
    private String t="http://maps.google.com/?q=22.5017764,88.3064908";
    private String num="9804046546";
    private double latitude;

    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sm.registerListener(sensorListener, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        aceVal = SensorManager.GRAVITY_EARTH;
        aceLast = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;


    }

        private final SensorEventListener sensorListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {

                float x = sensorEvent.values[0];
                float y = sensorEvent.values[1];
                float z = sensorEvent.values[2];

                aceLast = aceVal;
                aceVal = (float) Math.sqrt((double) (x * x + y * y + z * z));
                float delta = aceVal - aceLast;
                shake = shake * 0.9f + delta;

                if (shake > 28) {

                    Toast.makeText(getApplicationContext(), "SHAKED", Toast.LENGTH_SHORT).show();
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {                        return;
                    }
                    startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:1234567896")));//Enter your desired no. here
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Location location = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);
                    String a=String.valueOf(location.getLatitude());
                    String b=String.valueOf(location.getLongitude());
                    String text = "EMERGENCY SITUATION!!       My Current Location is: " +
                            "http://maps.google.com/?q="+a+","+b;
                    String no="1234567895";//Enter your desired no. here
                    SmsManager smsManager= SmsManager.getDefault();
                    smsManager.sendTextMessage(no,null,text,null,null);

                }

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };



    public void OnLocationChanged(Location location){
         longitude=location.getLongitude();
         latitude=location.getLatitude();

    }
}
