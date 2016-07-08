package zy.chasegoddness.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import zy.chasegoddness.model.bean.LocalSms;

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

    /**
     * 短信类型：所有短信
     */
    public final static int ALL_SMS = 0;


    private Context context;

    public LocalSmsModel(Context context) {
        this.context = context;
    }

    /**
     * 分页查询本地短信
     *
     * @param pageSize 页面大小
     * @param pageNum  页面编号（0开始）
     */
    public Observable<LocalSms> getLocalSmsList(String phoneNum, int pageNum, int pageSize) {
        int limit = pageSize;
        int offset = pageNum * pageSize;
        return getLocalSmsList(limit, offset, phoneNum);
    }


    /**
     * 查询本地短信
     *
     * @param limit  取多少条信息
     * @param offset 跳过多少行信息
     * @return
     */
    private Observable<LocalSms> getLocalSmsList(final int limit, final int offset, final String phoneNum) {
        return Observable.create(new Observable.OnSubscribe<LocalSms>() {
            @Override
            public void call(Subscriber<? super LocalSms> subscriber) {
                try {
                    Log.i("zy", "is UnSubscribed:" + subscriber.isUnsubscribed());
                    if (!subscriber.isUnsubscribed()) {

                        ContentResolver cr = context.getContentResolver();
                        String[] projection = new String[]{"_id", "address", "person",
                                "body", "date", "type"};
                        Uri uri = Uri.parse(SMS_URI_ALL);
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

    private LocalSms getLocalSmsFromCursor(Cursor cur, int nameColumn, int phoneNumberColumn, int smsbodyColumn, int dateColumn, int typeColumn) {
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
