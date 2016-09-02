package zy.chasegoddness.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Date;
import java.util.List;

import rx.schedulers.Schedulers;
import zy.chasegoddness.R;
import zy.chasegoddness.global.LocalDB;
import zy.chasegoddness.model.EveryDaySMSModel;
import zy.chasegoddness.model.FormatCheckModel;
import zy.chasegoddness.ui.activity.FriendsActivity;

/**
 * Created by Administrator on 2016/8/8.
 */
public class SendSMSService extends IntentService {

    public SendSMSService() {
        super("AutoSendSMSService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LocalDB db = new LocalDB(getApplicationContext());
        String phone = db.getPhoneNum();
        int favorability = db.getFavorability();//当前好感度
        long currentTime = new Date().getTime();//当前时间

        //如果女神的手机号码有问题 不发送短信
        if (phone == null || !FormatCheckModel.isPhoneNumber(phone)) {
            Log.e("zy", "SendSMSService 尚未设置女神手机号码 无法发送短信");
            return;
        }

        Log.i("zy", "SendSMSService 发送每日暖话");
        //获取每天暖话 并发送
        EveryDaySMSModel.getEveryDaySMS(currentTime, favorability)
                .observeOn(Schedulers.immediate())
                .subscribe(everyDaySMS -> {
                    String msg = everyDaySMS.getContent();
                    SmsManager manager = SmsManager.getDefault();
                    List<String> list = manager.divideMessage(msg);
                    for (String sms : list) {
                        manager.sendTextMessage(phone, null, sms, null, null);
                    }
                    Log.i("zy", "SendSMSService 发送短信：" + getLocalPhoneNum() + " -> " + phone + " :" + msg);
                    //完成短信发送
                    db.putSendUpdate(new Date().getTime());
                }, throwable -> {
                    Log.e("zy", "SendSMSService error: " + throwable.toString());
                    NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
                    Notification notification = mBuilder.setContentTitle("追女神")
                            .setContentText("自动发送每日暖话失败")
                            .setWhen(System.currentTimeMillis())
                            .setPriority(Notification.PRIORITY_DEFAULT)
                            .setDefaults(Notification.DEFAULT_ALL)
                            .setContentIntent(getDefaultIntent())
                            .setSmallIcon(R.drawable.app)
                            .build();
                    notification.flags = Notification.FLAG_AUTO_CANCEL;
                    mNotificationManager.notify(1, notification);
                });
    }

    /**
     * 获取本机号码
     */
    private String getLocalPhoneNum() {
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getLine1Number();
    }

    private PendingIntent getDefaultIntent() {
        final int requestCode = 203;
        Intent intent = new Intent(this, FriendsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(this, requestCode, new Intent(this, FriendsActivity.class), PendingIntent.FLAG_ONE_SHOT);
    }
}
