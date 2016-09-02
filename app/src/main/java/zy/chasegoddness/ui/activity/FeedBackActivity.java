package zy.chasegoddness.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFloat;

import rx.android.schedulers.AndroidSchedulers;
import zy.chasegoddness.R;
import zy.chasegoddness.model.FeedBackModel;
import zy.chasegoddness.ui.activity.iactivity.IFeedBackView;

public class FeedBackActivity extends BaseActivity implements IFeedBackView {

    private EditText et_content, et_qq, et_phone;
    private TextView tv_error;
    private ButtonFloat btn_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        initView();
    }

    private void initView() {
        et_content = (EditText) findViewById(R.id.et_feedback_content);
        et_phone = (EditText) findViewById(R.id.et_feedback_phone);
        et_qq = (EditText) findViewById(R.id.et_feedback_qq);
        tv_error = (TextView) findViewById(R.id.tv_feedback_error);
        btn_ok = (ButtonFloat) findViewById(R.id.btn_feedback_ok);

        btn_ok.setOnClickListener(v -> {
            hideError();
            final String content = getContent();
            final String QQ = getQQ();
            final String phone = getPhone();
            if (content == null || content.trim().equals("")) {
                showError("反馈内容不能为空");
                return;
            }

            FeedBackModel.uploadFeedBack(content, QQ, phone)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s -> {
                        Log.i("zy", "FeedBackActivity 反馈：" + s);
                        showToast("反馈成功");
                    }, throwable -> {
                        Log.e("zy", "FeedBackAcitivty error: " + throwable.toString());
                        showToast("反馈失败！");
                    });
            finish();
        });
    }

    @Override
    public String getPhone() {
        return et_phone.getText().toString();
    }

    @Override
    public String getQQ() {
        return et_qq.getText().toString();
    }

    @Override
    public String getContent() {
        return et_content.getText().toString();
    }

    @Override
    public void showError(String str) {
        tv_error.setVisibility(View.VISIBLE);
        tv_error.setText(str);
    }

    @Override
    public void hideError() {
        tv_error.setVisibility(View.GONE);
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, FeedBackActivity.class));
    }
}
