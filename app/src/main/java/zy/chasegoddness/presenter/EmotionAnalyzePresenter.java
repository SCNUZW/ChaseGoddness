package zy.chasegoddness.presenter;

import android.graphics.Color;
import android.util.Log;

import java.util.Arrays;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import zy.chasegoddness.model.EmotionAnalyzeModel;
import zy.chasegoddness.ui.dialog.EmotionAnalyzeDialog;
import zy.chasegoddness.ui.dialog.idialog.IEmotionAnalyzeView;

/**
 * Created by Administrator on 2016/8/5.
 */
public class EmotionAnalyzePresenter {

    private IEmotionAnalyzeView view;

    public EmotionAnalyzePresenter(EmotionAnalyzeDialog view) {
        this.view = view;
    }

    public void init() {
        String content;
        if (view.getArguments() != null && (content = view.getArguments().getString("content")) != null) {
            if (content != null) {
                view.setContent(content);

                EmotionAnalyzeModel.getClassfy(content)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(s -> {
                            view.setClassify(s);
                        }, error -> {
                            Log.e("zy", error.toString());
                            view.showClassfyError("分类失败");
                        });

                EmotionAnalyzeModel.getSentiment(content)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(doubles -> {
                            view.setSentiment(doubles[0], doubles[1]);
                        }, error -> {
                            Log.e("zy", "EmotionAnalyzePresenter getSentiment error: " + error.toString());
                            view.showSentimentError("正负面百分比分析失败");
                        });

                EmotionAnalyzeModel.getKeywordArray(content)
                        .flatMap(s -> {
                            Log.i("zy", Arrays.toString(s));
                            return EmotionAnalyzeModel.getSuggests(s);
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(strings -> {
                            Log.i("zy", Arrays.toString(strings));
                        }, throwable -> {
                            Log.e("zy", "EmotionAnalyzePresenter getKeyword error: " + throwable.toString());
                            view.showSuggestError("联想失败");
                        });
            }
        }
        view.dismiss();
    }
}
