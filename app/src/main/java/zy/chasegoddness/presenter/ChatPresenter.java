package zy.chasegoddness.presenter;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import zy.chasegoddness.global.LocalDB;
import zy.chasegoddness.model.LocalSmsModel;
import zy.chasegoddness.model.SetAccountModel;
import zy.chasegoddness.model.bean.LocalSms;
import zy.chasegoddness.ui.activity.ChatActivity;
import zy.chasegoddness.ui.activity.iactivity.IChatView;
import zy.chasegoddness.ui.dialog.SetAccountDialog;

/**
 * 聊天页面控制器
 */
public class ChatPresenter {

    private IChatView view;

    private String phoneNum;

    private int pageNum = 0;
    private int pageSize = 13;
    private int resultSize = 0;

    public ChatPresenter(ChatActivity view) {
        this.view = view;
    }

    public void refreshDate() {
        if (phoneNumNotExist()) {
            view.setRefreshing(false);
            SetAccountDialog.showDialog(view.getSupportFragmentManager());
        } else {
            view.setRefreshing(true);
            LocalSmsModel.getLocalSmsList(view.getContext(), phoneNum, pageNum, pageSize)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<LocalSms>() {
                        @Override
                        public void onStart() {
                            resultSize = 0;
                        }

                        @Override
                        public void onCompleted() {
                            if (resultSize == 0)
                                view.showToast("已经到顶了", Gravity.CENTER | Gravity.TOP, 0, 120);
                            else
                                view.notifyDataSetChanged(resultSize);

                            view.setRefreshing(false);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("zy", "ChatPresenter refreshDate error " + e);
                            view.showToast("可怕的错误发生了");
                        }

                        @Override
                        public void onNext(LocalSms localSms) {
                            view.insertSms(localSms);
                            resultSize++;
                        }
                    });
            //下次更新数据时 查找下一页的数据
            pageNum++;
        }
    }

    public void sendSms(String str) {
        if (TextUtils.isEmpty(str)) {
            view.showToast("发送内容不能为空");
        } else {
            view.clearEditText();
        }
        Log.i("zy", "发送短信：" + str);
    }

    private boolean phoneNumNotExist() {
        phoneNum = new LocalDB(view.getContext()).getPhoneNum();
        return !phoneNumExist();
    }

    public boolean phoneNumExist() {
        return phoneNum != null && phoneNum != "";
    }

    public int getPageSize() {
        return pageSize;
    }
}
