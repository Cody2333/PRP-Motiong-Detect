package com.cn.stepcounter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;


public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		
		if (StepCounterService.FLAG || StepDetector.CURRENT_SETP > 0) {
			Intent intent = new Intent(SplashActivity.this, StepCounterActivity.class);
																				
			startActivity(intent);
			this.finish();
		} else {
			new CountDownTimer(2000L, 1000L)
			{
				public void onFinish()
				{

					Intent intent = new Intent();
					intent.setClass(SplashActivity.this, StartActivity.class);
					startActivity(intent);
					overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
					finish();

				}

				public void onTick(long paramLong)
				{
				}
			}
			.start();
		}
	}

}

