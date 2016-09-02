package zy.chasegoddness.model;

import rx.Observable;
import rx.schedulers.Schedulers;
import zy.chasegoddness.model.bean.FeedBack;

public class FeedBackModel {
    public static Observable<String> uploadFeedBack(String content, String QQ, String phoneNum) {
        FeedBack fb = new FeedBack();
        fb.setContent(content);
        fb.setPhone(phoneNum);
        fb.setQQ(QQ);
        return fb.saveObservable()
                .subscribeOn(Schedulers.io());
    }
}
