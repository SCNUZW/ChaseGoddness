package zy.chasegoddness.ui.dialog;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import zy.chasegoddness.R;
import zy.chasegoddness.ui.view.FavorabilityCircleProgressBar;

/**
 * Created by Administrator on 2016/6/30.
 */
public class FavorabilityProgressBarDialog {

    private FavorabilityCircleProgressBar progressBar;
    private PopupWindow popupWindow;
    private float progress = -1;
    private String text = null;
    /**
     * 最后缩小的位移目的地
     */
    private float toX = 0f, toY = 0f;
    private int progressDuration = 2000, finishDuration = 2000, dismissDuration = 1000;

    public FavorabilityProgressBarDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_favorability, null);

        progressBar = (FavorabilityCircleProgressBar) view.findViewById(R.id.pb_favorability);
        progressBar.setOnCompleteLayoutListener((w, h) -> {
            if (progress >= 0f) {
                if (text != null)
                    progressBar.setProgress(progress, progressDuration, finishDuration, text);
                else
                    progressBar.setProgress(progress);
            }
        });
        progressBar.setOnFinishAnimationListener(() -> startAnimationAfterProgress());
        progressBar.setPivotX(0f);
        progressBar.setPivotY(0f);

        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x6F000000));
    }

    public FavorabilityProgressBarDialog progress(float value) {
        progress = value;
        return this;
    }

    public FavorabilityProgressBarDialog text(String str) {
        text = str;
        return this;
    }

    public FavorabilityProgressBarDialog toX(float x) {
        toX = x;
        return this;
    }

    public FavorabilityProgressBarDialog toY(float y) {
        toY = y;
        return this;
    }

    public FavorabilityProgressBarDialog duration(int progressDuration, int finishDuration, int dismissDuration) {
        return this.progressDuration(progressDuration).finishDuration(finishDuration).dismissDuration(dismissDuration);
    }

    public FavorabilityProgressBarDialog progressDuration(int duration) {
        progressDuration = duration;
        return this;
    }

    public FavorabilityProgressBarDialog finishDuration(int duration) {
        finishDuration = duration;
        return this;
    }

    public FavorabilityProgressBarDialog dismissDuration(int duration) {
        dismissDuration = duration;
        return this;
    }

    public void startAnimationAfterProgress() {

        final float transX = progressBar.getX() - toX;
        final float transY = progressBar.getY() - toY;
        Log.i("zy", transX + " " + transY + " " + toX + " " + toY);

        ValueAnimator animator = ValueAnimator.ofFloat(1f, 0f);
        animator.setDuration(dismissDuration);
        animator.addUpdateListener(animation -> {
            float value = (float) animation.getAnimatedValue();

            progressBar.setX(toX + transX * value);
            progressBar.setY(toY + transY * value);
            progressBar.setScaleX(value);
            progressBar.setScaleY(value);
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                popupWindow.dismiss();
                progressBar = null;
                popupWindow = null;
            }
        });
        animator.start();
    }

    public void show(View parent) {
        popupWindow.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }
}
