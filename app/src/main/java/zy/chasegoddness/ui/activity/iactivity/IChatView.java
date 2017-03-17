package zy.chasegoddness.ui.activity.iactivity;

import android.support.v4.app.FragmentManager;

import java.util.List;

import zy.chasegoddness.model.bean.LocalSms;

/**
 * Created by Administrator on 2016/7/2.
 */
public interface IChatView extends IBaseView{
    void showSms(List<LocalSms> list);

    void insertSms(LocalSms sms);

    void setRefreshing(boolean refreshing);

    void notifyDataSetChanged(int resultSize);

    void clearEditText();

    void setReply1(String reply);
    void setReply2(String reply);
    void setReply3(String reply);
    void setReplyOnEditText(String reply);

    FragmentManager getSupportFragmentManager();
}
