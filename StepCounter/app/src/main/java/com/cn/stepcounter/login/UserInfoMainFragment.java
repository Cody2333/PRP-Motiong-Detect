package com.cn.stepcounter.login;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVUser;
import com.cn.stepcounter.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoMainFragment extends Fragment implements View.OnClickListener {

    public UserInfoMainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_user_info_main, container, false);

        contentView.findViewById(R.id.id_change_password).setOnClickListener(this);
        contentView.findViewById(R.id.id_change_phone).setOnClickListener(this);
        contentView.findViewById(R.id.btn_setting_logout).setOnClickListener(this);
        return contentView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_change_password:
                getFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.id_main_content, new UserInfoChangePwdFragment())
                        .commit();
//                event.setFlag(UserInfoActivity.FragEnum.MODIFY_PWD);
                break;
            case R.id.id_change_phone:
                getFragmentManager().beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.id_main_content, new UserInfoChangePhoneFragment())
                        .commit();
//                event.setFlag(UserInfoActivity.FragEnum.MODIFY_PHONE);
                break;
            case R.id.btn_setting_logout:
                new AlertDialog.Builder(getActivity())
                        .setTitle(
                                getActivity().getResources().getString(
                                        R.string.dialog_message_title))
                        .setMessage(
                                getActivity().getResources().getString(
                                        R.string.action_logout_alert_message))
                        .setNegativeButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                        Logout();
                                    }
                                })
                        .setPositiveButton(android.R.string.cancel,
                                new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.dismiss();
                                    }
                                }).show();

                break;

        }
    }

    //登出
    private void Logout() {
        AVUser.logOut();
        Intent i = new Intent(getActivity(), RegisterActivity.class);
        startActivity(i);
        getActivity().finish();


    }
}
