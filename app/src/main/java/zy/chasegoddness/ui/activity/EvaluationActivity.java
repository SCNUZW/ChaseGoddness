package zy.chasegoddness.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import zy.chasegoddness.R;
import zy.chasegoddness.model.bean.Evaluation;
import zy.chasegoddness.presenter.EvaluationPresenter;

public class EvaluationActivity extends BaseActivity {

    private TextView tv_title;
    private TextView tv_content;
    private EvaluationPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        initPresenter();
        initView();
        presenter.init();
    }

    private void initPresenter() {
        presenter = new EvaluationPresenter(this);
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_evaluation_title);
        tv_content = (TextView) findViewById(R.id.tv_evaluation_content);
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    public void setContent(String content) {
        tv_content.setText(content);
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, EvaluationActivity.class));
    }
}
