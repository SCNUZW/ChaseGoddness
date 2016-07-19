package zy.chasegoddness.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;

import zy.chasegoddness.R;
import zy.chasegoddness.presenter.FriendsRegisterPresenter;
import zy.chasegoddness.ui.activity.iactivity.IFriendsRegisterView;

public class FriendsRegisterActivity extends BaseActivity implements IFriendsRegisterView {

    private EditText et_phone, et_pwd, et_smsCode;
    private Button btn_smsCode, btn_register;
    private TextView tv_error;
    private FriendsRegisterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_register);

        initPresenter();
        initView();
    }

    private void initPresenter() {
        presenter = new FriendsRegisterPresenter(this);
    }

    private void initView() {
        et_phone = (EditText) findViewById(R.id.et_friends_phone);
        et_pwd = (EditText) findViewById(R.id.et_friends_pwd);
        et_smsCode = (EditText) findViewById(R.id.et_friends_sms_code);
        btn_register = (Button) findViewById(R.id.btn_friends_register_ok);
        btn_smsCode = (Button) findViewById(R.id.btn_friends_sms_code);
        tv_error = (TextView) findViewById(R.id.tv_friends_error);

        btn_register.setOnClickListener(v -> presenter.register());
        btn_smsCode.setOnClickListener(v -> presenter.requestSMSCode());

        presenter.init();
    }

    @Override
    public void showError(String str) {
        tv_error.setText(str);
        tv_error.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideError() {
        tv_error.setVisibility(View.INVISIBLE);
    }

    @Override
    public String getPassword() {
        return et_pwd.getText().toString();
    }

    @Override
    public String getSMSCode() {
        return et_smsCode.getText().toString();
    }

    @Override
    public String getPhoneNum() {
        return et_phone.getText().toString();
    }

    @Override
    public void registerUnClickable() {
        btn_register.setClickable(false);
        btn_register.setText("请稍候");
        btn_register.setBackgroundColor(getResources().getColor(R.color.colorUnClickable));
    }

    @Override
    public void registerClickable() {
        btn_register.setClickable(true);
        btn_register.setText("注册");
        btn_register.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void smscodeClickable() {
        btn_smsCode.setClickable(true);
        btn_smsCode.setText("获取验证码");
        btn_smsCode.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void smscodeUnClickable() {
        btn_smsCode.setClickable(false);
        btn_smsCode.setBackgroundColor(getResources().getColor(R.color.colorUnClickable));
    }

    @Override
    public void smscodeUnClickable(int second) {
        btn_smsCode.setText(second + "");
    }

    @Override
    public void postRunnable(Runnable runnable) {
        runOnUiThread(runnable);
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, FriendsRegisterActivity.class));
    }
}
