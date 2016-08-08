package zy.chasegoddness.ui.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;

import zy.chasegoddness.R;
import zy.chasegoddness.presenter.EmotionAnalyzePresenter;
import zy.chasegoddness.ui.dialog.idialog.IEmotionAnalyzeView;

public class EmotionAnalyzeDialog extends DialogFragment implements IEmotionAnalyzeView {

    private TextView tv_content, tv_classify, tv_suggest, tv_sentiment;
    private ButtonFlat btn_ok;
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
        btn_ok = (ButtonFlat) v.findViewById(R.id.btn_emotion_ok);

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
    }

    @Override
    public void setSuggest(CharSequence suggest) {
        tv_suggest.setText(suggest);
    }

    @Override
    public double getGoodSentiment() {
        return 0;
    }

    @Override
    public void showSentimentError(String e) {
        tv_sentiment.setText(e);
        tv_sentiment.setTextColor(Color.RED);
    }

    @Override
    public double getBadSentiment() {
        return 0;
    }

    @Override
    public void setSentiment(double good, double bad) {
        tv_sentiment.setVisibility(View.GONE);
    }
}
