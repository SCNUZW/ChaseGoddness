package zy.chasegoddness.model;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpParams;

import org.json.JSONException;
import org.json.JSONObject;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * @author wsd
 *         自动回复功能
 */
public class AutoReplyModel {
    public AutoReplyModel() {
    }

    public Observable<String> getReply1(String content) {
        JSONObject jsonOj = new JSONObject();
        try {
            jsonOj.put("key", "cc434a1cf69aba2fa1593abdd1038969");
            jsonOj.put("info", content);
        } catch (JSONException e) {
        }
        HttpParams params = new HttpParams();
        params.putJsonParams(jsonOj.toString());
        return new RxVolley.Builder()
                .url("http://www.tuling123.com/openapi/api")
                .params(params)
                .getResult()
                .subscribeOn(Schedulers.io())
                .map(result -> {
                    String json = new String(result.data);
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject = new JSONObject(json);
                    } catch (JSONException e) {
                    }
                    return jsonObject.optString("text");
                });
    }

    public Observable<String> getReply2(String content) {
        String url = "http://api.qingyunke.com/api.php?key=free&appid=0&msg=" + content;
        return new RxVolley.Builder()
                .url(url)
                .getResult()
                .subscribeOn(Schedulers.io())
                .map(result -> {
                    String json = new String(result.data);
                    JSONObject object = new JSONObject();
                    try {
                        object = new JSONObject(json);
                    } catch (JSONException e) {
                    }
                    return object.optString("content");
                });
    }

    public Observable<String> getReply3(String content) {
        String url = "http://apis.haoservice.com/efficient/robot?info="+content+"&key=ea3456f2146345deb92a89fb67b7fd32";
        return new RxVolley.Builder()
                .url(url)
                .getResult()
                .subscribeOn(Schedulers.io())
                .map(result -> {
                    String json = new String(result.data);
                    JSONObject object = new JSONObject();
                    try {
                        object = new JSONObject(json);
                    } catch (JSONException e) {
                    }
                    object = object.optJSONObject("result");
                    return object == null ? "" : object.optString("text");
                });
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
