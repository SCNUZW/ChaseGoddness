package zy.chasegoddness.ui.activity.iactivity;

import android.support.v4.app.FragmentManager;

import java.util.List;

import zy.chasegoddness.model.bean.FriendsContent;

public interface IFriendsView extends IBaseView {
    FragmentManager getSupportFragmentManager();

    void setRefreshing(boolean refreshing);

    void updateList(List<FriendsContent> list);

    void setList(List<FriendsContent> list);

    void updateItem(FriendsContent item);

    void notifyChanged();
}
