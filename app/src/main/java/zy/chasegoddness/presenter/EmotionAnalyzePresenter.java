package zy.chasegoddness.presenter;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
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
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(strings -> {
                        int cnt = strings.length / 6;
                        SpannableStringBuilder sb = new SpannableStringBuilder();
                        for (int i = 0; i <= cnt; i++) {
                            String s = strings[i];
                            SpannableString ss = new SpannableString("  " + s + "  ");
                            ss.setSpan(new BackgroundColorSpan(getRandomColor()), 0, ss.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                            ss.setSpan(new ForegroundColorSpan(Color.WHITE), 0, ss.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                            sb.append(ss);
                            sb.append("  ");
                        }
                        view.setSuggest(sb);
                    }, throwable -> {
                        Log.e("zy", "EmotionAnalyzePresenter getKeyword error: " + throwable.toString());
                        view.showSuggestError("提取失败");
                    });
        } else {
            view.showClassfyError("分类失败");
            view.showContentError("读取内容失败");
            view.showSentimentError("正负面百分比分析失败");
            view.showSuggestError("提取失败");
        }
    }

    private static int getRandomColor() {
        int[] colors = new int[]{0xFFFFA500, 0xFFFF83FA, 0xFFEE799F, 0xFF4876FF, 0xFF66CDAA, 0xFFB03060, 0xFFCD00CD, 0xFF00B2EE};
        int index = (int) (Math.random() * colors.length);
        return colors[index];
    }
}
