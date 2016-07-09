package zy.chasegoddness.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import zy.chasegoddness.R;
import zy.chasegoddness.global.LocalDB;
import zy.chasegoddness.model.SetAccountModel;

public class GoddnessAccountActivity extends BaseActivity {

    private EditText et_phone;
    private ButtonFlat btn_ok;
    private TextView tv_wrongNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goddness_account);

        initView();
    }

    private final void initView() {
        et_phone = (EditText) findViewById(R.id.et_goddness_phone);
        et_phone.setText(new LocalDB(getContext()).getPhoneNum());

        tv_wrongNumber = (TextView) findViewById(R.id.tv_wrongNumber);

        btn_ok = (ButtonFlat) findViewById(R.id.btn_goddness_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = et_phone.getText().toString();
                if (SetAccountModel.isPhoneNumber(phoneNum)) {
                    tv_wrongNumber.setVisibility(View.GONE);

                    SetAccountModel.saveAccount(getContext(), phoneNum);

                    finish();
                } else {
                    tv_wrongNumber.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, GoddnessAccountActivity.class));
    }
}
