package com.cn.stepcounter.login;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.cn.stepcounter.BaseActivity;
import com.cn.stepcounter.R;
import com.cn.stepcounter.TitleView;


public class UserInfoActivity extends BaseActivity {

    private Button btnBack;
    private FragmentManager fm;
    private FragmentTransaction ft;
    private Fragment fragMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initView();
        initFragment();

        btnBack.setOnClickListener(btn_back_listener);
    }

    View.OnClickListener btn_back_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onBackPressed();
        }
    };

    private void initFragment() {
        fragMain = new UserInfoMainFragment();
        ft.add(R.id.id_main_content, fragMain);
        ft.commit();
    }


    private void initView(){
        TitleView tv = (TitleView)findViewById(R.id.id_title_view);
        btnBack = tv.getLeftButton();
        tv.hideRightButton();
        tv.setTitleText(activity.getString(R.string.persondetail_title_text));

        fm = getFragmentManager();
        ft = fm.beginTransaction();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) { //按下的如果是BACK，同时没有重复

            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (fm.findFragmentById(R.id.id_main_content) instanceof UserInfoMainFragment) {
            this.finish();
        }
        else {
            fm.popBackStack();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
