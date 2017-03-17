package zy.chasegoddness.model;

import android.util.Log;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

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
        String url = "http://www.tuling123.com/openapi/api?key=cc434a1cf69aba2fa1593abdd1038969&info="+content;
        return new RxVolley.Builder()
                .url(url)
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

    public Observable<String> getReply3(String content){
        try {
            content = URLEncoder.encode(content,"utf-8");
        } catch (UnsupportedEncodingException e) {
        }
        String url = "http://api.smartnlp.cn/cloud/robot/58cbc6910e00005371a011c7/answer?q="+content;
        return new RxVolley.Builder()
                .url(url)
                .getResult()
                .subscribeOn(Schedulers.io())
                .map(result -> {
                    String json = new String(result.data);
                    Log.i("zy",json);
                    JSONObject object = new JSONObject();
                    try {
                        object = new JSONObject(json);
                    } catch (JSONException e) {
                    }
                    try {
                        object = object.optJSONArray("answers").getJSONObject(0);
                    } catch (JSONException e) {
                    }
                    return object.optString("respond");
                });
    }

    /**
     * 获取多条自动回复内容
     */
    public Observable<String> getReply(String content){
        Observable<String> reply1 = getReply1(content);
        Observable<String> reply2 = getReply2(content);
        Observable<String> reply3 = getReply3(content);
        return Observable.mergeDelayError(reply1, reply2, reply3);
    }
}
