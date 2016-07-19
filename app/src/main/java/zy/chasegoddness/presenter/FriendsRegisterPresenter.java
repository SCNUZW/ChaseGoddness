package zy.chasegoddness.presenter;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import rx.android.schedulers.AndroidSchedulers;
import zy.chasegoddness.model.FormatCheckModel;
import zy.chasegoddness.model.FriendsRegisterModel;
import zy.chasegoddness.model.FriendsSMSCodeModel;
import zy.chasegoddness.model.bean.User;
import zy.chasegoddness.ui.activity.FriendsRegisterActivity;
import zy.chasegoddness.ui.activity.iactivity.IFriendsRegisterView;

/**
 * Created by Administrator on 2016/7/19.
 */
public class FriendsRegisterPresenter {

    private IFriendsRegisterView view;
    private int unClickableTime = 0;

    public FriendsRegisterPresenter(FriendsRegisterActivity view) {
        this.view = view;
    }

    public void init() {
        view.hideError();
    }

    public void register() {
        view.registerUnClickable();
        view.hideError();

        final String phone = view.getPhoneNum();
        final String pwd = view.getPassword();
        final String smsCode = view.getSMSCode();

        //1.检查手机号码
        if (!FormatCheckModel.isPhoneNumber(phone)) {
            view.showError("请输入合法的手机号码");
            return;
        }

        //2.封装成FamilyUser账号信息
        User user = new User();
        user.setMobilePhoneNumber(phone);
        user.setPassword(pwd);
        user.setUsername(phone);

        //3.注册
        FriendsRegisterModel.register(user, smsCode)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(u -> {/* 成功注册 u是当前用户*/
                            view.registerClickable();
                            view.finish();

                        }, throwable -> {/*验证码不正确 或者 注册用户信息失败 */
                            String e = throwable.toString();
                            Log.e("zy", "RegisterPresenter register error: " + e);

                            view.registerClickable();
                            if (e.startsWith("errorCode:401")) {//401主键重复
                                view.showError("手机号码已注册");
                            } else if (e.startsWith("errorCode:304")) {
                                view.showError("手机号码或者密码为空");
                            } else if (e.startsWith("errorCode:207")) {
                                view.showError("注册码错误");
                            } else {
                                view.showError("注册失败，请检查网络或验证码");
                            }
                        }

                );
    }

    public void requestSMSCode() {
        view.smscodeUnClickable();
        view.hideError();

        //1.检查手机号码
        final String phone = view.getPhoneNum();
        if (!FormatCheckModel.isPhoneNumber(phone)) {
            view.showError("请输入合法的手机号码");
            return;
        }

        //2.一定时间内不能再次点击获取验证码按钮
        final TimerTask task = countUnClickableTime();

        //3.获取验证码
        FriendsSMSCodeModel.requestSMSCode(phone)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(smsId -> {/* 发送验证码成功 smsId是验证码编号（用于跟踪详情）而不是验证码 */
                    Log.i("zy", "RegisterPresenter request smsCode: " + smsId);
                }, throwable -> {
                    view.showError("获取验证码失败");
                    task.cancel();
                    view.smscodeClickable();
                    Log.e("zy", "RegisterPresenter request smsCode Error:" + throwable);
                });
    }

    /**
     * 点击获取验证码按钮后开始计时 60秒内不能重新获取
     */
    private TimerTask countUnClickableTime() {
        unClickableTime = 60;
        TimerTask task;
        new Timer().scheduleAtFixedRate(task = new TimerTask() {
            @Override
            public void run() {
                view.postRunnable(() -> {
                    if (unClickableTime > 0)
                        view.smscodeUnClickable(unClickableTime--);
                    else
                        view.smscodeClickable();
                });
            }
        }, 0, 1000);
        return task;
    }
}
