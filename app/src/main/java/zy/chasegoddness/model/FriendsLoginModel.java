package zy.chasegoddness.model;

import android.support.v4.app.FragmentManager;

import zy.chasegoddness.ui.dialog.FriendsLoginDialog;

/**
 * 用户登录分享圈功能
 */
public class FriendsLoginModel {
    public static FriendsLoginDialog showDialog(FragmentManager manager) {
        FriendsLoginDialog dialog = new FriendsLoginDialog();
        dialog.show(manager, "FriendsLoginDialog");
        return dialog;
    }
}
