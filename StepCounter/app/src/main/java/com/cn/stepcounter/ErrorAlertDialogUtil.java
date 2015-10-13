package com.cn.stepcounter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by 科迪 on 2015/8/10.
 */
public class ErrorAlertDialogUtil {

    public static void showErrorDialog(Context ctx, String errorStr) {
        new AlertDialog.Builder(ctx)
                .setTitle(
                        ctx.getResources().getString(
                                R.string.dialog_error_title))
                .setMessage(
                        errorStr)
                .setNegativeButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        }).show();

    }
}
