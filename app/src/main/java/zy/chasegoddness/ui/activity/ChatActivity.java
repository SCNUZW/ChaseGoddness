package zy.chasegoddness.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.views.Card;
import com.gc.materialdesign.views.LayoutRipple;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;
import rx.schedulers.Schedulers;
import zy.chasegoddness.R;
import zy.chasegoddness.global.LocalDB;
import zy.chasegoddness.global.RxBus;
import zy.chasegoddness.model.bean.LocalSms;
import zy.chasegoddness.presenter.ChatPresenter;
import zy.chasegoddness.ui.activity.iactivity.IChatView;
import zy.chasegoddness.ui.dialog.EmotionAnalyzeDialog;
import zy.chasegoddness.ui.view.RefreshRecyclerView;

public class ChatActivity extends BaseActivity implements IChatView {

    private RefreshRecyclerView chatView;
    private ChatPresenter presenter;
    private ChatAdapter adapter;
    private List<LocalSms> list = new ArrayList<>();
    private EditText et_content;
    private TextView tv_reply1, tv_reply2, tv_reply3;
    private ImageView iv_send, iv_ai;
    private LayoutRipple lr_reply1, lr_reply2, lr_reply3;
    private LinearLayout ll_ai, ll_hint;
    /**
     * 软键盘高度
     */
    private int keyboardHeight = 500;
    private final static int DEFAULT_KEYBOARD_HEIGHT = 500;
    /**
     * 记录软键盘收缩时的高度
     */
    private int lastBottom = 0;
    /**
     * 当下拉刷新的时候的最后可见位置
     */
    private int lastVisiblePos;
    /**
     * AI面板是否可见
     */
    private boolean isShowAI = false;

    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initPresenter();
        initView();
        initRxBus();
        initSoftInputHeight();
    }

    private final void initPresenter() {
        presenter = new ChatPresenter(this);
    }

    private final void initView() {
        et_content = (EditText) findViewById(R.id.et_send_content);
        ll_ai = (LinearLayout) findViewById(R.id.ll_chat_ai);
        ll_hint = (LinearLayout) findViewById(R.id.ll_chat_hint);
        lr_reply1 = (LayoutRipple) findViewById(R.id.lr_chat_auto_reply1);
        lr_reply2 = (LayoutRipple) findViewById(R.id.lr_chat_auto_reply2);
        lr_reply3 = (LayoutRipple) findViewById(R.id.lr_chat_auto_reply3);
        chatView = (RefreshRecyclerView) findViewById(R.id.rrv_chat);
        iv_send = (ImageView) findViewById(R.id.iv_chat_send);
        iv_ai = (ImageView) findViewById(R.id.iv_chat_ai);
        tv_reply1 = (TextView) findViewById(R.id.tv_chat_auto_reply1);
        tv_reply2 = (TextView) findViewById(R.id.tv_chat_auto_reply2);
        tv_reply3 = (TextView) findViewById(R.id.tv_chat_auto_reply3);

        //聊天面板
        chatView.setAdapter(adapter = new ChatAdapter());
        chatView.setOnRefreshListener(top -> {
            if (top) {
                lastVisiblePos = chatView.getLastVisiblePosition();
                presenter.refreshDate();
            }
        });

        //调出自动回复的界面的按钮
        iv_ai.setOnClickListener(v -> {
            if (!isShowAI) showAIView(true);
            else showAIView(false);
        });

        //自动回复界面上的可供回复选项 点击后显示在回复文本框上
        lr_reply1.setOnClickListener(v -> presenter.setReplyContent(1));
        lr_reply2.setOnClickListener(v -> presenter.setReplyContent(2));
        lr_reply3.setOnClickListener(v -> presenter.setReplyContent(3));

        // 发送短信按钮
        iv_send.setOnClickListener(v -> presenter.sendSms(et_content.getText().toString()));

        //初始化聊天数据
        presenter.refreshDate();
    }

    private void initRxBus() {
        RxBus.getInstance().toObserverable()
                .observeOn(Schedulers.immediate())
                .subscribe(event -> {
                    if (event.getDesc().equals("update ChatActivity")) {
                        presenter.refreshDate();
                    }
                }, throwable -> {
                    Log.e("zy", "ChatActivity RxBus error:" + throwable.toString());
                });
    }

    /**
     * 尝试获取软键盘的高度
     */
    private final void initSoftInputHeight() {
        final LocalDB db = new LocalDB(getContext());
        keyboardHeight = db.getKeyboardHeight();
        if (keyboardHeight == 0) {
            keyboardHeight = DEFAULT_KEYBOARD_HEIGHT;
        }
        //通过软键盘打开和收缩时chatView大小不同而计算软键盘的高度
        chatView.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            Rect rect = new Rect();
            chatView.getWindowVisibleDisplayFrame(rect);

            if (lastBottom > rect.bottom) {//软键盘弹起
                keyboardHeight = lastBottom - rect.bottom;

                db.putKeyboardHeight(keyboardHeight);

                //关闭自动回复面板
                showAIView(false);
                //把聊天面版拉回最新的消息
                if (list.size() > 0)
                    chatView.setLastVisiblePosition(list.size() - 1);
            }
            lastBottom = rect.bottom;
        });
    }

    public final void showAIView(boolean visible) {
        isShowAI = visible;
        if (visible) {

            hideKeyBoard();

            if (presenter.phoneNumExist()) {
                handler.postDelayed(() -> {
                    ViewGroup.LayoutParams layoutParams = ll_ai.getLayoutParams();
                    layoutParams.height = keyboardHeight;
                    ll_ai.setLayoutParams(layoutParams);
                    ll_ai.setVisibility(View.VISIBLE);

                    //把聊天面版拉回最新的消息
                    if (list.size() > 0)
                        chatView.setLastVisiblePosition(list.size() - 1);
                }, 100);
            } else {
                handler.postDelayed(() -> {
                    ViewGroup.LayoutParams layoutParams = ll_hint.getLayoutParams();
                    layoutParams.height = keyboardHeight;
                    ll_hint.setLayoutParams(layoutParams);
                    ll_hint.setVisibility(View.VISIBLE);
                }, 100);
            }

        } else {
            ll_ai.setVisibility(View.GONE);
            ll_hint.setVisibility(View.GONE);
        }
    }

    private final void showKeyBoard() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.showSoftInputFromInputMethod(et_content.getWindowToken(), InputMethodManager.SHOW_FORCED);
    }

    private final void hideKeyBoard() {
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (touchInChatView(ev)) {
            hideKeyBoard();
            showAIView(false);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Rect rect = new Rect();
            chatView.getWindowVisibleDisplayFrame(rect);

            if (rect.bottom < lastBottom) {//有键盘或者是ai面板展开
                hideKeyBoard();
                return true;
            } else if (isShowAI) {
                showAIView(false);
                return true;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    private final boolean touchInChatView(MotionEvent ev) {
        float touchX = ev.getX();
        float touchY = ev.getY();

        int[] location = new int[2];
        chatView.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];

        //如果触摸点在chatView的范围内
        if (x <= touchX && touchX <= x + chatView.getWidth()
                && y <= touchY && touchY <= y + chatView.getHeight()) {
            return true;
        }
        return false;
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ChatActivity.class));
    }

    @Override
    public void showSms(List<LocalSms> list) {
        this.list = list;
    }

    @Override
    public void insertSms(LocalSms sms) {
        this.list.add(0, sms);
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        chatView.setRefreshing(refreshing);
    }

    @Override
    public void notifyDataSetChanged(int resultSize) {
        chatView.setLastVisiblePosition(resultSize + lastVisiblePos - 1);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void clearEditText() {
        et_content.setText("");
    }

    @Override
    public void setReply1(String reply) {
        tv_reply1.setText(reply);
    }

    @Override
    public void setReply2(String reply) {
        tv_reply2.setText(reply);
    }

    @Override
    public void setReply3(String reply) {
        tv_reply3.setText(reply);
    }

    @Override
    public void setReplyOnEditText(String reply) {
        et_content.setText(reply);
    }

    class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.view_chat, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final LocalSms sms = list.get(position);

            holder.tv_content.setText(sms.getBody());
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.chatCard.getLayoutParams();

            if (sms.getType() == LocalSms.Type.RECIEVE_SMS) {
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
                holder.tv_content.setGravity(Gravity.LEFT);
                holder.btn_analyze.setVisibility(View.VISIBLE);
                holder.btn_analyze.setOnClickListener(v -> {
                    final String content = sms.getBody();
                    EmotionAnalyzeDialog.showDialog(getSupportFragmentManager(), content);
                });
            } else {
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                holder.tv_content.setGravity(Gravity.RIGHT);
                holder.btn_analyze.setVisibility(View.GONE);
            }

            holder.chatCard.setLayoutParams(params);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ButtonFlat btn_analyze;
            TextView tv_content;
            Card chatCard;

            public ViewHolder(View view) {
                super(view);
                btn_analyze = (ButtonFlat) view.findViewById(R.id.btn_emotion_analyze);
                chatCard = (Card) view.findViewById(R.id.c_chat_card);
                tv_content = (TextView) view.findViewById(R.id.tv_chat_content);
            }
        }
    }
}
