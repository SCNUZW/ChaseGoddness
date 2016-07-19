package zy.chasegoddness.model;

import cn.bmob.v3.BmobSMS;
import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2016/7/19.
 */
public class FriendsSMSCodeModel {
    /**
     * 短信模板的名字
     */
    public static final String TEMPLE = "用户注册";

    private FriendsSMSCodeModel() {
    }

    /**
     * 获取验证码到指定手机号码
     */
    public static Observable<Integer> requestSMSCode(String number) {
        return BmobSMS.requestSMSCodeObservable(number, TEMPLE)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 验证指定验证码
     */
    public static Observable<Void> vertifySMSCode(String phoneNum, String code) {
        return BmobSMS.verifySmsCodeObservable(phoneNum, code)
                .subscribeOn(Schedulers.io());
    }
}
