package zy.chasegoddness.ui.dialog.idialog;

import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;

public interface IFriendsPostView extends IBaseDialog {
    void setPic1(Bitmap bitmap);

    void setPic2(Bitmap bitmap);

    void setPic3(Bitmap bitmap);

    void postClickable();

    void postUnClickable(String str);

    String getContent();

    void setContent(String content);

    FragmentManager getChildFragmentManager();

    FragmentManager getFragmentManager();

    void showError(String e);

    void hideError();
}
