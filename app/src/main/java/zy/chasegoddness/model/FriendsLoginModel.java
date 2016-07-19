package zy.chasegoddness.model;

import android.support.v4.app.FragmentManager;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import rx.Observable;
import rx.schedulers.Schedulers;
import zy.chasegoddness.model.bean.User;
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

    /**
     * 登陆
     */
    public static Observable<User> login(User user) {
        return user.loginObservable(User.class)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 注销账号
     */
    public static void logOut() {
        User user = getCurrentUser();
        if (user != null)
            user.logOut();
    }

    /**
     * 获取当前登录的账号 可能为null
     */
    public static User getCurrentUser() {
        return BmobUser.getCurrentUser(User.class);
    }
}
