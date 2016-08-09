package zy.chasegoddness.presenter;

import android.util.Log;

import cn.bmob.v3.BmobUser;
import rx.android.schedulers.AndroidSchedulers;
import zy.chasegoddness.model.FriendsContentModel;
import zy.chasegoddness.model.bean.User;
import zy.chasegoddness.ui.activity.FriendsActivity;
import zy.chasegoddness.ui.activity.ShowBigImageActivity;
import zy.chasegoddness.ui.activity.iactivity.IFriendsView;
import zy.chasegoddness.ui.dialog.FriendsLoginDialog;

/**
 * 分享圈的控制器
 */
public class FriendsContentPresenter {

    private IFriendsView view;
    private User currentUser;
    private int pageNum = 1;
    private int pageSize = 10;

    public FriendsContentPresenter(FriendsActivity view) {
        this.view = view;
    }

    public void init() {
        checkForLogin();
        refresh();
    }

    /**
     * 刷新最新的分享圈事件
     */
    public void refresh() {
        view.setRefreshing(true);
        view.notifyChanged();
        pageNum = 1;
        FriendsContentModel.getFriendsContent(pageNum, pageSize)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(friendsContents -> {
                    if (friendsContents.size() == 0)
                        view.showToast("一条数据都没有哦");
                    view.setList(friendsContents);
                }, throwable -> {
                    Log.e("zy", "Friends Presenter get FriendsContent error: " + throwable.toString());
                    view.setRefreshing(false);
                    view.showToast("一个可怕的错误发生了");
                }, () -> {
                    view.setRefreshing(false);
                    view.notifyChanged();
                });
    }

    /**
     * 加载更多的分享圈事件
     */
    public void loadMore() {
        view.setRefreshing(true);
        pageNum++;
        FriendsContentModel.getFriendsContent(pageNum, pageSize)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(friendsContents -> {
                    if (friendsContents.size() == 0) {
                        view.showToast("已经到底了");
                    } else {
                        view.updateList(friendsContents);
                        view.notifyChanged();
                    }
                }, throwable -> {
                    view.showToast("一个可怕的错误发生了");
                    view.setRefreshing(false);
                    Log.e("zy", "Friends Presenter get FriendsContent error: " + throwable.toString());
                }, () -> view.setRefreshing(false));
    }

    /**
     * 如果没登陆 弹出登录对话框
     */
    public boolean checkForLogin() {
        if (currentUser != null) return true;

        currentUser = BmobUser.getCurrentUser(User.class);
        if (currentUser != null) {
            return true;
        } else {
            FriendsLoginDialog.showDialog(view.getSupportFragmentManager());
            return false;
        }
    }

    /**
     * 打开查看大图界面
     */
    public void showBigImage(String url) {
        ShowBigImageActivity.startActivity(view.getContext(), url);
    }
}
