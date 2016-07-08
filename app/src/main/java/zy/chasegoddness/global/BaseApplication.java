package zy.chasegoddness.global;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import java.util.Stack;

/**
 * Created by Administrator on 2016/7/2.
 */
public class BaseApplication extends Application {
    private Stack<Activity> activityStack;

    @Override
    public void onCreate() {
        super.onCreate();

        activityStack = new Stack<>();
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
