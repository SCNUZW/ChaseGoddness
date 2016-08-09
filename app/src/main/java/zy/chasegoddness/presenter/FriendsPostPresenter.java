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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import cn.finalteam.galleryfinal.widget.GFImageView;
import zy.chasegoddness.R;
import zy.chasegoddness.global.RxBus;
import zy.chasegoddness.model.FriendsLoginModel;
import zy.chasegoddness.model.bean.FriendsContent;
import zy.chasegoddness.model.bean.User;
import zy.chasegoddness.ui.dialog.FriendsLoginDialog;
import zy.chasegoddness.ui.dialog.FriendsPostDialog;
import zy.chasegoddness.ui.dialog.idialog.IFriendsPostView;
import zy.chasegoddness.util.BitmapUtil;
import zy.chasegoddness.util.FileUtil;

public class FriendsPostPresenter {

    public IFriendsPostView view;
    String picPath1 = null, picPath2 = null, picPath3 = null;

    public FriendsPostPresenter(FriendsPostDialog view) {
        this.view = view;
    }

    public void post() {
        //1.如果未登陆 提示先登录 发布失败
        User author = FriendsLoginModel.getCurrentUser();
        if (author == null) {
            FriendsLoginDialog dialog = FriendsLoginDialog.showDialog(view.getFragmentManager());
            dialog.showError("请先登录");
            view.dismiss();
            return;
        }

        //2.发布信息
        String content = view.getContent();
        if (content.trim().equals("")) {
            view.showError("内容不能为空");
            return;
        }
        final FriendsContent fc = new FriendsContent();
        fc.setContent(content);
        fc.setAuthor(author);
        fc.setDate(new BmobDate(new Date()));

        view.postUnClickable("正在发布");
        List<String> picPath = new ArrayList<>();
        if (picPath1 != null) picPath.add(picPath1);
        if (picPath2 != null) picPath.add(picPath2);
        if (picPath3 != null) picPath.add(picPath3);
        final int size = picPath.size();
        if (size == 0) {
            fc.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    RxBus.getInstance().send(new RxBus.RxEvent().desc("update friendsContent"));
                    view.dismiss();
                }
            });
        } else {
            String[] picPaths = new String[size];
            BmobFile.uploadBatch(picPath.toArray(picPaths), new UploadBatchListener() {
                @Override
                public void onSuccess(List<BmobFile> list, List<String> list1) {
                    if (list.size() == picPath.size()) {//last time
                        if (list.size() >= 1) fc.setPic1(list.get(0));
                        if (list.size() >= 2) fc.setPic2(list.get(1));
                        if (list.size() >= 3) fc.setPic3(list.get(2));
                        fc.save(new SaveListener<String>() {
                            @Override
                            public void done(String s, BmobException e) {
                                RxBus.getInstance().send(new RxBus.RxEvent().desc("update friendsContent"));
                                view.dismiss();
                            }
                        });
                    }
                }

                @Override
                public void onProgress(int i, int i1, int i2, int i3) {
                }

                @Override
                public void onError(int i, String s) {
                    view.showError("发布失败");
                    view.postClickable();
                    Log.e("zy", "friends post presenter error:" + s);
                }
            });
        }
    }

    public void chosePicture() {
        final int REQUEST_CODE_GALLERY = 100;
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
                .setMutiSelectMaxSize(3)
                .build();
        GalleryFinal.init(core);
        GalleryFinal.openGalleryMuti(REQUEST_CODE_GALLERY, config, new GalleryFinal.OnHanlderResultCallback() {
            @Override
            public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                if (reqeustCode != REQUEST_CODE_GALLERY) return;
                for (int i = 0; i < resultList.size(); i++) {
                    PhotoInfo info = resultList.get(i);
                    Log.i("zy", info.getPhotoPath());
                    long currentTime = new Date().getTime();

                    if (i == 0) {
                        picPath1 = FileUtil.getDiskFilePath(view.getContext(), Environment.DIRECTORY_PICTURES, i + "friends" + currentTime + ".jpg");
                        Bitmap bitmap = BitmapUtil.compress(info.getPhotoPath(), picPath1,
                                true, true, 512 * 1024, 600, 600);
                        view.setPic1(bitmap);
                    } else if (i == 1) {
                        picPath2 = FileUtil.getDiskFilePath(view.getContext(), Environment.DIRECTORY_PICTURES, i + "friends" + currentTime + ".jpg");
                        Bitmap bitmap = BitmapUtil.compress(info.getPhotoPath(), picPath2,
                                true, true, 512 * 1024, 600, 600);
                        view.setPic2(bitmap);
                    } else if (i == 2) {
                        picPath3 = FileUtil.getDiskFilePath(view.getContext(), Environment.DIRECTORY_PICTURES, i + "friends" + currentTime + ".jpg");
                        Bitmap bitmap = BitmapUtil.compress(info.getPhotoPath(), picPath3,
                                true, true, 512 * 1024, 600, 600);
                        view.setPic3(bitmap);
                    }
                }
            }

            @Override
            public void onHanlderFailure(int requestCode, String errorMsg) {
                Log.e("zy", "photo error: " + errorMsg);
                view.showError("照片选择失败");
            }
        });
    }
}