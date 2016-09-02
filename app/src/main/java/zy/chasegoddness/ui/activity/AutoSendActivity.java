package zy.chasegoddness.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import com.gc.materialdesign.views.CheckBox;

import zy.chasegoddness.R;
import zy.chasegoddness.presenter.AutoSendPresenter;
import zy.chasegoddness.ui.activity.iactivity.IAutoSendView;

public class AutoSendActivity extends BaseActivity implements IAutoSendView {

    private CheckBox cb_autoSend;
    private TimePicker tp_autoSend;
    private AutoSendPresenter presenter;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_send);

        initPresenter();
        initView();
    }

    private void initPresenter() {
        presenter = new AutoSendPresenter(this);
    }

    private void initView() {
        cb_autoSend = (CheckBox) findViewById(R.id.cb_autoSend);
        tp_autoSend = (TimePicker) findViewById(R.id.tp_auto_send);

        tp_autoSend.setIs24HourView(true);
        tp_autoSend.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            if (cb_autoSend.isCheck())
                presenter.setAutoSend();
        });
        //当把checkBox选为自动发送时 显示时间选择器 否则隐藏时间选择器
        cb_autoSend.setOncheckListener((view, check) -> {
            presenter.setAutoSend();
            if (check) {
                showTimePicker();
            } else {
                hideTimePicker();
            }
        });

        presenter.init();
    }

    @Override
    public void setIsAutoSend(boolean isAutoSend) {
        handler.postDelayed(() -> cb_autoSend.setChecked(isAutoSend), 150);
    }

    @Override
    public void setAutoSendTimeHour(int hour) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tp_autoSend.setHour(hour);
        } else {
            tp_autoSend.setCurrentHour(hour);
        }
    }

    @Override
    public void setAutoSendTimeMinute(int minute) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            tp_autoSend.setMinute(minute);
        } else {
            tp_autoSend.setCurrentMinute(minute);
        }
    }

    @Override
    public int getHourOfDay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return tp_autoSend.getHour();
        }
        return tp_autoSend.getCurrentHour();
    }

    @Override
    public int getMinute() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return tp_autoSend.getMinute();
        }
        return tp_autoSend.getCurrentMinute();
    }

    @Override
    public boolean getIsCheckAutoSend() {
        return cb_autoSend.isCheck();
    }

    @Override
    public void showTimePicker() {
        tp_autoSend.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideTimePicker() {
        tp_autoSend.setVisibility(View.GONE);
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, AutoSendActivity.class));
    }
}
