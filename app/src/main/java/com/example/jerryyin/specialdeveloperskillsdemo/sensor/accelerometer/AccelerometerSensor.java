package com.example.jerryyin.specialdeveloperskillsdemo.sensor.accelerometer;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jerryyin.specialdeveloperskillsdemo.R;

/**
 * Created by JerryYin on 8/14/15.
 * 特色开发－－－加速度传感器
 */
public class AccelerometerSensor extends Activity {

    /**
     * Values
     */
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private SensorEventListener mSensorEventListener;

    /**
     * Views
     */
    private TextView mtvLightLevel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_layout_accelerometer);
        initViews();
        initSensor();

    }


    public void initViews() {


    }

    public void initSensor() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);       //加速度传感器
        mSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                // event同样存放所有数据，values 数组中会有三个值,分别代表手机在 X 轴、Y 轴和 Z 轴方向上的加速度信息
                // 加速度可能会是负值,所以要取它们的绝对值
                float xValue = Math.abs(event.values[0]);
                float yValue = Math.abs(event.values[1]);
                float zValue = Math.abs(event.values[2]);
                if (xValue > 15 || yValue > 15 || zValue > 15) {
                // 初始时有一个重力加速度 9.8，而且可以加载再任何轴上，所以给一个初始量15
                // 认为用户摇动了手机,触发摇一摇逻辑
                    Toast.makeText(AccelerometerSensor.this, "摇一摇", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {


            }
        }

        ;
        mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(mSensorEventListener);
        }

    }
}
