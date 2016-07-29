package zy.chasegoddness.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.gc.materialdesign.views.ButtonFloat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import zy.chasegoddness.R;
import zy.chasegoddness.global.RxBus;
import zy.chasegoddness.model.bean.FriendsContent;
import zy.chasegoddness.model.bean.User;
import zy.chasegoddness.presenter.FriendsPresenter;
import zy.chasegoddness.ui.activity.iactivity.IFriendsView;
import zy.chasegoddness.ui.dialog.FriendsPostDialog;
import zy.chasegoddness.ui.view.CircleImageView;
import zy.chasegoddness.ui.view.IconButton;
import zy.chasegoddness.ui.view.RefreshRecyclerView;
import zy.chasegoddness.util.GlideCircleTransform;

/**
 * 分享圈的界面
 */
public class FriendsActivity extends BaseActivity implements IFriendsView {

    private List<FriendsContent> list = new ArrayList<>();
    private ButtonFloat btn_add;
    private RefreshRecyclerView rrv_friends;
    private IconButton btn_setting;
    private FriendsAdapter mAdapter;
    private FriendsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        initPresenter();
        initView();
        initBus();
    }

    private final void initPresenter() {
        presenter = new FriendsPresenter(this);
    }

    private final void initView() {
        btn_add = (ButtonFloat) findViewById(R.id.btn_friends_add);
        btn_setting = (IconButton) findViewById(R.id.btn_friends_setting);
        rrv_friends = (RefreshRecyclerView) findViewById(R.id.rrv_friends);

        //点击发布按钮
        btn_add.setOnClickListener(v -> {
            if (presenter.checkForLogin())
                FriendsPostDialog.showDialog(getSupportFragmentManager());
        });

        //点击设置账号的按钮
        btn_setting.setOnClickListener(v -> {
            //TODO：设置分享圈账号信息
        });

        //分享圈的事件列表
        rrv_friends.setAdapter(mAdapter = new FriendsAdapter());
        rrv_friends.addItemDecoration(new Divider());
        rrv_friends.setOnRefreshListener(top -> {
            if (top) {
                presenter.refresh();
            } else {
                presenter.loadMore();
            }
        });

        presenter.init();
    }

    private void initBus() {
        RxBus.getInstance().toObserverable()
                .observeOn(Schedulers.immediate())
                .subscribe(event -> {
                    if (event.getDesc().equals("update friendsContent")) {
                        presenter.refresh();
                    }
                }, throwable -> Log.e("zy", "FriendsActivity RxBus error: " + throwable.toString()));
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, FriendsActivity.class));
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        rrv_friends.setRefreshing(refreshing);
    }

    @Override
    public void updateList(List<FriendsContent> list) {
        this.list.addAll(list);
    }

    @Override
    public void setList(List<FriendsContent> list) {
        this.list = list;
    }

    @Override
    public void updateItem(FriendsContent item) {
        this.list.add(item);
    }

    @Override
    public void notifyChanged() {
        mAdapter.notifyDataSetChanged();
    }

    class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.view_friends, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            FriendsContent content = list.get(position);
            User author = content.getAuthor();

            /*1.头像*/
            if (author.getAvatar() != null) {//如果有头像
                final String url = author.getAvatar().getUrl();
                Glide.with(FriendsActivity.this).load(url)
                        .transform(new GlideCircleTransform(getContext()))
                        .placeholder(R.drawable.default_avatar)
                        .into(holder.civ_avatar);
                holder.civ_avatar.setOnClickListener(v -> presenter.showBigImage(url));
            } else {
                holder.civ_avatar.setImageResource(R.drawable.default_avatar);
            }

            /*2.用户名*/
            if (author.getNickName() != null) {
                holder.tv_name.setText(author.getNickName());
            } else {
                holder.tv_name.setText(author.getUsername());
            }

            /*3.发布的内容*/
            holder.tv_content.setText(content.getContent());

            /*4.发布的图片 先缓存中存在的图片*/
            if (content.getPic1() != null) {//如果有图片
                holder.ll_picGroup.setVisibility(View.VISIBLE);
                final String url = content.getPic1().getUrl();
                Glide.with(FriendsActivity.this).load(url).placeholder(R.drawable.default_image).into(holder.pic1);
                holder.pic1.setVisibility(View.VISIBLE);
                holder.pic1.setOnClickListener(v -> presenter.showBigImage(url));
            } else {
                holder.ll_picGroup.setVisibility(View.GONE);
            }

            if (content.getPic2() != null) {
                final String url = content.getPic2().getUrl();
                Glide.with(FriendsActivity.this).load(url).placeholder(R.drawable.default_image).into(holder.pic2);
                holder.pic2.setVisibility(View.VISIBLE);
                holder.pic2.setOnClickListener(v -> presenter.showBigImage(url));
            } else {
                holder.pic2.setVisibility(View.INVISIBLE);
            }

            if (content.getPic3() != null) {
                final String url = content.getPic3().getUrl();
                Glide.with(FriendsActivity.this).load(url).placeholder(R.drawable.default_image).into(holder.pic3);
                holder.pic3.setVisibility(View.VISIBLE);
                holder.pic3.setOnClickListener(v -> presenter.showBigImage(url));
            } else {
                holder.pic3.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            IconButton civ_avatar;
            TextView tv_name, tv_content;
            LinearLayout ll_picGroup;
            IconButton pic1, pic2, pic3;

            public ViewHolder(View view) {
                super(view);

                civ_avatar = (IconButton) view.findViewById(R.id.ib_friends_avatar);
                tv_content = (TextView) view.findViewById(R.id.tv_friends_content);
                tv_name = (TextView) view.findViewById(R.id.tv_friends_username);
                ll_picGroup = (LinearLayout) view.findViewById(R.id.ll_friends_pic_group);
                pic1 = (IconButton) view.findViewById(R.id.ib_friends_pic1);
                pic2 = (IconButton) view.findViewById(R.id.ib_friends_pic2);
                pic3 = (IconButton) view.findViewById(R.id.ib_friends_pic3);
            }
        }
    }

    class Divider extends RecyclerView.ItemDecoration {
        Paint paint = new Paint();

        public Divider() {
            paint.setColor(getResources().getColor(R.color.colorUnClickable));
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            int count = parent.getChildCount();
            for (int i = 0; i < count; i++) {
                View child = parent.getChildAt(i);
                int top = child.getBottom();
                int bottom = top + 1;

                c.drawRect(left, top, right, bottom, paint);
            }
        }
    }
}
