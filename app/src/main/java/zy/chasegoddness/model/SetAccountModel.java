package zy.chasegoddness.model;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import zy.chasegoddness.global.LocalDB;
import zy.chasegoddness.ui.dialog.SetAccountDialog;

/**
 * Created by Administrator on 2016/7/5.
 */
public class SetAccountModel {
    public void showDialog(FragmentManager manager) {
        new SetAccountDialog().show(manager, "SetAccountDialog");
    }

    public static boolean isPhoneNumExist(Context context) {
        String phoneNum = new LocalDB(context).getPhoneNum();
        return TextUtils.isEmpty(phoneNum);
    }

    /**
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
