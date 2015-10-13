package com.cn.stepcounter.login;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.cn.stepcounter.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoChangePwdFragment extends Fragment {

    EditText etPassword;
    EditText etPasswordAgain;
    View viewContent;

    public UserInfoChangePwdFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewContent = inflater.inflate(R.layout.fragment_user_info_change_pwd, container, false);

        initView();
        viewContent.findViewById(R.id.id_button_complete).setOnClickListener(btnCompleteListener);

        return viewContent;
    }

    View.OnClickListener btnCompleteListener = new View.OnClickListener() {
        String pwd;
        String pwdAgain;
        @Override
        public void onClick(View v) {
            getEditInfo();
            if (checkInputValid()==false){
                return;
            }
            updatePwd();
        }

        private void getEditInfo() {
            pwd = etPassword.getText().toString();
            pwdAgain = etPasswordAgain.getText().toString();
        }

        private boolean checkInputValid() {
            if (pwd.length() < 6) {
                UserInfoChangePwdFragment.this.showErrorMessage(getActivity().getString(R.string.setuserinfo_error_password_short));
                return false;
            }
            if (!pwd.equals(pwdAgain)){
                UserInfoChangePwdFragment.this.showErrorMessage(getActivity().getString(R.string.setuserinfo_error_check_password));
                return false;
            }
            return true;
        }
        private void updatePwd() {
            try{
                AVUser currentUser = AVUser.getCurrentUser();
                AVObject objUser = (new AVQuery<AVObject>("_User")).get(currentUser.getObjectId());
                objUser.put("password", pwd);
                objUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e==null) {
                            Toast.makeText(getActivity(), R.string.fragment_user_info_change_pwd_toast_success, Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
                        } else{
                            showErrorMessage(getActivity().getString(R.string.setuserinfo_error_commit));
                            e.printStackTrace();
                        }
                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    };

    private void initView() {
        etPassword = (EditText) viewContent.findViewById(R.id.id_editText_user_password);
        etPasswordAgain = (EditText) viewContent.findViewById(R.id.id_editText_user_password_again);
    }


    private void showErrorMessage(String message) {
        new AlertDialog.Builder(getActivity())
                .setTitle(
                        getActivity().getResources().getString(
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


}
