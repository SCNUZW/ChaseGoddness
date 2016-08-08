package zy.chasegoddness.model;

import rx.Observable;

/**
 * @author wsd
 *         自动回复功能
 */
public class AutoReplyModel {
    private AutoReplyModel() {
    }

    public Observable<String> getReply1(String content) {
        //TODO: 获取自动回复
        return null;
    }

    public Observable<String> getReply2(String content) {
        //TODO: 获取自动回复
        return null;
    }

    public Observable<String> getReply3(String content) {
        //TODO: 获取自动回复
        return null;
    }

    /**
     * 获取多条自动回复内容
     */
    public Observable<String> getReply(String content) {
        Observable<String> reply1 = getReply1(content);
        Observable<String> reply2 = getReply2(content);
        Observable<String> reply3 = getReply3(content);
        return Observable.mergeDelayError(reply1, reply2, reply3);
    }
}
