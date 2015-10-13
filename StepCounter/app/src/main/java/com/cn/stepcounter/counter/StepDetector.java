package com.cn.stepcounter.counter;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;

public class StepDetector implements SensorEventListener {

    //如果步行只持续10步以内，则判断为干扰
    public static int CANCEL_STEP=10;
    //判断时间间隔为两秒
    public static int LAST_TIME=10000;
    public static int CURRENT_SETP = 0;
    public int UNCHECKED_STEP=0;

    public static float SENSITIVITY = 0;

    //起始时间戳
    private static long end = 0;
    private static long v_end = 0;
    //终止时间戳
    private static long start = 0;
    private static long v_start = 0;

    private boolean isWalking=false;
    //加速度传感器的数据
    float X_lateral = 0;
    float Y_longitudinal = 0;
    float Z_vertical = 0;

    //用于控制具体频率
    int DELAY_TIME = 1;

    //计数
    int count = 0;

    //人体的最短运动间隔
    public static int MIN_INTERVAL=200;


    //调试参数
    Double average = 10.0;
    Double range = 1.6;
    Double range_b=0.9;




    public StepDetector(Context context) {
        // TODO Auto-generated constructor stub
        super();

        if (SettingsActivity.sharedPreferences == null) {
            SettingsActivity.sharedPreferences = context.getSharedPreferences(
                    SettingsActivity.SETP_SHARED_PREFERENCES,
                    Context.MODE_PRIVATE);
        }
        SENSITIVITY = SettingsActivity.sharedPreferences.getInt(
                SettingsActivity.SENSITIVITY_VALUE, 3);

        Log.e("sensitivity",String.valueOf(SENSITIVITY));
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //图解中已经解释三个值的含义
            X_lateral += sensorEvent.values[0];
            Y_longitudinal += sensorEvent.values[1];
            Z_vertical += sensorEvent.values[2];

            count++;

            if (count % DELAY_TIME == 0) {

                //计算和加速度
                Double length = Math.sqrt((X_lateral / (float) DELAY_TIME) * (X_lateral / (float) DELAY_TIME)
                        + (Y_longitudinal / (float) DELAY_TIME) * (Y_longitudinal / (float) DELAY_TIME)
                        + (Z_vertical / (float) DELAY_TIME) * (Z_vertical / (float) DELAY_TIME));
                //     if (mlengthList.size() < 50) {
                //         mlengthList.add(length);
                //      }
                end = System.currentTimeMillis();
                v_end= System.currentTimeMillis();
                if (end - start > MIN_INTERVAL) {

                    if (length - average > range ) {
                        if(!isWalking){
                            UNCHECKED_STEP++;
                            if(UNCHECKED_STEP>CANCEL_STEP){
                                if (v_end-v_start>LAST_TIME){
                                    UNCHECKED_STEP=0;

                                }else{
                                    isWalking=true;
                                    CURRENT_SETP+=UNCHECKED_STEP;
                                    UNCHECKED_STEP=0;
                                }
                                v_start=v_end;
                            }
                        }else{
                            CURRENT_SETP++;
                        }
                        start = end;
                    }
                }
                X_lateral = 0;
                Y_longitudinal = 0;
                Z_vertical = 0;
            }

        }
          /* if (mlengthList.size() == 40) {
                Double sum = 0.0;
                for (Double i : mlengthList) {
                    sum += i;
                }
                for (Double j : mlengthList) {
                    end = System.currentTimeMillis();
                    if (end - start > MIN_INTERVAL) {
                        if (j - average > range) {
                            CURRENT_SETP++;CURRENT_SETP++;
                            start = end;
                        }
                    }

                }
            }*/

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

}
