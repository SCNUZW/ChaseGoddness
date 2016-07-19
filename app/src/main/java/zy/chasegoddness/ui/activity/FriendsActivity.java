package zy.chasegoddness.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFloat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.ProgressCallback;
import zy.chasegoddness.R;
import zy.chasegoddness.model.FriendsLoginModel;
import zy.chasegoddness.model.bean.FriendsContent;
import zy.chasegoddness.model.bean.User;
import zy.chasegoddness.presenter.FriendsPresenter;
import zy.chasegoddness.ui.activity.iactivity.IFriendsRegisterView;
import zy.chasegoddness.ui.activity.iactivity.IFriendsView;
import zy.chasegoddness.ui.dialog.FriendsLoginDialog;
import zy.chasegoddness.ui.view.CircleImageView;
import zy.chasegoddness.ui.view.IconButton;
import zy.chasegoddness.ui.view.RefreshRecyclerView;

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
            //TODO:发布分享圈事件
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

        private final LruCache<String, Bitmap> bitmapCache;

        public FriendsAdapter() {
            int maxMemory = (int) Runtime.getRuntime().maxMemory();
            int mCacheSize = maxMemory / 8;
            Log.i("zy", "Friends Activity maxMemory = " + maxMemory + " , " + maxMemory / 1024 + "KB, " + maxMemory / 1024 / 1024 + "MB");

            bitmapCache = new LruCache<String, Bitmap>(mCacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount();
                }
            };
        }

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
                showPicture(author.getAvatar(), holder.civ_avatar, R.drawable.default_avatar);
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
                holder.pic1.setOnClickListener(v -> {
                    final String url = content.getPic1().getUrl();
                    presenter.showBigImage(bitmapCache.get(url), url);
                });
                showPicture(content.getPic1(), holder.pic1, R.drawable.default_image);
            } else {
                holder.ll_picGroup.setVisibility(View.GONE);
            }

            if (content.getPic2() != null) {
                holder.pic2.setOnClickListener(v -> {
                    final String url = content.getPic2().getUrl();
                    presenter.showBigImage(bitmapCache.get(url), url);
                });
                showPicture(content.getPic2(), holder.pic2, R.drawable.default_image);
            } else {
                holder.pic2.setVisibility(View.INVISIBLE);
            }

            if (content.getPic3() != null) {
                holder.pic3.setOnClickListener(v -> {
                    final String url = content.getPic3().getUrl();
                    presenter.showBigImage(bitmapCache.get(url), url);
                });
                showPicture(content.getPic3(), holder.pic3, R.drawable.default_image);
            } else {
                holder.pic3.setVisibility(View.INVISIBLE);
            }
        }

        private void showPicture(BmobFile file, ImageView view, int defaultResourse) {
            if (!showCachePicture(file, view)) {
                view.setImageResource(defaultResourse);
                if (!showDiskPicture(file, view))
                    showNetworkPicture(file, view);
            }
        }

        private boolean showCachePicture(BmobFile pic, ImageView view) {
            view.setVisibility(View.VISIBLE);
            final String url = pic.getUrl();
            Bitmap bitmap;
            if ((bitmap = bitmapCache.get(url)) != null) {
                view.setImageBitmap(bitmap);
                return true;
            }
            return false;
        }

        private boolean showDiskPicture(BmobFile file, ImageView view) {
            File pic = file.getLocalFile();
            if (pic == null) return false;

            Bitmap bitmap = BitmapFactory.decodeFile(pic.getAbsolutePath());
            view.setImageBitmap(bitmap);
            return true;
        }

        private void showNetworkPicture(BmobFile file, ImageView view) {
            final String url = file.getUrl();
            view.setTag(url);
            file.download(new DownloadFileListener() {
                @Override
                public void done(String path, BmobException e) {
                    Bitmap pic = BitmapFactory.decodeFile(path);
                    if (pic != null) {
                        bitmapCache.put(url, pic);
                        if (view.getTag().equals(url)) {
                            view.setImageBitmap(pic);
                        }
                    } else {
                        showCachePicture(file, view);
                    }
                }

                @Override
                public void onProgress(Integer integer, long l) {
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            CircleImageView civ_avatar;
            TextView tv_name, tv_content;
            LinearLayout ll_picGroup;
            IconButton pic1, pic2, pic3;

            public ViewHolder(View view) {
                super(view);

                civ_avatar = (CircleImageView) view.findViewById(R.id.civ_friends_avatar);
                tv_content = (TextView) view.findViewById(R.id.tv_friends_content);
                tv_name = (TextView) view.findViewById(R.id.tv_friends_username);
                ll_picGroup = (LinearLayout) view.findViewById(R.id.ll_friends_pic_group);
                pic1 = (IconButton) view.findViewById(R.id.ib_friends_pic1);
                pic2 = (IconButton) view.findViewById(R.id.ib_friends_pic2);
                pic3 = (IconButton) view.findViewById(R.id.iv_friends_pic3);
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
