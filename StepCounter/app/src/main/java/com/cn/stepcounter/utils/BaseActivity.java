package com.cn.stepcounter.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Window;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVUser;

/**
 * 注册登录相关服务的基类activity
 */

public class BaseActivity extends Activity {

    public BaseActivity activity;
    private String userId;
    private ProgressDialog progressDialog;
    private AVUser currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        activity = this;
        userId = null;
        currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            userId = currentUser.getObjectId();
        }
    }

    public String getUserId() {
        return userId;
    }

    public AVUser getCurrentUser() {
        return currentUser;
    }

    protected void onPause() {
        super.onPause();
        AVAnalytics.onPause(this);
    }

    protected void onResume() {
        super.onResume();
        AVAnalytics.onResume(this);
    }


}
