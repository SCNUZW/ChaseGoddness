package zy.chasegoddness.ui.dialog.idialog;

import android.os.Bundle;

/**
 * Created by Administrator on 2016/8/5.
 */
public interface IEmotionAnalyzeView extends IBaseDialog {
    String getContent();

    void setContent(String content);

    void showContentError(String e);

    String getClassify();

    void showClassfyError(String e);

    void setClassify(CharSequence classify);

    String getSuggest();

    void showSuggestError(String e);

    void setSuggest(CharSequence suggest);

    double getGoodSentiment();

    void showSentimentError(String e);

    double getBadSentiment();

    void setSentiment(double good, double bad);

    Bundle getArguments();
}
