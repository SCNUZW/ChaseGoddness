package zy.chasegoddness.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gc.materialdesign.views.CheckBox;
import com.gc.materialdesign.views.ProgressBarDeterminate;

import zy.chasegoddness.R;
import zy.chasegoddness.presenter.MainPresenter;
import zy.chasegoddness.ui.activity.iactivity.IMainView;
import zy.chasegoddness.ui.dialog.FavorabilityProgressBarDialog;
import zy.chasegoddness.ui.view.MenuButton;

public class MainActivity extends BaseActivity implements IMainView {

    private static final int PROLOGUE_REQUEST = 200;
    private MainPresenter presenter;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initPresenter();
        initView();
        presenter.init();
        //PrologueActivity.startActivityForResult(getContext(), PROLOGUE_REQUEST);
    }

    private void initPresenter() {
        presenter = new MainPresenter(this);
    }

    private final void initView() {
        fl_scrim = (FrameLayout) findViewById(R.id.fl_scrim);
        iv_favourability = (ImageView) findViewById(R.id.iv_favourability);
        iv_favourability_bg = (ImageView) findViewById(R.id.iv_favourability_bg);
        iv_goddness_sms_bg = (ImageView) findViewById(R.id.iv_goddness_sms_bg);
        tv_evaluation = (TextView) findViewById(R.id.tv_evaluation);
        tv_goddness_sms = (TextView) findViewById(R.id.tv_goddness_sms);
        tv_sent = (TextView) findViewById(R.id.tv_sent);
        cb_autoSend = (CheckBox) findViewById(R.id.cb_autoSend);
        btn_menu = (MenuButton) findViewById(R.id.btn_menu);
        btn_evaluation = (TextView) findViewById(R.id.btn_evaluation);
        pb_favorability = (ProgressBarDeterminate) findViewById(R.id.pb_favorability);

        btn_menu.setOnClickMenuListener(new MainActivity.OnMenuClick());
        btn_evaluation.setOnClickListener(v -> EvaluationActivity.startActivity(getContext()));
        pb_favorability.setProgress(30);
        cb_autoSend.setOncheckListener((view, check) -> presenter.autoSend(check));
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.resume();
    }

    @Override
    public void showFavorabilityDialog(int progress, String text, int progressDuration, int finishDuration) {
        if (getContext() != null) {
            int width = iv_favourability.getWidth();
            int height = iv_favourability.getHeight();
            int[] location = new int[2];
            iv_favourability.getLocationOnScreen(location);

            new FavorabilityProgressBarDialog(MainActivity.this).progress(25).text(text)
                    .toX(location[0] + width / 2).toY(location[1] + height / 2)
                    .progressDuration(progressDuration)
                    .finishDuration(finishDuration)
                    .show(findViewById(R.id.rl_root));
        }
    }

    @Override
    public void showFavorabilityDialog(int progress, String text) {
        showFavorabilityDialog(progress, text, 2000, 2000);
    }

    @Override
    public void setEveryDaySMS(CharSequence str) {
        tv_goddness_sms.setTextColor(Color.DKGRAY);
        tv_goddness_sms.setText(str);
    }

    @Override
    public void setEveryDaySMSError(CharSequence e) {
        tv_goddness_sms.setText(e);
        tv_goddness_sms.setTextColor(Color.RED);
    }

    @Override
    public String getEveryDaySMS() {
        return tv_goddness_sms.getText().toString();
    }

    @Override
    public void setAutoSend(boolean isAutoSend) {
        handler.postDelayed(() -> cb_autoSend.setChecked(isAutoSend), 150);
    }

    @Override
    public void setIsSendToday(boolean isSendToday) {
        if (isSendToday) {
            tv_sent.setText("今天已发送");
        } else {
            tv_sent.setText("今天未发送");
        }
    }

    @Override
    public void setFavorability(int favoribility) {
        if (favoribility > 100) favoribility = 100;
        if (favoribility < 0) favoribility = 0;
        pb_favorability.setProgress(favoribility);
    }

    @Override
    public int getFavorability() {
        return pb_favorability.getProgress();
    }

    @Override
    public void setEvaluation(CharSequence str) {
        tv_evaluation.setTextColor(Color.DKGRAY);
        tv_evaluation.setText(str);
    }

    @Override
    public void setEvaluationError(CharSequence e) {
        tv_evaluation.setTextColor(Color.RED);
        tv_evaluation.setText(e);
    }

    @Override
    public String getEvaluation() {
        return tv_evaluation.getText().toString();
    }

    class OnMenuClick implements MenuButton.OnClickMenuListener {
        @Override
        public boolean onClickItem(int id) {
            //showFavorabilityDialog(25, "好感度+");
            switch (id) {
                case 0://friends
                    FriendsActivity.startActivity(getContext());
                    break;
                case 1://chat
                    ChatActivity.startActivity(getContext());
                    break;
                case 2://third party
                    ThirdPlatformActivity.startActivity(getContext());
                    break;
                case 3://setting
                    SettingActivity.startActivity(getContext());
                    break;
                default:
                    break;
            }
            return true;
        }

        @Override
        public void onClickMenu(boolean open) {
            if (open) {
                fl_scrim.setBackgroundColor(0x55000000);
            } else {
                fl_scrim.setBackgroundColor(0x00000000);
            }
        }
    }

    private MenuButton btn_menu;
    private ProgressBarDeterminate pb_favorability;
    private FrameLayout fl_scrim;
    private ImageView iv_favourability, iv_favourability_bg, iv_goddness_sms_bg;
    private TextView tv_goddness_sms, tv_evaluation, tv_sent, btn_evaluation;
    private CheckBox cb_autoSend;
}
