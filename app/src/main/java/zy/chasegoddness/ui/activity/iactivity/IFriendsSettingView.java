package zy.chasegoddness.ui.activity.iactivity;

import android.support.v4.app.FragmentManager;

/**
 * Created by Administrator on 2016/8/9.
 */
public interface IFriendsSettingView extends IBaseView {
    void setNickName(String name);

    void setDescription(String desc);

    String getNickName();

    String getDescription();

    void setAvatar(String url);

    void showError(String error);

    void hideError();

    FragmentManager getSupportFragmentManager();
}
