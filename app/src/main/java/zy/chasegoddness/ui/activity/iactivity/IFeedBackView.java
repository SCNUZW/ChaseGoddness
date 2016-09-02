package zy.chasegoddness.ui.activity.iactivity;

/**
 * Created by Administrator on 2016/9/2.
 */
public interface IFeedBackView extends IBaseView{
    String getPhone();
    String getQQ();
    String getContent();
    void showError(String str);
    void hideError();
}
