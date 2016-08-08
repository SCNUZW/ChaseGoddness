package zy.chasegoddness.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zy on 2016/8/8.
 * 情感分析显示正负面百分比的饼状图
 */
public class SentimentBar extends View {

    public SentimentBar(Context context) {
        super(context);
    }

    public SentimentBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SentimentBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SentimentBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


}
