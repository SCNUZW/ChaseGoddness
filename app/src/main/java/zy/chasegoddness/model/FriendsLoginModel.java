package zy.chasegoddness.model;


import cn.bmob.v3.BmobUser;
import rx.Observable;
import rx.schedulers.Schedulers;
import zy.chasegoddness.model.bean.User;

/**
 * 用户登录分享圈功能
 */
public class FriendsLoginModel {

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
