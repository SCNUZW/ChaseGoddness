package zy.chasegoddness.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gc.materialdesign.views.Button;

import zy.chasegoddness.R;
import zy.chasegoddness.ui.activity.iactivity.IFriendsRegisterView;

public class FriendsRegisterActivity extends BaseActivity implements IFriendsRegisterView {

    private EditText et_phone, et_pwd, et_smsCode;
    private Button btn_smsCode, btn_register;
    private TextView tv_error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_register);

        initView();
    }

    private void initView() {
        et_phone = (EditText) findViewById(R.id.et_friends_phone);
        et_pwd = (EditText) findViewById(R.id.et_friends_pwd);
        et_smsCode = (EditText) findViewById(R.id.et_friends_sms_code);
        btn_register = (Button) findViewById(R.id.btn_friends_register);
        btn_smsCode = (Button) findViewById(R.id.btn_friends_sms_code);
        tv_error = (TextView) findViewById(R.id.tv_friends_error);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        btn_smsCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
    public void RegisterUnClickable() {
        btn_register.setClickable(false);
        btn_register.setText("请稍候");
        btn_register.setBackgroundColor(getResources().getColor(R.color.colorUnClickable));
    }

    @Override
    public void RegisterClickable() {
        btn_register.setClickable(true);
        btn_register.setText("注册");
        btn_register.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void SMSCodeClickable() {
        btn_smsCode.setClickable(true);
        btn_smsCode.setText("获取验证码");
        btn_smsCode.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void SMSCodeUnClickable() {
        btn_smsCode.setClickable(false);
        btn_smsCode.setBackgroundColor(getResources().getColor(R.color.colorUnClickable));
    }

    @Override
    public void SMSCodeUnClickable(int second) {
        btn_smsCode.setText(second + "");
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, FriendsRegisterActivity.class));
    }
}
