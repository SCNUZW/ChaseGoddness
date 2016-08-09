package zy.chasegoddness.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import zy.chasegoddness.global.LocalDB;

public class ShutdownReceiver extends BroadcastReceiver {
    public ShutdownReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        /**
         * 当关机的时候 自动发送短信的AlarmManager会被取消 所以不再自动发送短信
         */
        LocalDB db = new LocalDB(context);
        db.putAutoSend(false);
    }
}
