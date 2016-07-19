package zy.chasegoddness.model;

import rx.Observable;
import rx.schedulers.Schedulers;
import zy.chasegoddness.model.bean.User;

/**
 * 分享圈注册功能
 */
public class FriendsRegisterModel {

    private FriendsRegisterModel() {
    }

    /**
     * 注册新用户
     *
     * @param user 新用户的信息
     * @param code 短信验证码
     */
    public static Observable<User> register(final User user, String code) {
        return FriendsSMSCodeModel.vertifySMSCode(user.getMobilePhoneNumber(), code)/*验证码验证*/
                .flatMap(aVoid -> {/*如果验证码正确 上传用户信息*/
                    return user.signUpObservable(User.class);
                });
    }
}
