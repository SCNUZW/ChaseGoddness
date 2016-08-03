package zy.chasegoddness.model;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpParams;

import org.json.JSONArray;
import org.json.JSONException;

import rx.Observable;
import rx.schedulers.Schedulers;
import zy.chasegoddness.util.Constant;

/**
 * Created by Administrator on 2016/8/2.
 */
public class EmotionAnalyzeModel {
    public static Observable<String> getClassfy(String content) {
        final JSONArray array = new JSONArray().put(content);
        final HttpParams params = new HttpParams();
        params.putHeaders("Content-Type", "application/json");
        params.putHeaders("Accept", "application/json");
        params.putHeaders("Accept-Encoding", "UTF-8");
        params.putHeaders("X-Token", "RWM6nX_5.4477.g2RsJn20QJZj");
        params.putJsonParams(array.toString());

        return new RxVolley.Builder()
                .httpMethod(RxVolley.Method.POST)
                .params(params)
                .contentType(RxVolley.ContentType.JSON)
                .url(Constant.URL.classify)
                .getResult()
                .subscribeOn(Schedulers.io())
                .map(result -> {
                    String data = new String(result.data);
                    try {
                        JSONArray number = new JSONArray(data);
                        int num = number.getInt(0);
                        switch (num) {
                            case 0:
                                return "体育";
                            case 1:
                                return "教育";
                            case 2:
                                return "财经";
                            case 3:
                                return "社会";
                            case 4:
                                return "娱乐";
                            case 5:
                                return "军事";
                            case 6:
                                return "国内";
                            case 7:
                                return "科技";
                            case 8:
                                return "互联网";
                            case 9:
                                return "房产";
                            case 10:
                                return "国际";
                            case 11:
                                return "女人";
                            case 12:
                                return "汽车";
                            case 13:
                                return "游戏";
                            default:
                                break;
                        }
                    } catch (JSONException e) {
                    }
                    return "未知";
                });
    }

    public static Observable<Double[]> getSentiment(String content) {
        final JSONArray array = new JSONArray().put(content);
        final HttpParams params = new HttpParams();
        params.putHeaders("Content-Type", "application/json");
        params.putHeaders("Accept", "application/json");
        params.putHeaders("X-Token", Constant.BosonNLPToken);
        params.putJsonParams(array.toString());

        return new RxVolley.Builder()
                .httpMethod(RxVolley.Method.POST)
                .params(params)
                .url(Constant.URL.sentiment)
                .contentType(RxVolley.ContentType.JSON)
                .getResult()
                .subscribeOn(Schedulers.io())
                .map(result -> {
                    Double[] data = new Double[2];
                    String json = new String(result.data);
                    try {
                        JSONArray number = new JSONArray(json).getJSONArray(0);
                        data[0] = number.getDouble(0);
                        data[1] = number.getDouble(1);
                    } catch (JSONException e) {
                        data[0] = data[1] = 0d;
                    }
                    return data;
                });
    }
}
