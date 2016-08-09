package zy.chasegoddness.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Date;
import java.util.List;

import rx.schedulers.Schedulers;
import zy.chasegoddness.global.LocalDB;
import zy.chasegoddness.model.EveryDaySMSModel;
import zy.chasegoddness.model.FormatCheckModel;

/**
 * Created by Administrator on 2016/8/8.
 */
public class SendSMSService extends IntentService {

    public SendSMSService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        LocalDB db = new LocalDB(getApplicationContext());
        String phone = db.getPhoneNum();
        int favorability = db.getFavorability();//当前好感度
        long currentTime = new Date().getTime();//当前时间

        //如果女神的手机号码有问题 不发送短信
        if (phone == null || !FormatCheckModel.isPhoneNumber(phone)) return;

        //获取每天暖话 并发送
        EveryDaySMSModel.getEveryDaySMS(currentTime, favorability)
                .observeOn(Schedulers.immediate())
                .subscribe(everyDaySMS -> {
                    String msg = everyDaySMS.getContent();
                    SmsManager manager = SmsManager.getDefault();
                    List<String> list = manager.divideMessage(msg);
                    for (String sms : list) {
                        manager.sendTextMessage(phone, getLocalPhoneNum(), sms, null, null);
                    }

                    //完成短信发送
                    db.putSendUpdate(new Date().getTime());
                }, throwable -> {
                    Log.e("zy", "SendSMSService error: " + throwable.toString());
                });
    }

    /**
     * 获取本机号码
     */
    private String getLocalPhoneNum() {
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getLine1Number();
    }
}
