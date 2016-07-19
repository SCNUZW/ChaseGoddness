package zy.chasegoddness.model;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import rx.Observable;
import rx.schedulers.Schedulers;
import zy.chasegoddness.model.bean.FriendsContent;

/**
 * Created by Administrator on 2016/7/19.
 */
public class FriendsModel {
    public static Observable<List<FriendsContent>> getFriendsContent(int pageNum, int pageSize) {
        if (pageNum <= 0 || pageSize <= 0) {
            throw new IllegalArgumentException("pageNum or pageSize should bigger than 0");
        }

        BmobQuery<FriendsContent> query = new BmobQuery<>();
        query.order("-date");
        query.include("author");
        query.setLimit(pageSize);
        query.setSkip((pageNum - 1) * pageSize);
        query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);
        return query.findObjectsObservable(FriendsContent.class)
                .subscribeOn(Schedulers.io());
    }


}
