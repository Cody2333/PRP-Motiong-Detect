package com.cn.stepcounter.login;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.cn.stepcounter.utils.BaseActivity;
import com.cn.stepcounter.utils.ProgressDialogUtil;
import com.cn.stepcounter.R;
import com.cn.stepcounter.utils.TitleView;
import com.cn.stepcounter.counter.StartActivity;


public class RegisterActivity extends BaseActivity {

    Button loginByName;
    Button login;
    Button getCheckNumber;
    EditText userPhone;
    EditText checkNumber;
    private AVObject obj;
    private View.OnClickListener loginByNameClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(RegisterActivity.this, LogInActivity.class));
            activity.finish();
        }
    };
    private View.OnClickListener getCheckNumberClicked = new View.OnClickListener() {
        private String pattern = "^1[3|7|8][0-9]{9}$";
        private Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                int time = bundle.getInt("time");
                if (time > 0) {
                    getCheckNumber.setText(time + activity.getString(R.string.register_button_get_check_number_clicked));
                    getCheckNumber.setEnabled(false);
                } else {
                    getCheckNumber.setText(R.string.register_button_get_check_number);
                    getCheckNumber.setEnabled(true);
                }
            }
        };
        //实现1分钟内只能按一次获取验证码
        private Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    super.run();
                    int t = 60;
                    while (t >= -1) {
                        Bundle b = new Bundle();
                        Message message = new Message();
                        b.putInt("time", t);
                        message.setData(b);
                        handler.sendMessage(message);
                        t--;
                        sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };

        @Override
        public void onClick(View v) {

            String phoneNumber = getPhone();
            if (!phoneNumber.matches(pattern)) {
                showErrorMessage(activity.getString(R.string.register_error_user_phone_text));
                return;
            }
            ProgressDialogUtil.progressDialogShow(activity);
            AVOSCloud.requestSMSCodeInBackgroud(phoneNumber, activity.getString(R.string.app_name), activity.getString(R.string.register_check_phone_number), 30, new RequestMobileCodeCallback() {
                @Override
                public void done(AVException e) {
                    thread.start();
                    ProgressDialogUtil.progressDialogDismiss();
                    showHintMessage(activity.getString(R.string.register_check_number_rollback));
                    //???????[todo]
                }
            });
        }
    };
    //??????
    private View.OnClickListener loginClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String phoneNumber = getPhone();
            String checkNumber = getCheckNumber();
            ProgressDialogUtil.progressDialogShow(activity);
            AVUser.signUpOrLoginByMobilePhoneInBackground(phoneNumber, checkNumber, new LogInCallback<AVUser>() {
                @Override
                public void done(AVUser avUser, AVException e) {
                    ProgressDialogUtil.progressDialogDismiss();
                    if (avUser != null) {
                        startNextActivity(avUser);
                    } else {
                        showErrorMessage(activity.getString(R.string.register_error_check_number));
                    }
                }
            });
        }


    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AVAnalytics.trackAppOpened(getIntent());
        initView();

        String id = getUserId();
        if (getCurrentUser() != null) {
            startNextActivity(getCurrentUser());
        }

        initListener();

    }

    private void initListener() {
        login.setOnClickListener(loginClicked);
        loginByName.setOnClickListener(loginByNameClicked);
        getCheckNumber.setOnClickListener(getCheckNumberClicked);
    }

    private void startNextActivity(final AVUser currentUser) {
 /*       if (currentUser.getBoolean("completed") == false) {
            new AlertDialog.Builder(activity)
                    .setTitle(
                            activity.getResources().getString(
                                    R.string.dialog_error_title))
                    .setMessage(activity.getString(R.string.register_error_info_not_completed))
                    .setNegativeButton(android.R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();
                                    Intent i = new Intent(RegisterActivity.this, SetUserInfoActivity.class);
                                    i.putExtra("phoneNumber", currentUser.getString("mobilePhoneNumber"));
                                    startActivity(i);
                                    activity.finish();
                                }
                            }).show();
        } else {*/
            startActivity(new Intent(RegisterActivity.this, StartActivity.class));
            activity.finish();
        //}
    }

    private void startNextAty() {
        new AsyncTask<Void, Void, Void>() {
            //刷新过程中需要做的操作在这里
            protected Void doInBackground(Void... params) {
                try {
                    obj = getUserObject();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                    startActivity(new Intent(RegisterActivity.this, StartActivity.class));
                    activity.finish();
            }
        }.execute();
    }


    //获取用户对象
    private AVObject getUserObject() {
        try {
            AVQuery<AVObject> query = new AVQuery<AVObject>("_User");
            return query.get(AVUser.getCurrentUser().getObjectId());
        } catch (AVException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String getPhone() {
        String result = userPhone.getText().toString();
        if (result.length() != 11) {
            return "";
        }
        return result;
    }

    private String getCheckNumber() {
        String result = checkNumber.getText().toString();
        return result;
    }

    //显示错误信息
    private void showErrorMessage(String message) {
        new AlertDialog.Builder(activity)
                .setTitle(
                        activity.getResources().getString(
                                R.string.dialog_error_title))
                .setMessage(message)
                .setNegativeButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }

    //显示提示信息
    private void showHintMessage(String message) {
        new AlertDialog.Builder(activity)
                .setTitle(
                        activity.getResources().getString(
                                R.string.dialog_message_title))
                .setMessage(message)
                .setNegativeButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();
    }

    private void initView() {
        loginByName = (Button) findViewById(R.id.register_button_login_by_name);
        login = (Button) findViewById(R.id.register_button_login);
        getCheckNumber = (Button) findViewById(R.id.register_button_get_check_number);
        userPhone = (EditText) findViewById(R.id.register_phone_number);
        checkNumber = (EditText) findViewById(R.id.register_check_number);

        TitleView tv = (TitleView) findViewById(R.id.register_title_view);
        tv.hideLeftButton();

        tv.hideRightButton();
        tv.setTitleText(activity.getString(R.string.register_title_text));
    }
}