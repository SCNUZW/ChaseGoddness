package zy.chasegoddness.model;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import zy.chasegoddness.global.LocalDB;
import zy.chasegoddness.ui.dialog.SetAccountDialog;

/**
 * 设置女神账号的功能
 */
public class SetAccountModel {
    private SetAccountModel() {
    }

    public static SetAccountDialog showDialog(FragmentManager manager) {
        SetAccountDialog dialog = new SetAccountDialog();
        dialog.show(manager, "SetAccountDialog");
        return dialog;
    }

    public static boolean isPhoneNumExist(Context context) {
        String phoneNum = new LocalDB(context).getPhoneNum();
        return TextUtils.isEmpty(phoneNum);
    }

    /**
     * 保存设置的女神账号
     */
    public static void saveAccount(Context context, String phoneNum) {
        new LocalDB(context).putPhoneNum(phoneNum);
    }
}
