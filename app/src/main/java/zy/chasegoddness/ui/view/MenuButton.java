package zy.chasegoddness.ui.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import zy.chasegoddness.R;

/**
 * Created by Administrator on 2016/4/21.
 */
public class MenuButton extends Button {

    private ViewGroup rl_root;
    private Context context;
    /**
     * 展开按钮的数量
     */
    private final static int BTN_ITEM_NUM = 4;
    private Button[] btn_item = new Button[BTN_ITEM_NUM];
    /**
     * 按钮的icon
     */
    private static final int[] icon = new int[]{R.drawable.btn_friends, R.drawable.btn_chat, R.drawable.btn_thirdparty, R.drawable.btn_setting};
    private final int btn_icon = R.drawable.btn_menu;
    /**
     * 播放关闭菜单的动画时长
     */
    private final static int CLOSE_ANIMATION_DURATION = 300;
    /**
     * 播放展开菜单的旋转动画时长
     */
    private final static int OPEN_ROTATION_DURATION = 500;
    /**
     * 播放展开菜单的向上位移动画时长
     */
    private final static int OPEN_UP_DURATION = 300;
    /**
     * 展开动画时按钮围绕菜单按钮的半径
     */
    private float radius;
    /**
     * 开始动画前的坐标（菜单按钮所在的坐标）
     */
    private float startX, startY;
    /**
     * 菜单是否已经展开
     */
    private boolean isOpen = false;
    /**
     * 菜单是否已经关闭
     */
    private boolean isClose = true;
    /**
     * 向上展开时的动画
     */
    private AnimatorSet upAnimator;
    /**
     * 围绕菜单旋转展开时的动画
     */
    private ValueAnimator rotateAnimator;
    /**
     * 关闭菜单时的动画
     */
    private AnimatorSet closeAnimator;
    /**
     * 点击菜单项的事件监听器
     */
    private OnClickMenuListener mOnClickMenuListener;

    public MenuButton(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public MenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    public MenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MenuButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        initView();
    }

    private final void initView() {
        if (!isInEditMode())
            rl_root = (ViewGroup) ((Activity) getContext()).findViewById(android.R.id.content);

        setBackgroundResource(btn_icon);
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radius = rl_root.getWidth() / 2f;

                if (mOnClickMenuListener != null)
                    mOnClickMenuListener.onClickMenu(isClose);

                if (isOpen) {
                    closeMenu();
                }
                if (isClose) {
                    openMenu();
                }
                requestFocusFromTouch();
            }
        });
        setFocusable(true);
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (isOpen) {
                        if (mOnClickMenuListener != null)
                            mOnClickMenuListener.onClickMenu(isClose);

                        closeMenu();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private final void closeMenu() {
        isOpen = false;

        //取消展开的动画
        if (rotateAnimator != null)
            rotateAnimator.cancel();
        if (upAnimator != null)
            upAnimator.cancel();

        playCloseAnimator(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                closeAnimator = null;
                for (int i = 0; i < BTN_ITEM_NUM; i++) {
                    rl_root.removeView(btn_item[i]);
                    btn_item[i] = null;
                }
                isClose = true;
            }
        });
    }

    private final void openMenu() {
        isOpen = true;
        isClose = false;

        startX = getX();
        startY = getY();

        // 取消闭合的动画
        if (closeAnimator != null)
            closeAnimator.cancel();

        for (int i = 0; i < BTN_ITEM_NUM; i++) {
            final int id = i;
            btn_item[i] = new Button(context);
            btn_item[i].setBackgroundResource(icon[i]);
            btn_item[i].setLayoutParams(new ViewGroup.LayoutParams(getWidth(), getHeight()));
            btn_item[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClickMenuListener != null) {
                        boolean complete = mOnClickMenuListener.onClickItem(id);
                        if (complete) {
                            mOnClickMenuListener.onClickMenu(isClose);
                            closeMenu();
                        }
                    }
                }
            });
            rl_root.addView(btn_item[i]);
        }

        playOpenAnimation(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                upAnimator = null;

                if (isOpen) {
                    for (int i = 0; i < BTN_ITEM_NUM; i++)
                        playRotateAnimator(i);
                }
            }
        });
    }

    /**
     * 开始播放展开菜单的动画
     */
    private final void playOpenAnimation(Animator.AnimatorListener animatorListener) {
        ObjectAnimator[] up = new ObjectAnimator[BTN_ITEM_NUM];
        for (int i = 0; i < BTN_ITEM_NUM; i++) {
            btn_item[i].setX(getX());
            btn_item[i].setY(getY());

            up[i] = ObjectAnimator.ofFloat(btn_item[i], "Y", startY, startY - radius);
            up[i].setDuration(OPEN_UP_DURATION);
            up[i].setInterpolator(new DecelerateInterpolator());
        }

        upAnimator = new AnimatorSet();
        upAnimator.playTogether(up);
        upAnimator.addListener(animatorListener);
        upAnimator.start();
    }

    /**
     * 播放按钮i围绕着菜单按钮旋转的动画
     */
    private final void playRotateAnimator(final int i) {
        final float angle = (float) (Math.PI / 2f / (BTN_ITEM_NUM - 1) * i);
        rotateAnimator = ValueAnimator.ofFloat(0f, angle);
        rotateAnimator.setDuration(OPEN_ROTATION_DURATION);
        rotateAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                rotateAnimator = null;
            }
        });
        rotateAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (btn_item[i] != null) {
                    float angle = (float) animation.getAnimatedValue();
                    btn_item[i].setX((float) (startX - radius * Math.sin(angle)));
                    btn_item[i].setY((float) (startY - radius * Math.cos(angle)));
                }
            }
        });
        rotateAnimator.start();
    }

    private final void playCloseAnimator(Animator.AnimatorListener listener) {
        ObjectAnimator[] closeAnimatorX = new ObjectAnimator[BTN_ITEM_NUM];
        ObjectAnimator[] closeAnimatorY = new ObjectAnimator[BTN_ITEM_NUM];
        List<Animator> list = new ArrayList<>();
        for (int i = 0; i < BTN_ITEM_NUM; i++) {
            closeAnimatorX[i] = ObjectAnimator.ofFloat(btn_item[i], "X", btn_item[i].getX(), startX);
            closeAnimatorY[i] = ObjectAnimator.ofFloat(btn_item[i], "Y", btn_item[i].getY(), startY);
            closeAnimatorX[i].setDuration(CLOSE_ANIMATION_DURATION);
            closeAnimatorY[i].setDuration(CLOSE_ANIMATION_DURATION);
            list.add(closeAnimatorX[i]);
            list.add(closeAnimatorY[i]);
        }

        closeAnimator = new AnimatorSet();
        closeAnimator.playTogether(list);
        closeAnimator.addListener(listener);
        closeAnimator.start();
    }

    public interface OnClickMenuListener {
        boolean onClickItem(int id);

        void onClickMenu(boolean open);
    }

    public void setOnClickMenuListener(OnClickMenuListener listener) {
        mOnClickMenuListener = listener;
    }
}
