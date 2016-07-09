package zy.chasegoddness.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.AutoHideButtonFloat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import zy.chasegoddness.R;
import zy.chasegoddness.model.FriendsLoginModel;
import zy.chasegoddness.model.bean.FriendsContent;
import zy.chasegoddness.model.bean.User;
import zy.chasegoddness.ui.dialog.FriendsLoginDialog;
import zy.chasegoddness.ui.view.CircleImageView;
import zy.chasegoddness.ui.view.IconButton;
import zy.chasegoddness.ui.view.RefreshRecyclerView;

/**
 * 分享圈的界面
 */
public class FriendsActivity extends BaseActivity {

    private List<FriendsContent> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        initView();
        initAccount();
    }

    private final void initView() {
        btn_add = (AutoHideButtonFloat) findViewById(R.id.btn_friends_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_setting = (IconButton) findViewById(R.id.btn_friends_setting);
        btn_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        rrv_friends = (RefreshRecyclerView) findViewById(R.id.rrv_friends);
        rrv_friends.setAdapter(new FriendsAdapter());
        rrv_friends.setOnRefreshListener(new RefreshRecyclerView.OnRefreshListener() {
            @Override
            public void onRefresh(boolean top) {

            }
        });
    }

    private void initAccount() {
        User user = BmobUser.getCurrentUser(User.class);
        if (user != null) {

        } else {
            FriendsLoginModel.showDialog(getSupportFragmentManager());
        }
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, FriendsActivity.class));
    }

    private AutoHideButtonFloat btn_add;
    private RefreshRecyclerView rrv_friends;
    private IconButton btn_setting;

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
            if (author.getAvatar() != null && author.getAvatar().getLocalFile() != null) {//如果有头像
                String avatarPath = author.getAvatar().getLocalFile().getAbsolutePath();
                holder.civ_avatar.setImageBitmap(BitmapFactory.decodeFile(avatarPath));
            } else {
                holder.civ_avatar.setImageResource(R.drawable.default_avatar);
            }

            /*2.用户名*/
            holder.tv_name.setText(author.getUsername());

            /*3.发布的内容*/
            holder.tv_content.setText(content.getContent());

            /*4.发布的图片*/
            if (content.getPic1() != null) {//如果有图片
                holder.ll_picGroup.setVisibility(View.VISIBLE);
                String pic1Path = content.getPic1().getLocalFile().getAbsolutePath();
                holder.pic1.setImageBitmap(BitmapFactory.decodeFile(pic1Path));
            } else {
                holder.ll_picGroup.setVisibility(View.GONE);
            }

            if (content.getPic2() != null) {
                String pic2Path = content.getPic2().getLocalFile().getAbsolutePath();
                holder.pic2.setImageBitmap(BitmapFactory.decodeFile(pic2Path));
                holder.pic2.setVisibility(View.VISIBLE);
            } else {
                holder.pic3.setVisibility(View.INVISIBLE);
            }

            if (content.getPic3() != null) {
                String pic3Path = content.getPic3().getLocalFile().getAbsolutePath();
                holder.pic3.setImageBitmap(BitmapFactory.decodeFile(pic3Path));
                holder.pic3.setVisibility(View.VISIBLE);
            } else {
                holder.pic3.setVisibility(View.INVISIBLE);
            }
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
}
