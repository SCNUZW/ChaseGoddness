package zy.chasegoddness.ui.dialog.idialog;

import android.content.Context;
import android.support.v4.app.Fragment;

public interface IBaseDialog {
    void dismiss();

    Fragment getFragment();

    Context getContext();
}
