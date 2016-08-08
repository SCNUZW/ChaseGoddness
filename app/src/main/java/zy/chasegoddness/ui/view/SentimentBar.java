package zy.chasegoddness.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * Created by zy on 2016/8/8.
 * 情感分析显示正负面百分比的饼状图
 */
public class SentimentBar extends View {

    /**
     * 控件默认的宽度
     */
    private final static int DEFAULT_RADIUS = 200;
    /**
     * 饼状图的外围半径 即是控件的宽度的1/2
     */
    private int radius_outside;
    private int radius_inside;

    

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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_RADIUS, getResources().getDisplayMetrics());
        int width = getDefaultSize(minRadius, widthMeasureSpec);
        int height = getDefaultSize(minRadius, heightMeasureSpec);
        radius_outside = Math.min(width, height) / 2;
        setMeasuredDimension(radius_outside * 2, radius_outside * 2);
    }
}
