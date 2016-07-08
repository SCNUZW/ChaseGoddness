package zy.chasegoddness.ui.activity.iactivity;

import android.content.Context;
import android.view.Gravity;

/**
 * Created by Administrator on 2016/7/1.
 */
public interface IBaseView {
    void showToast(String str);

    void showToast(String str, int gravity, int offsetX, int offsetY);

    Context getContext();
}
