package zy.chasegoddness.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gc.materialdesign.views.LayoutRipple;

import zy.chasegoddness.R;
import zy.chasegoddness.global.BaseApplication;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    LayoutRipple lr_account, lr_adjust, lr_autosend, lr_about, lr_feedback, lr_push, lr_exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initView();
    }

    private final void initView() {
        lr_account = (LayoutRipple) findViewById(R.id.lr_setting_account);
        lr_adjust = (LayoutRipple) findViewById(R.id.lr_setting_adjust);
        lr_autosend = (LayoutRipple) findViewById(R.id.lr_setting_autosend);

        lr_about = (LayoutRipple) findViewById(R.id.lr_setting_about);
        lr_feedback = (LayoutRipple) findViewById(R.id.lr_setting_feedback);
        lr_push = (LayoutRipple) findViewById(R.id.lr_setting_push);

        lr_exit = (LayoutRipple) findViewById(R.id.lr_setting_exit);

        lr_account.setOnClickListener(this);
        lr_adjust.setOnClickListener(this);
        lr_autosend.setOnClickListener(this);
        lr_about.setOnClickListener(this);
        lr_feedback.setOnClickListener(this);
        lr_push.setOnClickListener(this);
        lr_exit.setOnClickListener(this);
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lr_setting_account:
                GoddnessAccountActivity.startActivity(getContext());
                break;
            case R.id.lr_setting_adjust:
                break;
            case R.id.lr_setting_autosend:
                AutoSendActivity.startActivity(getContext());
                break;
            case R.id.lr_setting_about:
                break;
            case R.id.lr_setting_feedback:
                FeedBackActivity.startActivity(getContext());
                break;
            case R.id.lr_setting_push:
                break;
            case R.id.lr_setting_exit:
                ((BaseApplication) getApplication()).exit();
                break;
        }
    }
}
