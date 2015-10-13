package com.cn.stepcounter.counter;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class StepDetector implements SensorEventListener {

    public static int CURRENT_SETP = 0;

    public static float SENSITIVITY = 0;
    private static long end = 0;
    private static long start = 0;
    float X_lateral = 0;
    float Y_longitudinal = 0;
    float Z_vertical = 0;
    int DELAY_TIME = 5;
    int count = 0;
    List<Double> mlengthList = new ArrayList<Double>();
    Double average = 10.0;
    Double range = 3.0;
    Double range_b=1.5;
    private float mLastValues[] = new float[3 * 2];
    private float mScale[] = new float[2];
    private float mYOffset;

    private float mLastDirections[] = new float[3 * 2];
    private float mLastExtremes[][] = {new float[3 * 2], new float[3 * 2]};
    private float mLastDiff[] = new float[3 * 2];
    private int mLastMatch = -1;


    public StepDetector(Context context) {
        // TODO Auto-generated constructor stub
        super();
        int h = 480;
        mYOffset = h * 0.5f;
        mScale[0] = -(h * 0.5f * (1.0f / (SensorManager.STANDARD_GRAVITY * 2)));
        mScale[1] = -(h * 0.5f * (1.0f / (SensorManager.MAGNETIC_FIELD_EARTH_MAX)));
        if (SettingsActivity.sharedPreferences == null) {
            SettingsActivity.sharedPreferences = context.getSharedPreferences(
                    SettingsActivity.SETP_SHARED_PREFERENCES,
                    Context.MODE_PRIVATE);
        }
        SENSITIVITY = SettingsActivity.sharedPreferences.getInt(
                SettingsActivity.SENSITIVITY_VALUE, 3);
    }

    // public void setSensitivity(float sensitivity) {
    // SENSITIVITY = sensitivity; // 1.97 2.96 4.44 6.66 10.00 15.00 22.50
    // // 33.75
    // // 50.62
    // }

	// public void onSensorChanged(int sensor, float[] values) {
    @Override
	public void onSensorChanged(SensorEvent event) {
		// Log.i(Constant.STEP_SERVER, "StepDetector");
		Sensor sensor = event.sensor;
		// Log.i(Constant.STEP_DETECTOR, "onSensorChanged");
		synchronized (this) {
			if (sensor.getType() == Sensor.TYPE_ORIENTATION) {
			} else {
				int j = (sensor.getType() == Sensor.TYPE_ACCELEROMETER) ? 1 : 0;
				if (j == 1) {
					float vSum = 0;
					for (int i = 0; i < 3; i++) {
						final float v = mYOffset + event.values[i] * mScale[j];
						vSum += v;
					}
					int k = 0;
					float v = vSum / 3;

					float direction = (v > mLastValues[k] ? 1: (v < mLastValues[k] ? -1 : 0));
					if (direction == -mLastDirections[k]) {
						// Direction changed
						int extType = (direction > 0 ? 0 : 1); // minumum or
						// maximum?
						mLastExtremes[extType][k] = mLastValues[k];
						float diff = Math.abs(mLastExtremes[extType][k]- mLastExtremes[1 - extType][k]);

						if (diff > SENSITIVITY) {
							boolean isAlmostAsLargeAsPrevious = diff > (mLastDiff[k] * 2 / 3);
							boolean isPreviousLargeEnough = mLastDiff[k] > (diff / 3);
							boolean isNotContra = (mLastMatch != 1 - extType);

							if (isAlmostAsLargeAsPrevious && isPreviousLargeEnough && isNotContra) {
								end = System.currentTimeMillis();
								if (end - start > 500) {// ��ʱ�ж�Ϊ����һ��
									Log.i("StepDetector", "CURRENT_SETP:"
											+ CURRENT_SETP);
									CURRENT_SETP++;
									mLastMatch = extType;
									start = end;
								}
							} else {
								mLastMatch = -1;
							}
						}
						mLastDiff[k] = diff;
					}
					mLastDirections[k] = direction;
					mLastValues[k] = v;
				}
			}
		}
	}

 /*   @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //图解中已经解释三个值的含义
            X_lateral += sensorEvent.values[0];
            Y_longitudinal += sensorEvent.values[1];
            Z_vertical += sensorEvent.values[2];

            count++;

            //中值滤波
            if (count % DELAY_TIME == 0) {

                Double length = Math.sqrt((X_lateral / (float) DELAY_TIME) * (X_lateral / (float) DELAY_TIME)
                        + (Y_longitudinal / (float) DELAY_TIME) * (Y_longitudinal / (float) DELAY_TIME)
                        + (Z_vertical / (float) DELAY_TIME) * (Z_vertical / (float) DELAY_TIME));
                //     if (mlengthList.size() < 50) {
                //         mlengthList.add(length);
                //      }
                Log.e("sdf", String.valueOf(length));
                end = System.currentTimeMillis();
                if (end - start > 500) {

                    if (length - average > range || average-length>range_b) {
                        CURRENT_SETP++;
                        start = end;
                    }
                }
                X_lateral = 0;
                Y_longitudinal = 0;
                Z_vertical = 0;
            }

        }
         /*   if (mlengthList.size() == 40) {
                Double sum = 0.0;
                for (Double i : mlengthList) {
                    sum += i;
                }
                for (Double j : mlengthList) {
                    end = System.currentTimeMillis();
                    if (end - start > 500) {
                        if (j - average > range) {
                            CURRENT_SETP++;CURRENT_SETP++;
                            start = end;
                        }
                    }

                }
            }*/

    //}


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub
    }

}
