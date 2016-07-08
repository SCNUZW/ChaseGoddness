package zy.chasegoddness.ui.activity.iactivity;

/**
 * Created by Administrator on 2016/7/1.
 */
public interface IMainView extends IBaseView {
    void showFavorabilityDialog(int progress, String text, int progressDuration, int finishDuration);

    void showFavorabilityDialog(int progress, String text);
}
