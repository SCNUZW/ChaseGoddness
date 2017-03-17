package zy.chasegoddness.ui.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import com.nineoldandroids.animation.ObjectAnimator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zy on 2016/7/27.
 * 侧滑隐藏的菜单
 */
public class SideMenu extends RelativeLayout {
    public SideMenu(Context context) {
        super(context);
        init(context);
    }

    public SideMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SideMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SideMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private View menu;
    private List<View> menuItem = new ArrayList<>();

    protected void init(Context context) {
    }

    private boolean trigger = false;
    private float pressX, pressY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            pressX = event.getX();
            pressY = event.getY();
            trigger = true;
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            //若水平方向上移动较大 则拦截事件进行处理
            if (Math.abs(event.getX() - pressX) > Math.abs(event.getY() - pressY))
                return true;
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (pressX - event.getX() > 100 && trigger) {
                trigger = false;
                openMenu();
                return true;
            } else if (event.getX() - pressX > 100 && trigger) {
                trigger = false;
                closeMenu();
                return true;
            }
        }
        return false;
    }

    private boolean isOpen = false;

    public void openMenu() {
        if (menu != null) {
            final int width = menu.getWidth();
            for (int i = 0; i < menuItem.size(); i++) {
                View child = menuItem.get(i);
                if (child.getTranslationX() < width) return;//如果菜单已经在打开的状态或正在运动的状态 不播放动画
                menu.setBackgroundColor(0x3fff9fcf);
                postDelayed(() -> ObjectAnimator.ofFloat(child, "translationX", width, -width / 8f, 0).setDuration(500).start(), i * 50);
            }
            isOpen = true;
        }
    }

    public void closeMenu() {
        if (menu != null) {
            final int width = menu.getWidth();
            for (int i = 0; i < menuItem.size(); i++) {
                View child = menuItem.get(i);
                if (child.getTranslationX() > 0) return;//如果菜单已经在关闭的状态或正在运动的状态 不播放动画
                menu.setBackgroundColor(0x00000000);
                post(() -> ObjectAnimator.ofFloat(child, "translationX", 0, width).setDuration(500).start());
            }
            isOpen = false;
        }
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setSideMenuResourse(int resId) {
        if (menu != null)
            throw new IllegalStateException("can not set layout resourse of sideMenu again!");

        menu = LayoutInflater.from(getContext()).inflate(resId, SideMenu.this, false);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.addRule(ALIGN_PARENT_RIGHT);
        addView(menu, lp);

        if (menu instanceof ViewGroup) {
            ViewGroup menuGroup = (ViewGroup) menu;
            int cnt = menuGroup.getChildCount();
            for (int i = 0; i < cnt; i++) {
                View child = menuGroup.getChildAt(i);
                menuItem.add(child);
                final int pos = i;
                if (itemListener != null) {
                    View button = ((ViewGroup) child).getChildAt(0);
                    button.setOnClickListener(view -> itemListener.onClick(view, pos));
                }
            }
        }
        menu.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = menu.getWidth();
                for (View child : menuItem) {
                    child.setTranslationX(width);
                }
                menu.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    private OnClickMenuItemListener itemListener;

    public void setOnClickMenuItemListener(OnClickMenuItemListener listener) {
        itemListener = listener;

        for (int i = 0; i < menuItem.size(); i++) {
            View child = menuItem.get(i);
            final int pos = i;
            if (itemListener != null) {
                View button = ((ViewGroup) child).getChildAt(0);
                button.setOnClickListener(view -> itemListener.onClick(view, pos));
                //child.setOnClickListener(view -> itemListener.onClick(view, pos));
            }
        }
    }

    public interface OnClickMenuItemListener {
        void onClick(View v, int postion);
    }

//    public interface onMenuListener {
//        void onOpen();
//
//        void onClose();
//    }
}
