package zy.chasegoddness.global;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

/**
 * Created by Administrator on 2016/7/5.
 */
public class LocalDB {

    public static final String DB_NAME = "GoddnessData";

    public static final String PHONE = "phoneNum";
    public static final String AUTO_SEND = "autoSend";
    public static final String AUTO_SEND_HOUR = "autoSendHour";
    public static final String AUTO_SEND_MINUTE = "autoSendMinute";
    public static final String SEND_UPDATE = "sendUpdate";
    public static final String FIRST_LOGIN = "firstLogin";
    public static final String FAVORABILITY = "favorability";
    public static final String FAVOR_UPDATE = "favorabilityUpdate";
    public static final String KEYBOARDHEIGHT = "keyboardHeight";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public LocalDB(Context context) {
        sharedPreferences = context.getSharedPreferences(DB_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void putPhoneNum(String phoneNum) {
        editor.putString(PHONE, phoneNum).commit();
    }

    public String getPhoneNum() {
        return sharedPreferences.getString(PHONE, "");
    }

    public void putAutoSend(boolean isAutoSend) {
        editor.putBoolean(AUTO_SEND, isAutoSend).commit();
    }

    public boolean getAutoSend() {
        return sharedPreferences.getBoolean(AUTO_SEND, false);
    }

    public void putAutoSendHour(int hour) {
        editor.putInt(AUTO_SEND_HOUR, hour).commit();
    }

    public int getAutoSendHour() {
        return sharedPreferences.getInt(AUTO_SEND_HOUR, -1);
    }

    public void putAutoSendMinute(int minute) {
        editor.putInt(AUTO_SEND_MINUTE, minute).commit();
    }

    public int getAutoSendMinute() {
        return sharedPreferences.getInt(AUTO_SEND_MINUTE, -1);
    }

    public void putSendUpdate(long time) {
        editor.putLong(SEND_UPDATE, time).commit();
    }

    public long getSendUpdate() {
        return sharedPreferences.getLong(SEND_UPDATE, 0);
    }

    public void putFirstLogin(boolean isFirstLogin) {
        editor.putBoolean(FIRST_LOGIN, isFirstLogin).commit();
    }

    public boolean getFirstLogin() {
        return sharedPreferences.getBoolean(FIRST_LOGIN, true);
    }

    public void putFavorability(int favorability) {
        editor.putInt(FAVORABILITY, favorability).commit();
    }

    public int getFavorability() {
        return sharedPreferences.getInt(FAVORABILITY, 30);
    }

    public void putFavorUpdate(long updateDate) {
        editor.putLong(FAVOR_UPDATE, updateDate).commit();
    }

    public long getFavorUpdate() {
        return sharedPreferences.getLong(FAVOR_UPDATE, 0);
    }

    public void putKeyboardHeight(int height) {
        editor.putInt(KEYBOARDHEIGHT, height).commit();
    }

    public int getKeyboardHeight() {
        return sharedPreferences.getInt(KEYBOARDHEIGHT, 0);
    }
}
