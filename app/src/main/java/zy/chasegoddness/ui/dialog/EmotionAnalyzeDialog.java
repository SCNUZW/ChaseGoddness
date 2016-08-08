package zy.chasegoddness.ui.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;

import java.util.Locale;

import zy.chasegoddness.R;
import zy.chasegoddness.presenter.EmotionAnalyzePresenter;
import zy.chasegoddness.ui.dialog.idialog.IEmotionAnalyzeView;
import zy.chasegoddness.ui.view.SentimentBar;

public class EmotionAnalyzeDialog extends DialogFragment implements IEmotionAnalyzeView {

    private TextView tv_content, tv_classify, tv_suggest, tv_sentiment, tv_good, tv_bad;
    private ButtonFlat btn_ok;
    private SentimentBar sb_sentiment;
    private EmotionAnalyzePresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_emotion_analyze, container, false);
        initPresenter();
        initView(view);
        return view;
    }

    private void initPresenter() {
        presenter = new EmotionAnalyzePresenter(this);
    }

    private void initView(View v) {
        tv_content = (TextView) v.findViewById(R.id.tv_emotion_content);
        tv_classify = (TextView) v.findViewById(R.id.tv_emotion_classify);
        tv_suggest = (TextView) v.findViewById(R.id.tv_emotion_suggest);
        tv_sentiment = (TextView) v.findViewById(R.id.tv_emotion_sentiment);
        tv_good = (TextView) v.findViewById(R.id.tv_emotion_good_sentiment);
        tv_bad = (TextView) v.findViewById(R.id.tv_emotion_bad_sentiment);
        btn_ok = (ButtonFlat) v.findViewById(R.id.btn_emotion_ok);
        sb_sentiment = (SentimentBar) v.findViewById(R.id.sb_sentiment);

        btn_ok.setOnClickListener(view -> dismiss());

        presenter.init();
    }

    public static EmotionAnalyzeDialog showDialog(FragmentManager manager, String content) {
        EmotionAnalyzeDialog dialog = new EmotionAnalyzeDialog();
        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        dialog.setArguments(bundle);
        dialog.show(manager, "EmotionAnalyzeDialog");
        return dialog;
    }

    @Override
    public String getContent() {
        return tv_content.getText().toString();
    }

    @Override
    public void showContentError(String e) {
        tv_content.setText(e);
        tv_content.setTextColor(Color.RED);
    }

    @Override
    public void setContent(String content) {
        tv_content.setText(content);
    }

    @Override
    public String getClassify() {
        return tv_classify.getText().toString();
    }

    @Override
    public void showClassfyError(String e) {
        tv_classify.setText(e);
        tv_classify.setTextColor(Color.RED);
    }

    @Override
    public void setClassify(CharSequence classify) {
        tv_classify.setText(classify);
    }

    @Override
    public String getSuggest() {
        return tv_suggest.getText().toString();
    }

    @Override
    public void showSuggestError(String e) {
        tv_suggest.setText(e);
        tv_suggest.setTextColor(Color.RED);
        sb_sentiment.setVisibility(View.GONE);
    }

    @Override
    public void setSuggest(CharSequence suggest) {
        tv_suggest.setText(suggest);
    }

    @Override
    public double getGoodSentiment() {
        return sb_sentiment.getGoodSentiment();
    }

    @Override
    public void showSentimentError(String e) {
        tv_sentiment.setText(e);
        tv_sentiment.setTextColor(Color.RED);
    }

    @Override
    public double getBadSentiment() {
        return sb_sentiment.getBadSentiment();
    }

    @Override
    public void setSentiment(double good, double bad) {
        tv_sentiment.setVisibility(View.GONE);
        sb_sentiment.setGoodSentiment(good);
        sb_sentiment.setBadSentiment(bad);
        sb_sentiment.setVisibility(View.VISIBLE);
        tv_good.setText("正面：" + String.format("%.2f", good * 100) + "%");
        tv_bad.setText("负面：" + String.format("%.2f", bad * 100) + "%");
        tv_good.setVisibility(View.VISIBLE);
        tv_bad.setVisibility(View.VISIBLE);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }
}
