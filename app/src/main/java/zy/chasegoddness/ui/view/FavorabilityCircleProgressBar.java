package zy.chasegoddness.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;

/**
 * Created by Administrator on 2016/6/30.
 */
public class FavorabilityCircleProgressBar extends View {

    /**
     * 直径
     */
    private int diameter;
    /**
     * 圆心坐标
     */
    private float centerX, centerY;
    /**
     * 进度条的宽度
     */
    private float progressWidth;
    /**
     * 弧形外框的宽度
     */
    private float arcWidth;
    /**
     * 刻度的宽度
     */
    private float degreeLongWidth, degreeShortWidth;
    /**
     * 外圈刻度的长度
     */
    private float degreeLongLen, degreeShortLen;
    /**
     * 外圈刻度与进度条的距离
     */
    private final float DEGREE_PROGRESS_DISTANCE = dp2px(8);
    /**
     * 环形进度条的外接矩形
     */
    private RectF bgRect;
    /**
     * 进度条的最大值
     */
    private int maxValue;
    /**
     * 目前的进度
     */
    private float curValue;
    private float curAngle;
    /**
     * 外观颜色
     */
    private int degreeColor, arcColor;
    private int[] progressColors;
    private Paint arcPaint, progressPaint, degreePaint, textPaint, wavePaint, basePaint;

    /**
     * 波浪水平面的中线，浪高峰，浪低谷,浪高度
     */
    private float mid, max, min, waveHeight;
    /**
     * 波浪的起伏间隔 一个完整波浪（包括一个高峰和一个低谷）长度为 4 * interval
     */
    private float interval;
    /**
     * 波浪动画的位移速度
     */
    private float offset;
    /**
     * 波浪的数目
     */
    private int waveNum;
    private Path circlePath, wavePath;
    private int[] waveColors;
    /**
     * 可选在动画即将结束时显示字体
     */
    private String text;
    private float textAlpha, textHeight;

    public FavorabilityCircleProgressBar(Context context) {
        super(context);
        init();
    }

    public FavorabilityCircleProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FavorabilityCircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FavorabilityCircleProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private final void init() {
        //默认线条宽度
        progressWidth = dp2px(8);
        arcWidth = dp2px(12);
        degreeLongWidth = dp2px(4);
        degreeShortWidth = dp2px(2);
        degreeLongLen = dp2px(8);
        degreeShortLen = dp2px(5);

        //外观颜色
        degreeColor = 0xFFEBEBEB;
        arcColor = 0xFFEBEBEB;
        progressColors = new int[]{0xFFCD8500, 0xFFEE82EE, 0xFFFF3E96, 0xFFEE0000, 0xFFCD8500};
        waveColors = new int[]{0xFFFF6A6A, 0xFFFFFFFF};

        //进度条数值
        maxValue = 100;
        curValue = 0;

        //波浪数值
        mid = 0;
        waveNum = 3;

        //外部刻度线
        degreePaint = new Paint();
        degreePaint.setColor(degreeColor);

        //整个弧形
        arcPaint = new Paint();
        arcPaint.setAntiAlias(true);
        arcPaint.setStyle(Paint.Style.STROKE);
        arcPaint.setStrokeWidth(arcWidth);
        arcPaint.setColor(arcColor);
        arcPaint.setStrokeCap(Paint.Cap.ROUND);

        //当前进度的弧形
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(progressWidth);

        //完成时字体
        textPaint = new Paint();
        textPaint.setTextSize(sp2px(30));
        textPaint.setColor(0xFF000000);

        //波浪
        wavePaint = new Paint();
        wavePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        //水瓶
        basePaint = new Paint();

        circlePath = new Path();
        wavePath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //如果没有指定控件的大小 则采用默认的直径大小来计算控件的大小
        int width = getDefaultSize(getSuggestedSize(), widthMeasureSpec);
        int height = getDefaultSize(getSuggestedSize(), heightMeasureSpec);
        int size = Math.min(width, height);

        if (size != getSuggestedSize())//如果已经指定控件的大小 则通过控件大小计算进度条的直径
            diameter = getActualDiameter(width);

        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //弧形的矩阵区域
        bgRect = new RectF();
        bgRect.top = degreeLongLen + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.left = degreeLongLen + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.right = diameter + (degreeLongLen + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE);
        bgRect.bottom = diameter + (degreeLongLen + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE);

        //圆心
        centerX = (2 * degreeLongLen + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE) / 2;
        centerY = (2 * degreeLongLen + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE) / 2;

        //设置渐变色
        SweepGradient sweepGradient = new SweepGradient(centerX, centerY, progressColors, null);
        Matrix rotateMatrix = new Matrix();
        rotateMatrix.setRotate(45, centerX, centerY);
        sweepGradient.setLocalMatrix(rotateMatrix);
        progressPaint.setShader(sweepGradient);

        int[] colors = new int[]{0xFFFFFFFF, 0xFFE0FFFF, 0xFFCAE1FF};
        basePaint.setShader(new RadialGradient(centerX, centerY, diameter / 2f, colors, null, Shader.TileMode.CLAMP));

        //设置波浪参数
        mid = bgRect.bottom;
        max = min = mid;
        waveHeight = dp2px(6);
        interval = w / (waveNum * 4);
        offset = -4 * interval;

        if (onCompleteLayoutListener != null) {
            onCompleteLayoutListener.onCompleteLayout(w, h);
            onCompleteLayoutListener = null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //刻度
        drawDegree(canvas);
        //水瓶
        canvas.drawCircle(bgRect.centerX(), bgRect.centerY(), diameter / 2f, basePaint);
        //水面
        drawWave(canvas);
        //整个弧
        canvas.drawArc(bgRect, 45, 90 + 270, false, arcPaint);
        //当前进度
        canvas.drawArc(bgRect, 45, 90 + curAngle, false, progressPaint);
        //字体
        drawText(canvas);
    }

    private final void drawDegree(Canvas canvas) {
        /**
         * 从135度开始到225度 每9度都标上刻度
         */
        canvas.save();
        for (int i = 0; i < 40; i++) {
            if (i > 15 && i < 25) {
                canvas.rotate(9, centerX, centerY);
                continue;
            }
            if (i % 5 == 0) {
                degreePaint.setStrokeWidth(degreeLongWidth);
                degreePaint.setColor(degreeColor);
                canvas.drawLine(centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE,
                        centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - degreeLongLen, degreePaint);
            } else {
                degreePaint.setStrokeWidth(degreeShortWidth);
                degreePaint.setColor(degreeColor);
                canvas.drawLine(centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - (degreeLongLen - degreeShortLen) / 2,
                        centerX, centerY - diameter / 2 - progressWidth / 2 - DEGREE_PROGRESS_DISTANCE - (degreeLongLen - degreeShortLen) / 2 - degreeShortLen, degreePaint);
            }
            canvas.rotate(9, centerX, centerY);
        }
        canvas.restore();
    }

    private final void drawWave(Canvas canvas) {
        int saveFlag = canvas.save(Canvas.ALL_SAVE_FLAG);
        /**
         * 画足够长的水平波浪线
         */
        wavePath.reset();
        wavePath.moveTo(offset, mid);
        for (int i = 1; i <= (waveNum * 4) + 4; i += 4) {
            wavePath.quadTo(i * interval + offset, max, (i + 1) * interval + offset, mid);
            wavePath.quadTo((i + 2) * interval + offset, min, (i + 3) * interval + offset, mid);
        }
        wavePath.lineTo(((waveNum * 4) + 4 + 3) * interval + offset, bgRect.bottom);
        wavePath.lineTo(offset, bgRect.bottom);
        wavePath.close();

        /**
         * 画大小与进度条相同的圆
         */
        circlePath.reset();
        circlePath.addArc(bgRect, 0, 360);

        wavePaint.setShader(new LinearGradient(bgRect.centerX(), bgRect.bottom, bgRect.centerX(), max, waveColors, null, Shader.TileMode.REPEAT));

        canvas.clipPath(wavePath);
        canvas.clipPath(circlePath, Region.Op.INTERSECT);//两区域相交的地方就是波浪
        canvas.drawPaint(wavePaint);

        canvas.restoreToCount(saveFlag);
    }

    private final void drawText(Canvas canvas) {
        if (TextUtils.isEmpty(text))
            return;

        textPaint.setAlpha((int) (textAlpha * 255));
        float len = textPaint.measureText(text);
        canvas.drawText(text, centerX - len / 2f, centerY - textHeight, textPaint);
    }

    public void setProgress(float value) {
        setProgress(value, 2000);
    }

    public void setProgress(float value, int duration) {
        setProgress(value, duration, 1000, null);
    }

    public void setProgress(float value, int progressDuration, final int finishDuration, @Nullable final String text) {
        if (value > maxValue) {
            value = maxValue;
        }
        if (value < 0) {
            value = 0;
        }

        startProgressAnimator(curValue, value, progressDuration, new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                startFinishAnimator(text, finishDuration);
            }
        });
    }

    private void startProgressAnimator(float start, float end, int progressDuration, AnimatorListenerAdapter listener) {
        ValueAnimator progressAnimator = ValueAnimator.ofFloat(start, end);
        progressAnimator.setDuration(progressDuration);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                curValue = (float) animation.getAnimatedValue();
                updateProgressBar(curValue);
                updateWave(curValue);
                postInvalidate();
            }
        });
        progressAnimator.setInterpolator(new DecelerateInterpolator());
        progressAnimator.addListener(listener);
        progressAnimator.start();
    }

    private void startFinishAnimator(@Nullable String text, int finishDuration) {
        this.text = text;
        final ValueAnimator finishAnimator = ValueAnimator.ofFloat(0f, 1f);
        finishAnimator.setDuration(finishDuration);
        finishAnimator.setInterpolator(new DecelerateInterpolator());
        finishAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                updateText(value);
                updateWave(curValue);
                postInvalidate();
            }
        });
        finishAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (finishAnimationListener != null) {
                    finishAnimationListener.onFinish();
                    finishAnimationListener = null;
                }
            }
        });
        finishAnimator.start();
    }

    /**
     * 刷新波浪位移
     */
    private final void updateWave(float progress) {
        if (progress < 30) waveHeight = dp2px(12);
        else if (progress < 60) waveHeight = dp2px(9);
        else if (progress < 90) waveHeight = dp2px(7);
        else waveHeight = dp2px(5);

        mid = bgRect.bottom - progress / maxValue * diameter;
        max = mid - waveHeight;
        min = mid + waveHeight;

        if (offset > 0) {
            offset = -interval * 3;
        } else {
            offset += interval / 4;
        }
    }

    /**
     * 刷新进度条弧度
     */
    private final void updateProgressBar(float progress) {
        curAngle = progress * (270f / maxValue);
    }

    /**
     * 刷新字体显示的高度和透明度
     */
    private final void updateText(float progress) {
        textAlpha = 1 - progress;
        textHeight = progress * dp2px(40);
    }

    /**
     * 如果没有指定控件的大小 则采用默认的直径大小来计算控件的大小
     *
     * @return 控件的默认长度
     */
    private final int getSuggestedSize() {
        diameter = 3 * getScreenWidth() / 5;
        return (int) (2 * degreeLongLen + progressWidth + diameter + 2 * DEGREE_PROGRESS_DISTANCE);
    }

    /**
     * 如果已经指定控件的大小 则通过控件大小计算进度条的直径
     *
     * @return 进度条的直径
     */
    private final int getActualDiameter(int actualWidth) {
        return (int) (actualWidth - (2 * degreeLongLen + progressWidth + 2 * DEGREE_PROGRESS_DISTANCE));
    }

    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public void setOnCompleteLayoutListener(OnCompleteLayoutListener listener) {
        onCompleteLayoutListener = listener;
    }

    private OnCompleteLayoutListener onCompleteLayoutListener;

    public interface OnCompleteLayoutListener {
        void onCompleteLayout(int w, int h);
    }

    public void setOnFinishAnimationListener(OnFinishAnimationListener listener) {
        finishAnimationListener = listener;
    }

    private OnFinishAnimationListener finishAnimationListener;

    public interface OnFinishAnimationListener {
        void onFinish();
    }

    private float dp2px(float dip) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getContext().getResources().getDisplayMetrics());
    }

    private float sp2px(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getContext().getResources().getDisplayMetrics());
    }
}
