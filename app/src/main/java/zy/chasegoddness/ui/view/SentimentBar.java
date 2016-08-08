package zy.chasegoddness.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
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
     * 小比率的颜色
     */
    private final static int COLOR_OUTSIDE = 0xFFFFE4B5;
    /**
     * 大比率的颜色
     */
    private final static int COLOR_INSIDE = 0xFFFFBBFF;
    /**
     * 饼状图的外围半径 即是控件的宽度的1/2
     */
    private int radius_outside;
    /**
     * 中半径
     */
    private int radius_inside;

    private double good = 0d;
    private double bad = 0d;

    private int thin_width = 70;
    private int wide_width = 100;

    private RectF rect_inside;
    private RectF rect_outside;

    public SentimentBar(Context context) {
        super(context);
        init(context);
    }

    public SentimentBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SentimentBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SentimentBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private Paint paint_outside;
    private Paint paint_inside;

    private void init(Context context) {
        paint_outside = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_inside = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint_outside.setStrokeWidth(thin_width);
        paint_outside.setStyle(Paint.Style.STROKE);
        paint_outside.setStrokeCap(Paint.Cap.BUTT);
        paint_outside.setColor(COLOR_OUTSIDE);

        paint_inside.setStrokeWidth(wide_width);
        paint_inside.setStyle(Paint.Style.STROKE);
        paint_inside.setStrokeCap(Paint.Cap.BUTT);
        paint_inside.setColor(COLOR_INSIDE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int minRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_RADIUS, getResources().getDisplayMetrics());
        int width = getDefaultSize(minRadius, widthMeasureSpec);
        int height = getDefaultSize(minRadius, heightMeasureSpec);

        radius_outside = Math.min(width, height) / 2;
        radius_inside = radius_outside - wide_width / 2;

        int padding = radius_outside - radius_inside;
        rect_inside = rect_outside
                = new RectF(padding, padding, padding + radius_inside * 2, padding + radius_inside * 2);

        setMeasuredDimension(radius_outside * 2, radius_outside * 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        double sentiment = good;
        if (good == 0d) {
            sentiment = 1 - bad;
        }
        int angle = (int) (sentiment * 360);
        canvas.drawArc(rect_inside, 300, angle, false, paint_inside);
        canvas.drawArc(rect_outside, (300 + angle) % 360, 360 - angle, false, paint_outside);
        canvas.restore();
    }

    public void setGoodSentiment(double good) {
        this.good = good;
    }

    public void setBadSentiment(double bad) {
        this.bad = bad;
    }

    public double getGoodSentiment() {
        return good;
    }

    public double getBadSentiment() {
        return bad;
    }
}
