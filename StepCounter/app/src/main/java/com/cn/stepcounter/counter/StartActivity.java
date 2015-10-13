package com.cn.stepcounter.counter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.cn.stepcounter.R;
import com.cn.stepcounter.login.UserInfoActivity;

public class StartActivity extends Activity {

	private TextView runTextView, strongTextView;
	private TextView settingView,autoView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.start);

		runTextView = (TextView)findViewById(R.id.count);
		strongTextView = (TextView)findViewById(R.id.checkAction);
		settingView=(TextView)findViewById(R.id.setting_tv);
		autoView=(TextView)findViewById(R.id.auto_tv);

		runTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(StartActivity.this, StepCounterActivity.class);
				Bundle bundle = new Bundle(); 
				bundle.putBoolean("run", false);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		strongTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(StartActivity.this, StepCounterActivity.class);
				Bundle bundle = new Bundle(); 
				bundle.putBoolean("run", true);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		settingView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(StartActivity.this, SettingsActivity.class);
				startActivity(intent);
			}
		});

		autoView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				//Toast.makeText(StartActivity.this,"working on it",Toast.LENGTH_SHORT).show();
				Intent intent=new Intent(StartActivity.this, UserInfoActivity.class);
				startActivity(intent);
			}
		});



	}


}
