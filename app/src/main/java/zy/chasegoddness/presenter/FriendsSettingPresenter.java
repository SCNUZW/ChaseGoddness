package zy.chasegoddness.presenter;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.ImageViewTarget;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.widget.GFImageView;
import zy.chasegoddness.R;
import zy.chasegoddness.model.FriendsLoginModel;
import zy.chasegoddness.model.FriendsUpdateModel;
import zy.chasegoddness.model.bean.User;
import zy.chasegoddness.ui.activity.iactivity.IFriendsSettingView;
import zy.chasegoddness.ui.dialog.FriendsLoginDialog;
import zy.chasegoddness.util.BitmapUtil;
import zy.chasegoddness.util.FileUtil;

/**
 * Created by Administrator on 2016/8/9.
 */
public class FriendsSettingPresenter {
    private IFriendsSettingView view;

    public FriendsSettingPresenter(IFriendsSettingView view) {
        this.view = view;
    }

    public void init() {
        User user = FriendsLoginModel.getCurrentUser();
        if (user == null) {
            FriendsLoginDialog.showDialog(view.getSupportFragmentManager());//弹出登陆对话框
            return;
        }

        if (user.getNickName() != null)
            view.setNickName(user.getNickName());
        else
            view.setNickName("未设置");
        if (user.getDesc() != null)
            view.setDescription(user.getDesc());
        else
            view.setDescription("我很潇洒");
        if (user.getAvatar() != null)
            view.setAvatar(user.getAvatar().getUrl());

        view.hideError();
    }

    public void chosePicture() {
        view.hideError();
        final int REQUEST_CODE_GALLERY = 120;
        Resources resources = view.getContext().getResources();
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(resources.getColor(R.color.colorPrimary))
                .setFabNornalColor(resources.getColor(R.color.colorPrimary))
                .setFabPressedColor(resources.getColor(R.color.colorPrimaryDark))
                .setTitleBarTextColor(0xffffffff)
                .setCheckSelectedColor(resources.getColor(R.color.colorPrimary))
                .build();
        CoreConfig core = new CoreConfig.Builder(view.getContext(), new ImageLoader() {
            @Override
            public void displayImage(Activity activity, String path, GFImageView imageView, Drawable defaultDrawable, int width, int height) {
                Glide.with(activity)
                        .load("file://" + path)
                        .placeholder(defaultDrawable)
                        .error(defaultDrawable)
                        .override(width, height)
                        .diskCacheStrategy(DiskCacheStrategy.NONE) //不缓存到SD卡
                        .skipMemoryCache(true)
                        .into(new ImageViewTarget<GlideDrawable>(imageView) {
                            @Override
                            protected void setResource(GlideDrawable resource) {
                                imageView.setImageDrawable(resource);
                            }

                            @Override
                            public void setRequest(Request request) {
                                imageView.setTag(R.id.adapter_item_tag_key, request);
                            }

                            @Override
                            public Request getRequest() {
                                return (Request) imageView.getTag(R.id.adapter_item_tag_key);
                            }
                        });
            }

            @Override
            public void clearMemoryCache() {

            }
        }, theme)
                .setNoAnimcation(true)
                .setTakePhotoFolder(FileUtil.getDiskCacheDir(view.getContext(), "photo"))
                .build();
        FunctionConfig config = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableCrop(true)
                .build();
        GalleryFinal.init(core);
        GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, config, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (reqeustCode == REQUEST_CODE_GALLERY && resultList.size() > 0) {
                    PhotoInfo info = resultList.get(0);
                    String path = info.getPhotoPath();
                    view.setAvatar(path);
                    FriendsUpdateModel.updateAvatar(path);
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                Log.e("zy", "photo error: " + errorMsg);
                view.showError("选择图片失败");
            }
        });
    }

    public boolean checkLogin() {
        if (FriendsLoginModel.getCurrentUser() == null) {
            FriendsLoginDialog.showDialog(view.getSupportFragmentManager());
            return false;
        }
        return true;
    }
}
