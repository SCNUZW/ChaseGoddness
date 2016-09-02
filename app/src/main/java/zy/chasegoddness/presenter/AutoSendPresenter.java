package zy.chasegoddness.presenter;

import android.util.Log;

import zy.chasegoddness.global.LocalDB;
import zy.chasegoddness.model.EveryDaySMSModel;
import zy.chasegoddness.model.FormatCheckModel;
import zy.chasegoddness.model.bean.EveryDaySMS;
import zy.chasegoddness.ui.activity.AutoSendActivity;
import zy.chasegoddness.ui.activity.iactivity.IAutoSendView;
import zy.chasegoddness.ui.dialog.SetAccountDialog;

/**
 * Created by Administrator on 2016/8/22.
 */
public class AutoSendPresenter {
    private IAutoSendView view;
    private LocalDB db;

    public AutoSendPresenter(AutoSendActivity view) {
        this.view = view;
        db = new LocalDB(view.getContext());
    }

    public void init() {
        boolean isAutoSend = db.getAutoSend();
        int hour = db.getAutoSendHour();
        int minute = db.getAutoSendMinute();
        if (hour != -1) {
            view.setAutoSendTimeHour(hour);
        }
        if (minute != -1) {
            view.setAutoSendTimeMinute(minute);
        }
        if (isAutoSend) {
            view.setIsAutoSend(true);
            view.showTimePicker();
        } else {
            view.setIsAutoSend(false);
            view.hideTimePicker();
        }
    }

    public void setAutoSend() {
        boolean isAutoSend = view.getIsCheckAutoSend();
        int hour = view.getHourOfDay();
        int minute = view.getMinute();

        String phone = db.getPhoneNum();
        if (isAutoSend &&
                (phone == null || !FormatCheckModel.isPhoneNumber(phone))) {
            view.setIsAutoSend(false);
            SetAccountDialog.showDialog(view.getSupportFragmentManager());
        }

        Log.i("zy", "AutoSendPresenter update auto send status: hour = " + hour + " minute = " + minute + " isAuto = " + isAutoSend);
        db.putAutoSend(isAutoSend);
        db.putAutoSendHour(hour);
        db.putAutoSendMinute(minute);

        if (isAutoSend) {
            EveryDaySMSModel.startAutoSendService(view.getContext(), hour, minute);
        } else {
            EveryDaySMSModel.cancelAutoSendService(view.getContext());
        }
    }
}
