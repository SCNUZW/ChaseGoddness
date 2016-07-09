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

    /**
     * 检验手机号码的合法性
     * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     * 联通：130、131、132、152、155、156、185、186
     * 电信：133、153、180、189、（1349卫通）
     */
    public static boolean isPhoneNumber(String phoneNum) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(phoneNum);
        System.out.println(m.matches() + "---");
        return m.matches();
    }
}
