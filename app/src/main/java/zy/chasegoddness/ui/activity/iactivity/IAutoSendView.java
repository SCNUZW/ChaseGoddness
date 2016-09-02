package zy.chasegoddness.ui.activity.iactivity;

import android.support.v4.app.FragmentManager;

/**
 * Created by Administrator on 2016/8/22.
 */
public interface IAutoSendView extends IBaseView{
    void setIsAutoSend(boolean isAutoSend);

    void setAutoSendTimeHour(int hour);

    void setAutoSendTimeMinute(int minute);

    int getHourOfDay();

    int getMinute();

    boolean getIsCheckAutoSend();

    FragmentManager getSupportFragmentManager();

    void showTimePicker();

    void hideTimePicker();
}
