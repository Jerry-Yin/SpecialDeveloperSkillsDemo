package com.example.jerryyin.specialdeveloperskillsdemo.sensor.orientation;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jerryyin.specialdeveloperskillsdemo.R;

/**
 * Created by JerryYin on 8/15/15.
 * 方向传感器，之前实现方法已经不推荐，目前推荐方式是由 加速度传感器＋地磁传感器组成
 */
public class OrientationSensor extends Activity {

    /**
     * Views
     */
    private ImageView mImageCompass;

    /**
     * Values
     */
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor, mMagneticSensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sensor_layout_orientation);

        initViews();
        initSensor();
    }

    public void initViews() {
        mImageCompass = (ImageView) findViewById(R.id.iv_compass);

    }

    public void initSensor() {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);       //加速度传感器
        mMagneticSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mSensorManager.registerListener(mSensorEventListener, mAccelerometerSensor, SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(mSensorEventListener, mMagneticSensor, SensorManager.SENSOR_DELAY_GAME);

    }

    //初始化传感器接口
    private SensorEventListener mSensorEventListener = new SensorEventListener() {

        float[] accelerometerValues = new float[3];
        float[] magneticValues = new float[3];

        private float lastRotateDegree;

        @Override
        public void onSensorChanged(SensorEvent event) {
            // 判断当前是加速度传感器还是地磁传感器
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                // 注意赋值时要调用clone()方法
                accelerometerValues = event.values.clone();
            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                magneticValues = event.values.clone();
            }

            float[] R = new float[9];
            float[] values = new float[3];
            mSensorManager.getRotationMatrix(R, null, accelerometerValues, magneticValues);     //获取到一个包含旋转矩阵的 R 数组
            mSensorManager.getOrientation(R, values);       //values[0]记录着手机围绕着 Z 轴的旋转弧度 1--X, 2--Y(弧度)
//            Math.toDegrees(values[0]);

            // 将计算出的旋转角度取反,用于旋转指南针背景图
            float rotateDegree = -(float) Math.toDegrees(values[0]);
            if (Math.abs(rotateDegree - lastRotateDegree) > 1) {
                RotateAnimation animation = new RotateAnimation(lastRotateDegree, rotateDegree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setFillAfter(true);               // RotateAnimation 旋转动画效果(旋转的开始角度, 结束角度, X轴的伸缩模式, 伸缩值, Y轴的伸缩模式, 伸缩值)
                mImageCompass.startAnimation(animation);
                lastRotateDegree = rotateDegree;
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSensorManager != null) {
            mSensorManager.unregisterListener(mSensorEventListener);
        }
    }
}
