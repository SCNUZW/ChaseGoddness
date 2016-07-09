package zy.chasegoddness.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import zy.chasegoddness.ui.activity.iactivity.IBaseView;
import zy.chasegoddness.global.BaseApplication;
import zy.chasegoddness.global.StatusBarCompat;

/**
 * Created by Administrator on 2016/6/30.
 */
public class BaseActivity extends AppCompatActivity implements IBaseView {

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        StatusBarCompat.compat(this);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        StatusBarCompat.compat(this);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
        StatusBarCompat.compat(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseApplication) getApplication()).pushActivity(this);
    }

    @Override
    public void finish() {
        super.finish();
        ((BaseApplication) getApplication()).popActivity();
    }

    @Override
    public void showToast(String str) {
        if (getContext() != null)
            Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String str, int gravity, int offsetX, int offsetY) {
        if (getContext() != null) {
            Toast toast = Toast.makeText(getContext(), str, Toast.LENGTH_SHORT);
            toast.setGravity(gravity, offsetX, offsetY);
            toast.show();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }
}
