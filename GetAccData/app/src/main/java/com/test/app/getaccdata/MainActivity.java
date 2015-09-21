package com.test.app.getaccdata;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

import java.text.DecimalFormat;

public class MainActivity extends Activity {

    //设置数字滤波的频率
    int DELAY_TIME = 6;

    long end = 0;
    long start = 0;

    long endQ = 0;
    long startQ = 0;

    float X_lateral = 0;
    float Y_longitudinal = 0;
    float Z_vertical = 0;

    float XQ = 0;
    float YQ = 0;
    float ZQ = 0;

    DecimalFormat df = new DecimalFormat("0.0000 ");
    private SensorManager sm;

    private Sensor accelerometer; // 加速度传感器
    private Sensor magnetic; // 地磁场传感器


    private TextView XX, YY, ZZ, length_tv,QQ,WW,EE;
    private Button btn;

    private String lengthList = "";
    private String Xlist = "";
    private String Ylist = "";
    private String Zlist = "";
    private String XQlist = "";
    private String YQlist = "";
    private String ZQlist = "";
    private String TimeList = "";
    private String TimeListQ = "";
    private int count = 0;
    private int countQ = 0;

    private Boolean isSaved = false;
    private Boolean isStarted = false;
    /*
   * SensorEventListener接口的实现，需要实现两个方法
   * 方法1 onSensorChanged 当数据变化的时候被触发调用
   * 方法2 onAccuracyChanged 当获得数据的精度发生变化的时候被调用，比如突然无法获得数据时
   * */
    final SensorEventListener myAccelerometerListener = new SensorEventListener() {

        //复写onSensorChanged方法
        public void onSensorChanged(SensorEvent sensorEvent) {

            synchronized (this){
                if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER && isStarted) {
                    //图解中已经解释三个值的含义
                    X_lateral += sensorEvent.values[0];
                    Y_longitudinal += sensorEvent.values[1];
                    Z_vertical += sensorEvent.values[2];

                    count++;

                    //中值滤波
                    if (count % DELAY_TIME == 0) {
                        XX.setText("x=" + df.format(X_lateral / (float) DELAY_TIME));
                        YY.setText("y=" + df.format(Y_longitudinal / (float) DELAY_TIME));
                        ZZ.setText("z=" + df.format(Z_vertical / (float) DELAY_TIME));
                        double length = Math.sqrt((X_lateral / (float) DELAY_TIME) * (X_lateral / (float) DELAY_TIME)
                                + (Y_longitudinal / (float) DELAY_TIME) * (Y_longitudinal / (float) DELAY_TIME)
                                + (Z_vertical / (float) DELAY_TIME) * (Z_vertical / (float) DELAY_TIME));
                        length_tv.setText("length= " + df.format(length));
                        lengthList += df.format(length) + ",";
                        Xlist += df.format(X_lateral / (float) DELAY_TIME) + ",";
                        Ylist += df.format(Y_longitudinal / (float) DELAY_TIME) + ",";
                        Zlist += df.format(Z_vertical / (float) DELAY_TIME) + ",";
                        if (end == 0) {
                            end = System.currentTimeMillis();
                            start = System.currentTimeMillis();
                            TimeList += end - start + ",";
                        } else {
                            end = System.currentTimeMillis();
                            TimeList += end - start + ",";

                        }

                        X_lateral = 0;
                        Y_longitudinal = 0;
                        Z_vertical = 0;
                        Log.i("cody", String.valueOf(count / DELAY_TIME));

                    }
                } else if (sensorEvent.sensor.getType() == Sensor.TYPE_ORIENTATION && isStarted) {
                    XQ += sensorEvent.values[0];
                    YQ += sensorEvent.values[1];
                    ZQ += sensorEvent.values[2];
                    countQ++;

                    //中值滤波
                    if (count % (DELAY_TIME/2) == 0) {

                        QQ.setText("AZ=" + df.format(XQ / (float) DELAY_TIME));
                        WW.setText("PI=" + df.format(YQ / (float) DELAY_TIME));
                        EE.setText("RO=" + df.format(ZQ / (float) DELAY_TIME));

                        XQlist += df.format(XQ / (float) DELAY_TIME) + ",";
                        YQlist += df.format(YQ / (float) DELAY_TIME) + ",";
                        ZQlist += df.format(ZQ / (float) DELAY_TIME) + ",";

                        if (endQ == 0) {
                            endQ = System.currentTimeMillis();
                            startQ = System.currentTimeMillis();
                            TimeListQ += endQ - startQ + ",";
                        } else {
                            endQ = System.currentTimeMillis();
                            TimeListQ += endQ - startQ + ",";

                        }


                        XQ = 0;
                        YQ = 0;
                        ZQ = 0;

                    }
                }
            }

        }

        //复写onAccuracyChanged方法
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            //ACT.setText("onAccuracyChanged被触发");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AVOSCloud.initialize(this, "DxyghogX5MvJgd9gneilVjLd", "Q9VATuKisrXjquR5O54dovVF");
        initView();
        initListener();
        initSensor();

    }

    private void initSensor() {  //创建一个SensorManager来获取系统的传感器服务
        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        //选取加速度感应器
        int sensorType = Sensor.TYPE_ACCELEROMETER;
        /*
         * 最常用的一个方法 注册事件
         * 参数1 ：SensorEventListener监听器
         * 参数2 ：Sensor 一个服务可能有多个Sensor实现，此处调用getDefaultSensor获取默认的Sensor
         * 参数3 ：模式 可选数据变化的刷新频率
         * */
        sm.registerListener(myAccelerometerListener, sm.getDefaultSensor(sensorType), SensorManager.SENSOR_DELAY_FASTEST);
        sm.registerListener(myAccelerometerListener, sm.getDefaultSensor(Sensor.TYPE_ORIENTATION), SensorManager.SENSOR_DELAY_FASTEST);
    }

    private void initListener() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStarted == false) {
                    isStarted = true;
                    btn.setText("停止");
                } else {
                    isStarted = false;
                    btn.setText("开始记录");
                    doSave();
                }

            }
        });
    }

    private void initView() {
        XX = (TextView) findViewById(R.id.XX);
        YY = (TextView) findViewById(R.id.YY);
        ZZ = (TextView) findViewById(R.id.ZZ);
        QQ = (TextView) findViewById(R.id.QQ);
        WW = (TextView) findViewById(R.id.WW);
        EE = (TextView) findViewById(R.id.EE);
        length_tv = (TextView) findViewById(R.id.tv_length);
        btn = (Button) findViewById(R.id.btn_start);
    }


    private void doSave() {
        final AVFile avFile;
        final AVObject avObject = new AVObject("Post");
        avFile = new AVFile("dataRecord", (lengthList + "/r/n" + Xlist + "/r/n" + Ylist + "/r/n" + Zlist + "/r/n" + TimeList+
                "/r/n" + XQlist + "/r/n" + YQlist + "/r/n" + ZQlist+ "/r/n"+TimeListQ).getBytes());
        avFile.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                avObject.put("content", "changedData");
                avObject.put("attached", avFile);
                avObject.saveInBackground();
            }
        });
        lengthList = "";
        Xlist = "";
        Ylist = "";
        Zlist = "";
        XQlist = "";
        YQlist = "";
        ZQlist = "";
        TimeList = "";
        TimeListQ="";
        end = 0;
        start = 0;
        endQ = 0;
        startQ = 0;
    }


}
