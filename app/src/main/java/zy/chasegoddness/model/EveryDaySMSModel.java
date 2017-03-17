package zy.chasegoddness.model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import rx.Observable;
import rx.schedulers.Schedulers;
import zy.chasegoddness.global.LocalDB;
import zy.chasegoddness.model.bean.EveryDaySMS;
import zy.chasegoddness.service.SendSMSService;
import zy.chasegoddness.util.TimeUtil;

/**
 * @author zy
 *         每日短信的相关功能
 */
public class EveryDaySMSModel {
    private EveryDaySMSModel() {
    }

    /**
     * 获取时间（毫秒）为time的日期和好感度favorability对应的每日短信
     */
    public static Observable<EveryDaySMS> getEveryDaySMS(long time, int favorability) {
        return getCountOfEveryDaySMS(favorability)
                .flatMap(size -> {
                    int favor = normalization(favorability);
                    int hash = hashCodeOfDate(time);
                    int id = Math.abs(hash) % size;
                    Log.i("zy", "getEveryDaySMS: hash = " + hash + " , size = " + size + " , favorability = " + favor + " , id = " + id);
                    BmobQuery<EveryDaySMS> query = new BmobQuery<>();
                    query.addWhereEqualTo("favourability", favor);
                    query.addWhereEqualTo("id", id);
                    return query.findObjectsObservable(EveryDaySMS.class);
                })
                .flatMap(everyDaySMSes -> {
                    for (EveryDaySMS sms : everyDaySMSes)
                        Log.i("zy", sms.getContent());
                    return Observable.from(everyDaySMSes);
                })
                .take(1);
    }

    /**
     * 获取好感度favorability对应的每日暖话的数量
     */
    public static Observable<Integer> getCountOfEveryDaySMS(int favorability) {
        favorability = normalization(favorability);
        BmobQuery<EveryDaySMS> query = new BmobQuery<>();
        query.addWhereEqualTo("favourability", favorability);
        return query.countObservable(EveryDaySMS.class)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 获取好感度favorability对应的所有每日短信
     */
    public static Observable<List<EveryDaySMS>> getEveryDaySMS(int favorability) {
        favorability = normalization(favorability);
        BmobQuery<EveryDaySMS> query = new BmobQuery<>();
        query.addWhereEqualTo("favourability", favorability);
        return query.findObjectsObservable(EveryDaySMS.class)
                .subscribeOn(Schedulers.io());
    }

    /**
     * 开启每天自动发送短信的服务
     */
    public static void startAutoSendService(Context context, int hour, int minute) {
        Calendar today = Calendar.getInstance();

        Calendar target = Calendar.getInstance();
        target.set(Calendar.HOUR_OF_DAY, hour);
        target.set(Calendar.MINUTE, minute);

        long firstTime;
        if (target.after(today)) {//设置的时间还未过去 今天还要发送短信
            firstTime = target.getTimeInMillis();
        } else {//时间已经过去
            firstTime = target.getTimeInMillis() + AlarmManager.INTERVAL_DAY;//从一天后的这个时候开始
        }

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //alarmManager.cancel(getDefaultIntent(context));
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstTime, AlarmManager.INTERVAL_DAY, getDefaultIntent(context));
    }

    /**
     * 取消每天自动发送短信的服务
     */
    public static void cancelAutoSendService(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getDefaultIntent(context));
    }

    private static PendingIntent getDefaultIntent(Context context) {
        final int requestCode = 204;
        Intent intent = new Intent(context, SendSMSService.class);
        return PendingIntent.getService(context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 确保同一天的日期会生成同一个hash值，
     * 而不同日期会生成不同的hash值
     */
    private static int hashCodeOfDate(long time) {
        Date date = new Date(time);
        String dateFormate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return dateFormate.hashCode();
    }

    /**
     * 把好感度数值标准化到特定的几个阀值上
     */
    private static int normalization(int number) {
        return number / 10 * 10;
    }

    public static boolean isSendToday(Context context) {
        long lastSendTime = new LocalDB(context).getSendUpdate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastSendTime);

        Calendar today = Calendar.getInstance();

        return TimeUtil.isSameDay(today, calendar);
    }
}
