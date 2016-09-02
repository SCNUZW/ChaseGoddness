package zy.chasegoddness.ui.activity.iactivity;

/**
 * Created by Administrator on 2016/7/1.
 */
public interface IMainView extends IBaseView {
    void showFavorabilityDialog(int progress, String text, int progressDuration, int finishDuration);

    void showFavorabilityDialog(int progress, String text);

    void setEveryDaySMS(CharSequence str);

    void setEveryDaySMSError(CharSequence e);

    String getEveryDaySMS();

    void setAutoSend(boolean isAutoSend);

    void setIsSendToday(boolean isSendToday);

    void setFavorability(int favoribility);

    int getFavorability();

    void setEvaluation(CharSequence str);

    void setEvaluationError(CharSequence e);

    String getEvaluation();
}
