package com.example.chinhowh.userapp20;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import layout.Fragment_event;
import layout.Fragment_home;
import layout.Fragment_settings;
import layout.Fragment_weather;

public class MainActivity extends AppCompatActivity {

    private FragmentManager Fmanager;
    private FragmentTransaction Ftransaction;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fmanager = getSupportFragmentManager();
            Ftransaction = Fmanager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Fragment_home fragment_home = new Fragment_home();
                    Ftransaction.replace(R.id.content, fragment_home);
                    Ftransaction.commit();
                    return true;

                case R.id.navigation_event:
                    Fragment_event fragment_event = new Fragment_event();
                    Ftransaction.replace(R.id.content, fragment_event);
                    Ftransaction.commit();
                    return true;

                case R.id.navigation_weather:
                    Fragment_weather fragment_weather = new Fragment_weather();
                    Ftransaction.replace(R.id.content, fragment_weather);
                    Ftransaction.commit();
                    return true;

                case R.id.navigation_settings:
                    Fragment_settings fragment_settings = new Fragment_settings();
                    Ftransaction.replace(R.id.content, fragment_settings);
                    Ftransaction.commit();
                    return true;

            }
            return false;
        }

    };


    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;
    private LocationManager lc;
    private Location currentLocation = null;
    private MyLocationListener ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fmanager = getSupportFragmentManager();
        Ftransaction = Fmanager.beginTransaction();
        Fragment_home fragment_home = new Fragment_home();
        Ftransaction.replace(R.id.content, fragment_home).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        lc = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lc.isProviderEnabled(LocationManager.GPS_PROVIDER)) //檢查GPS是否開啟，並跳出警告視窗
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("定位管理")
                    .setMessage("GPS目前狀態未啟用。\n" + "是否現在去啟用？")
                    .setPositiveButton("啟用", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(i);
                        }
                    })
                    .setNegativeButton("不啟用", null)
                    .create().show();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED) //
        {
            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 已經取得權限
                Toast.makeText(this, "取得權限取得GPS資訊",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "直到取得權限, 否則無法取得GPS資訊",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void onResume() {
        super.onResume();
        ll = new MyLocationListener();
        int minTime = 1000;
        float minDistance = 1;
        try {
            lc.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, ll);
            lc.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, minTime, minDistance, ll);
        }
        catch (SecurityException sex) {
            Log.e("UserApp", "GPS權限失敗..." + sex.getMessage());
        }
    }

    protected void onPause() {
        super.onPause();
        try {
            lc.removeUpdates(ll);
        }
        catch (SecurityException sex) {
            Log.e("UserApp", "GPS權限失敗..." + sex.getMessage());
        }
    }


    class MyLocationListener implements LocationListener {
        public void onLocationChanged(Location current)
        {
            double lat, lng;
            if (current != null)
            {
                currentLocation = current;
                lat = current.getLatitude();
                lng = current.getLongitude();
                Toast.makeText(MainActivity.this, "經緯度座標變更...", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }
}
