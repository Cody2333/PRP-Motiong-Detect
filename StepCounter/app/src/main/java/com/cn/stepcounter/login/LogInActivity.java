package com.cn.stepcounter.login;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.cn.stepcounter.BaseActivity;
import com.cn.stepcounter.ErrorAlertDialogUtil;
import com.cn.stepcounter.ProgressDialogUtil;
import com.cn.stepcounter.R;
import com.cn.stepcounter.TitleView;
import com.cn.stepcounter.counter.StartActivity;

public class LogInActivity extends BaseActivity {


    private Button loginButton;
    private Button backButton;
    private Button forgetPasswordButton;
    private EditText userPasswordEditText;
    private EditText userPhoneEditText;
//    private ProgressDialog progressDialog;
    private Boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        AVAnalytics.trackAppOpened(getIntent());
        initView();



        loginButton.setOnClickListener(loginListener);
        backButton.setOnClickListener(backListener);
        forgetPasswordButton.setOnClickListener(forgetPasswordListener);
//        forgetPasswordButton.setOnClickListener(forgetPasswordListener);


    }

    View.OnClickListener backListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            startActivity(new Intent(LogInActivity.this, RegisterActivity.class));
            activity.finish();
        }
    };

    View.OnClickListener forgetPasswordListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Toast.makeText(LogInActivity.this, R.string.login_hint_forget_password, Toast.LENGTH_SHORT).show();
        }
    };

    public void initView() {
        loginButton = (Button) findViewById(R.id.button_login);
        userPasswordEditText = (EditText) findViewById(R.id.editText_userPassword);
        userPhoneEditText = (EditText)findViewById(R.id.editText_userPhone);
        forgetPasswordButton = (Button)findViewById(R.id.button_forget_password);
        TitleView tv = (TitleView)findViewById(R.id.login_title_view);
        tv.setTitleText(activity.getString(R.string.login_title_text));
        tv.hideRightButton();
        backButton = tv.getLeftButton();
    }


    View.OnClickListener loginListener = new View.OnClickListener() {

        @SuppressLint("NewApi")
        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public void onClick(View arg0) {
            String userPhone = userPhoneEditText.getText().toString();
            String password = password();
            if (userPhone.isEmpty()) {
                //showUserPhoneEmptyError();
                ErrorAlertDialogUtil.showErrorDialog(activity, activity.getResources().getString(R.string.login_error_phone_null));
                return;
            }
            if (userPhone.length() != 11) {
                //showUserPhoneLengthError();
                ErrorAlertDialogUtil.showErrorDialog(activity,activity.getResources().getString(R.string.register_error_user_phone_length));
                return;
            }
            if (password.isEmpty()) {
                //showUserPasswordEmptyError();
                ErrorAlertDialogUtil.showErrorDialog(activity,activity.getResources().getString(R.string.register_error_password_null));
                return;
            }
            ProgressDialogUtil.progressDialogShow(activity);
            AVUser.loginByMobilePhoneNumberInBackground(userPhone, password, new LogInCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    if (avUser != null) {
                        ProgressDialogUtil.progressDialogDismiss();
                        Intent mainIntent = new Intent(activity,
                                StartActivity.class);
                        startActivity(mainIntent);
                        activity.finish();
                    } else {
                        ProgressDialogUtil.progressDialogDismiss();
                        //showLoginError();
                        ErrorAlertDialogUtil.showErrorDialog(activity, activity.getResources().getString(R.string.login_error_login));
                    }
                }
            });
        }

        private String password() {
            return userPasswordEditText.getText().toString();
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK)
            backButton.callOnClick();
        return false;
    }



    @Override
    public void onStart() {
        super.onStart();
        //EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        //EventBus.getDefault().unregister(this);
        super.onStop();
    }

}
