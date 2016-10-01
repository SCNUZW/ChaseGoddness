package zy.chasegoddness.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gc.materialdesign.views.LayoutRipple;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import zy.chasegoddness.R;
import zy.chasegoddness.global.RxBus;
import zy.chasegoddness.presenter.FriendsSettingPresenter;
import zy.chasegoddness.ui.activity.iactivity.IFriendsSettingView;
import zy.chasegoddness.ui.dialog.FriendsSettingDialog;
import zy.chasegoddness.util.GlideCircleTransform;

public class FriendsSettingActivity extends BaseActivity implements IFriendsSettingView {

    private ImageView iv_avatar;
    private TextView tv_name, tv_desc, tv_error;
    private LayoutRipple lr_name, lr_desc;
    private FriendsSettingPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_setting);
        initPresenter();
        initView();
        initRxBus();
    }

    private void initPresenter() {
        presenter = new FriendsSettingPresenter(this);
    }

    private void initRxBus() {
        RxBus.getInstance()
                .toObserverable()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    if (event.getDesc().equals("finish update user")) {
                        presenter.init();
                    }
                }, throwable -> {
                    showError("发生了一个未知错误");
                    Log.e("zy", "FriendsSettingActivity error" + throwable);
                });
    }

    private void initView() {
        lr_desc = (LayoutRipple) findViewById(R.id.lr_friends_setting_desc);
        lr_name = (LayoutRipple) findViewById(R.id.lr_friends_setting_name);
        iv_avatar = (ImageView) findViewById(R.id.iv_friends_setting_avatar);
        tv_name = (TextView) findViewById(R.id.tv_friends_setting_name);
        tv_desc = (TextView) findViewById(R.id.tv_friends_setting_desc);
        tv_error = (TextView) findViewById(R.id.tv_friends_setting_error);

        lr_desc.setOnClickListener(v -> {
            if (presenter.checkLogin())
                FriendsSettingDialog.showDialog(getSupportFragmentManager());
        });
        lr_name.setOnClickListener(v -> {
            if (presenter.checkLogin())
                FriendsSettingDialog.showDialog(getSupportFragmentManager());
        });
        iv_avatar.setOnClickListener(v -> {
            if (presenter.checkLogin())
                presenter.chosePicture();
        });

        presenter.init();
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, FriendsSettingActivity.class));
    }

    @Override
    public void setNickName(String name) {
        tv_name.setText(name);
    }

    @Override
    public void setDescription(String desc) {
        tv_desc.setText(desc);
    }

    @Override
    public String getNickName() {
        return tv_name.getText().toString();
    }

    @Override
    public String getDescription() {
        return tv_desc.getText().toString();
    }

    @Override
    public void setAvatar(String urlOrPath) {
        Log.i("zy", "show picture: " + urlOrPath);
        Glide.with(this)
                .load(urlOrPath)
                .transform(new GlideCircleTransform(getContext()))
                .into(iv_avatar);
    }

    @Override
    public void showError(String error) {
        tv_error.setText(error);
        tv_error.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideError() {
        tv_error.setVisibility(View.GONE);
    }
}
