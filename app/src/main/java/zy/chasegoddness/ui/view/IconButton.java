package zy.chasegoddness.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.gc.materialdesign.utils.Utils;

/**
 * 带点击效果的ImageView
 */
public class IconButton extends ImageView {
    public IconButton(Context context) {
        super(context);
        init(context);
    }

    public IconButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IconButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public IconButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private Paint paint;
    private Paint basePaint;
    private int maxRadius;

    private void init(Context context) {
        paint = new Paint();
        basePaint = new Paint();
        basePaint.setColor(0x45ffffff);
        basePaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    /**
     * 图片是否受到了点击
     */
    private boolean clicked = false;
    /**
     * 点击的位置
     */
    private float x, y;
    /**
     * 涟漪动画
     */
    ValueAnimator animator;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (animator != null) animator.cancel();

            x = event.getX();
            y = event.getY();

            clicked = true;
            startAnimator();
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        maxRadius = w > h ? w : h;
    }

    /**
     * 点击涟漪的半径
     */
    private float radius;
    private final int[] colors = {0xccffffff, 0x55ffffff};

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (clicked && radius > 0) {
            paint.setShader(new RadialGradient(x, y, radius, colors, null, Shader.TileMode.CLAMP));
            canvas.drawCircle(x, y, radius, paint);
            canvas.drawRect(0, 0, getWidth(), getHeight(), basePaint);
        }
    }

    private void startAnimator() {
        animator = ValueAnimator.ofFloat(0, maxRadius);
        animator.setDuration(300);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(animation -> {
            radius = (float) animation.getAnimatedValue();
            postInvalidate();
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                clicked = false;
                animator = null;
            }
        });
        animator.start();
    }
}
