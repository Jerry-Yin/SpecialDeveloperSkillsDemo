package com.example.jerryyin.specialdeveloperskillsdemo.location;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jerryyin.specialdeveloperskillsdemo.R;

import java.util.List;

/**
 * Created by JerryYin on 8/14/15.
 * 特色开发－－基于位置的服务
 */
public class LocationService extends Activity {

    private TextView mtvShowLocation;

    private LocationManager mLocationManager;
    private List<String> providerLists;
    private String provider;
    private Location mLocation;
    private LocationListener mLocationListener;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.locatiion_layout);

        initViews();
        initLocation();
    }

    public void initViews() {
        mtvShowLocation = (TextView) findViewById(R.id.tv_show_location);

    }


    public void initLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);    //实例化管理器
        providerLists = mLocationManager.getProviders(true);            // 获取所有可用的位置提供器

        if (providerLists.contains(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.GPS_PROVIDER;
        } else if (providerLists.contains(LocationManager.NETWORK_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        } else {
            //当没有可用的位置􏰀供器时,弹出Toast􏰀示用户
            Toast.makeText(this, "No location provider to use",Toast.LENGTH_SHORT).show();
            return;
        }

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (mLocation == null){
                    mLocation = location;
                }
                // 更新当前设备的位置信息
                showLocation(mLocation);
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
        };

        mLocationManager.requestLocationUpdates(provider, 5000, 5, mLocationListener);
        mLocation = mLocationManager.getLastKnownLocation(provider);
        while (mLocation == null){
            mLocationManager.requestLocationUpdates(provider, 5000, 5, mLocationListener);
        }
        if (mLocation != null){
            showLocation(mLocation);
        }
    }

    private void showLocation(Location location) {
        String currentPosition = "latitude(纬度) is :" + location.getLatitude() + "\n" +
                                    "longitude(经度) is :" + location.getLongitude();
        mtvShowLocation.setText(currentPosition);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mLocationManager != null){
            // 关闭程序时将监听器移除
            mLocationManager.removeUpdates(mLocationListener);
        }

    }
}
