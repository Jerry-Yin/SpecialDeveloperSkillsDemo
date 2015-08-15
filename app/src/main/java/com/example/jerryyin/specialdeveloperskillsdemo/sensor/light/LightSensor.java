package com.example.jerryyin.specialdeveloperskillsdemo.sensor.light;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import com.example.jerryyin.specialdeveloperskillsdemo.R;

/**
 * Created by JerryYin on 8/14/15.
 * 特色开发－－传感器
 */
public class LightSensor extends Activity {

    /**Values*/
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private SensorEventListener mSensorEventListener;

    /**Views*/
    private TextView mtvLightLevel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_layout_light);
        initViews();
        initSensor();
    }

    public void initViews(){
        mtvLightLevel = (TextView) findViewById(R.id.tv_light_level);
    }

    public void initSensor() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);       // TYPE_LIGHT 表示获取到一个 光线传感器 的实例

        mSensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                //当传感器 监测到的数值 发生变化时就会调用
                //（event）参数里又包含了一个 values 数组,所有传感器输出的信息都是 存放在这里的。
                // values数组中第一个下标的值就是当前的光照强度
                float value = event.values[0];
                mtvLightLevel.setText("Current light level is :" + value + " lx");
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                //当传感器的 精度 发生变化时就会调用

            }
        };

        mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_NORMAL);  //第三个参数表示传感器输出信息的更新速率


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //程序退出或传感器使用完毕时,一定要调用 unregisterListener ()方 法将使用的资源释放掉
        if (mSensorManager != null){
            mSensorManager.unregisterListener(mSensorEventListener);
        }
    }
}
