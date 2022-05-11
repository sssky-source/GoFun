package com.coolweather.gofun.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogUtils {

    private static DialogUtils instance = null;

    public static DialogUtils getInstance() {
        if (instance == null) {
            instance = new DialogUtils();
        }
        return instance;
    }

    private DialogUtils(){}

    public void showDialog(Context context, String titleInfo, final DialogUtils.DialogCallBack callBack) {
        AlertDialog.Builder alterDialog = new AlertDialog.Builder(context);
        alterDialog.setMessage(titleInfo);
        alterDialog.setCancelable(true);

        alterDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callBack.OkEvent();
            }
        });
        alterDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alterDialog.show();
    }

    public interface DialogCallBack {
        void OkEvent();
    }
}
