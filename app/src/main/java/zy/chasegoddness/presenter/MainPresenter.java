package zy.chasegoddness.presenter;

import android.util.Log;
import android.util.TimeUtils;

import java.util.Calendar;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import zy.chasegoddness.global.LocalDB;
import zy.chasegoddness.model.EveryDaySMSModel;
import zy.chasegoddness.model.bean.EveryDaySMS;
import zy.chasegoddness.ui.activity.AutoSendActivity;
import zy.chasegoddness.ui.activity.MainActivity;
import zy.chasegoddness.ui.activity.iactivity.IMainView;
import zy.chasegoddness.util.TimeUtil;

/**
 * Created by Administrator on 2016/8/18.
 */
public class MainPresenter {
    private IMainView view;
    private LocalDB db;

    public MainPresenter(MainActivity view) {
        this.view = view;
        this.db = new LocalDB(view.getContext());
    }

    public void init() {
        int favorability = db.getFavorability();
        EveryDaySMSModel.getEveryDaySMS(System.currentTimeMillis(), favorability)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(everyDaySMS -> {
                    view.setEveryDaySMS(everyDaySMS.getContent());
                }, throwable -> {
                    view.setEveryDaySMSError("获取每日暖话失败");
                    Log.e("zy", "MainPresenter init getEveryDaySMS error: " + throwable);
                });

        view.setFavorability(favorability);
    }

    public void resume() {
        boolean isAutoSend = db.getAutoSend();
        view.setAutoSend(isAutoSend);

        long sendDate = db.getSendUpdate();
        if (sendDate != 0) {
            Calendar sendDay = Calendar.getInstance();
            sendDay.setTimeInMillis(sendDate);
            if (TimeUtil.isSameDay(Calendar.getInstance(), sendDay)) {
                view.setIsSendToday(true);
                return;
            }
        }
        view.setIsSendToday(false);
    }

    public void autoSend(boolean autoSend) {
        //设置成可以自动发送
        if (autoSend) {
            int hour = db.getAutoSendHour();
            int minute = db.getAutoSendMinute();
            if (hour == -1 || minute == -1) {//还没设置好时间
                //跳转到设置自动发送时间的页面
                AutoSendActivity.startActivity(view.getContext());
            } else {
                db.putAutoSend(true);
                EveryDaySMSModel.startAutoSendService(view.getContext(), hour, minute);
            }
        } else {
            db.putAutoSend(false);
            EveryDaySMSModel.cancelAutoSendService(view.getContext());
        }
    }
}
