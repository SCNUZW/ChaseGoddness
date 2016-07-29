package zy.chasegoddness.global;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import java.util.Stack;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

public class BaseApplication extends Application {
    private Stack<Activity> activityStack;

    @Override
    public void onCreate() {
        super.onCreate();
        activityStack = new Stack<>();
        initBmob();
    }

    private void initBmob() {
        //第一：默认初始化
        //Bmob.initialize(this, "d81976c202d52737c00d431f02f7b968");
        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config = new BmobConfig.Builder(this)
                //设置appkey
                .setApplicationId("d81976c202d52737c00d431f02f7b968")
                //请求超时时间（单位为秒）：默认15s
                //.setConnectTimeout(30)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(1024 * 1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(3600)
                .build();
        Bmob.initialize(config);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void pushActivity(Activity activity) {
        activityStack.push(activity);
        Log.i("zy", "Activity Stack push " + activityStack.size());
    }

    public void popActivity() {
        if (!activityStack.empty())
            activityStack.pop();
        Log.i("zy", "Activity Stack pop " + activityStack.size());
    }

    public void exit() {
        while (!activityStack.empty()) {
            Activity activity = activityStack.peek();
            activity.finish();
        }
    }
}
