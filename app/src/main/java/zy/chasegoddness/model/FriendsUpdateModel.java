package zy.chasegoddness.model;

import android.util.Log;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import rx.Observable;
import rx.Subscriber;
import zy.chasegoddness.global.RxBus;
import zy.chasegoddness.model.bean.User;

/**
 * Created by Administrator on 2016/8/9.
 */
public class FriendsUpdateModel {
    private FriendsUpdateModel() {
    }

    public static Observable<Void> updateNickName(String nickName) {
        User user = FriendsLoginModel.getCurrentUser();
        if (user != null) {
            user.setNickName(nickName);
            return user.updateObservable();
        }

        return error("尚未登陆");
    }

    public static Observable<Void> updateDesc(String desc) {
        User user = FriendsLoginModel.getCurrentUser();
        if (user != null) {
            user.setDesc(desc);
            return user.updateObservable();
        }

        return error("尚未登陆");
    }

    public static void updateAvatar(String picPath) {
        User user = FriendsLoginModel.getCurrentUser();
        if (user != null) {
            BmobFile file = new BmobFile(new File(picPath));
            file.upload(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        user.setAvatar(file);
                        user.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                RxBus.getInstance().send(new RxBus.RxEvent().desc("finish update user"));
                            }
                        });
                    }
                }
            });
        }
    }

    private static Observable<Void> error(String error) {
        return Observable.create(subscriber -> {
            throw new RuntimeException(error);
        });
    }
}
