package zy.chasegoddness.ui.dialog;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;

import zy.chasegoddness.R;
import zy.chasegoddness.presenter.FriendsPostPresenter;
import zy.chasegoddness.ui.dialog.idialog.IFriendsPostView;
import zy.chasegoddness.ui.view.IconButton;

public class FriendsPostDialog extends DialogFragment implements IFriendsPostView {

    private IconButton ib_camera, ib_pic1, ib_pic2, ib_pic3;
    private ButtonFlat btn_post;
    private EditText et_content;
    private TextView tv_error;
    private FriendsPostPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_friends_post, container, false);
        initPresenter();
        initView(view);
        return view;
    }

    private void initPresenter() {
        presenter = new FriendsPostPresenter(this);
    }

    private void initView(View v) {
        ib_camera = (IconButton) v.findViewById(R.id.ib_friends_camera);
        ib_pic1 = (IconButton) v.findViewById(R.id.ib_friends_pic1);
        ib_pic2 = (IconButton) v.findViewById(R.id.ib_friends_pic2);
        ib_pic3 = (IconButton) v.findViewById(R.id.ib_friends_pic3);
        et_content = (EditText) v.findViewById(R.id.et_friends_content);
        btn_post = (ButtonFlat) v.findViewById(R.id.btn_friends_post);
        tv_error = (TextView) v.findViewById(R.id.tv_friends_error);

        ib_camera.setOnClickListener(view -> presenter.chosePicture());
        btn_post.setOnClickListener(view -> presenter.post());
    }

    public static void showDialog(FragmentManager manager) {
        new FriendsPostDialog().show(manager, "FriendsPostDialog");
    }

    @Override
    public void setPic1(Bitmap bitmap) {
        ib_pic1.setImageBitmap(bitmap);
        ib_pic1.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPic2(Bitmap bitmap) {
        ib_pic2.setImageBitmap(bitmap);
        ib_pic2.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPic3(Bitmap bitmap) {
        ib_pic3.setImageBitmap(bitmap);
        ib_pic3.setVisibility(View.VISIBLE);
    }

    @Override
    public String getContent() {
        return et_content.getText().toString();
    }

    @Override
    public void setContent(String content) {
        et_content.setText(content);
    }

    @Override
    public void showError(String e) {
        tv_error.setText(e);
        tv_error.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideError() {
        tv_error.setVisibility(View.INVISIBLE);
    }

    @Override
    public void postClickable() {
        btn_post.setClickable(true);
        btn_post.setText("发布");
        btn_post.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public void postUnClickable(String str) {
        btn_post.setText(str);
        btn_post.setBackgroundColor(getResources().getColor(R.color.colorUnClickable));
        btn_post.setClickable(false);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }
}
