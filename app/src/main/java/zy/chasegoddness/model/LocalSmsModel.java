package zy.chasegoddness.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Single;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import zy.chasegoddness.model.bean.LocalSms;

/**
 * 与本地短信相关的功能
 */
public class LocalSmsModel {
    /**
     * 所有短信
     */
    public final static String SMS_URI_ALL = "content://sms/";
    /**
     * 收件箱短信
     */
    public final static String SMS_URI_INBOX = "content://sms/inbox";
    /**
     * 已发送短信
     */
    public final static String SMS_URI_SEND = "content://sms/sent";
    /**
     * 草稿箱短信
     */
    public final static String SMS_URI_DRAFT = "content://sms/draft";

    private LocalSmsModel() {
    }

    /**
     * 分页查询与phoneNum交互的本地短信
     *
     * @param pageSize 页面大小
     * @param pageNum  页面编号（0开始）
     */
    public static Observable<LocalSms> getLocalSmsList(Context context, String phoneNum, int pageNum, int pageSize) {
        int limit = pageSize;
        int offset = pageNum * pageSize;
        return getLocalSmsList(context, limit, offset, phoneNum, LocalSms.Type.ALL_SMS);
    }

    /**
     * 查询最近一条来自phoneNum的本地短信
     */
    public static Observable<LocalSms> getLastLocalSms(Context context, String phoneNum) {
        return getLocalSmsList(context, 1, 0, phoneNum, LocalSms.Type.RECIEVE_SMS);
    }

    public static void sendSMS(String content, String phoneNum) {
        //获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        //拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(content);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phoneNum, null, text, null, null);
        }
    }

    /**
     * 查询本地短信
     *
     * @param limit  取多少条信息
     * @param offset 跳过多少行信息
     * @param type   短信的类型
     */
    public static Observable<LocalSms> getLocalSmsList(final Context context, final int limit, final int offset, final String phoneNum, final int type) {
        return Observable.create(new Observable.OnSubscribe<LocalSms>() {
            @Override
            public void call(Subscriber<? super LocalSms> subscriber) {
                try {
                    if (!subscriber.isUnsubscribed()) {

                        Uri uri;
                        if (type == LocalSms.Type.SEND_SMS) uri = Uri.parse(SMS_URI_SEND);
                        else if (type == LocalSms.Type.RECIEVE_SMS) uri = Uri.parse(SMS_URI_INBOX);
                        else uri = Uri.parse(SMS_URI_ALL);

                        String[] projection = new String[]{"_id", "address", "person",
                                "body", "date", "type"};

                        ContentResolver cr = context.getContentResolver();
                        Cursor cur = cr.query(uri, projection, "address = " + phoneNum, null, "date desc limit " + limit + " offset " + offset);

                        if (cur.moveToFirst()) {
                            int nameColumn = cur.getColumnIndex("person");
                            int phoneNumberColumn = cur.getColumnIndex("address");
                            int smsbodyColumn = cur.getColumnIndex("body");
                            int dateColumn = cur.getColumnIndex("date");
                            int typeColumn = cur.getColumnIndex("type");

                            do {
                                LocalSms sms = getLocalSmsFromCursor(cur, nameColumn, phoneNumberColumn, smsbodyColumn, dateColumn, typeColumn);
                                subscriber.onNext(sms);
                            } while (cur.moveToNext());

                        } else {
                            //no result
                        }

                        subscriber.onCompleted();
                    }
                } catch (Exception e) {
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * 查询到Cursor对象后 映射成为LocalSms对象
     */
    private static LocalSms getLocalSmsFromCursor(Cursor cur, int nameColumn, int phoneNumberColumn, int smsbodyColumn, int dateColumn, int typeColumn) {
        LocalSms sms = new LocalSms();

        sms.setName(cur.getString(nameColumn));
        sms.setPhoneNumber(cur.getString(phoneNumberColumn));
        sms.setBody(cur.getString(smsbodyColumn));
        sms.setType(cur.getInt(typeColumn));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date d = new Date(Long.parseLong(cur.getString(dateColumn)));
        sms.setDate(dateFormat.format(d));

        return sms;
    }
}
