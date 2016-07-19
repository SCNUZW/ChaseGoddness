package zy.chasegoddness.ui.activity.iactivity;

/**
 * 好友圈注册界面
 */
public interface IFriendsRegisterView extends IBaseView {
    void showError(String str);

    void hideError();

    String getPassword();

    String getSMSCode();

    String getPhoneNum();

    void registerUnClickable();

    void registerClickable();

    void smscodeClickable();

    void smscodeUnClickable();

    void smscodeUnClickable(int second);

    void postRunnable(Runnable runnable);
}
