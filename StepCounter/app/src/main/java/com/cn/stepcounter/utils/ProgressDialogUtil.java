package com.cn.stepcounter.utils;

import android.app.ProgressDialog;
import android.content.Context;

import com.cn.stepcounter.R;

/**
 * Created by 科迪 on 2015/7/17.
 */
public class ProgressDialogUtil {
    private static ProgressDialog progressDialog;

    public static void progressDialogDismiss() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    public static void progressDialogShow(Context ctx) {
        progressDialog = ProgressDialog
                .show(ctx,
                        ctx.getResources().getText(
                                R.string.dialog_message_title),
                        ctx.getResources().getText(
                                R.string.dialog_text_wait), true, true);
    }

}
