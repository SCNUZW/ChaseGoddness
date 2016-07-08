package zy.chasegoddness.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/6/29.
 */
public class WaveLineView extends View {

    private int width;
    private int height;
    /**
     * 波浪动画的位移量
     */
    private int offset;
    /**
     * 波浪峰值
     */
    private int max, min;
    /**
     * 波浪的中线
     */
    private int mid;
    /**
     * 每一个波浪由两段长度为2 * interval的贝塞尔曲线组成<br>
     * 分别是凸的曲线和凹的曲线<br>
     * 每一段interval都是 mid和max之间 或者是 mid和min之间的变化曲线
     */
    private int interval;
    /**
     * 初始的波浪数
     */
    private int waveNum = 6;

    private Paint wavePaint;
    private Path wavePath;

    public WaveLineView(Context context) {
        super(context);
        init();
    }

    public WaveLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public WaveLineView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private final void init() {
        wavePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        wavePaint.setColor(Color.BLUE);
        wavePaint.setStrokeWidth(3f);
        wavePaint.setStyle(Paint.Style.STROKE);

        wavePath = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;

        mid = height / 2;
        max = height / 3;
        min = height * 2 / 3;
        interval = width / (waveNum * 4);

        offset = -4 * interval;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        wavePath.reset();
        wavePath.moveTo(offset, mid);
        for (int i = 1; i <= (waveNum * 4) + 4; i += 4) {
            wavePath.quadTo(i * interval + offset, max, (i + 1) * interval + offset, mid);
            wavePath.quadTo((i + 2) * interval + offset, min, (i + 3) * interval + offset, mid);
        }

        canvas.drawPath(wavePath, wavePaint);

        if (offset > 0) {
            offset = -interval * 3;
        } else {
            offset += interval / 2;
        }
        postInvalidateDelayed(50);
    }
}
