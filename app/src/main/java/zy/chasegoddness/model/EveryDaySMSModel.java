package zy.chasegoddness.model;

import android.content.Context;

import java.util.Calendar;
import java.util.List;

import rx.Observable;
import zy.chasegoddness.global.LocalDB;
import zy.chasegoddness.model.bean.EveryDaySMS;
import zy.chasegoddness.util.DateUtil;

/**
 * Created by Administrator on 2016/8/8.
 */
public class EveryDaySMSModel {
    private EveryDaySMSModel() {
    }

    /**
     * 获取时间（毫秒）为time的日期和好感度favorability对应的每日短信
     */
    public static Observable<EveryDaySMS> getEveryDaySMS(long time, int favorability) {
        return Observable.empty();
    }

    /**
     * 获取好感度favorability对应的每日暖话的数量
     */
    public static Observable<Integer> getCountOfEveryDaySMS(int favorability) {
        return Observable.empty();
    }

    /**
     * 获取好感度favorability对应的所有每日短信
     */
    public static Observable<List<EveryDaySMS>> getEveryDaySMS(int favorability) {
        return Observable.empty();
    }

    public static boolean isSendToday(Context context) {
        long lastSendTime = new LocalDB(context).getSendUpdate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastSendTime);

        Calendar today = Calendar.getInstance();

        return DateUtil.isSameDay(today, calendar);
    }
}
