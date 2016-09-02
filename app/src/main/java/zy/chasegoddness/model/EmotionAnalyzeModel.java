package zy.chasegoddness.model;

import android.content.Context;
import android.util.Log;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpParams;
import com.kymjs.rxvolley.rx.Result;

import org.json.JSONArray;
import org.json.JSONException;

import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import zy.chasegoddness.util.Constant;

/**
 * @author zy
 *         情感分析的相关功能
 */
public class EmotionAnalyzeModel {
    /**
     * 对content的类别进行分类
     */
    public static Observable<String> getClassfy(String content) {
        final JSONArray array = new JSONArray().put(content);
        final HttpParams params = new HttpParams();
        params.putHeaders("Content-Type", "application/json");
        params.putHeaders("Accept", "application/json");
        params.putHeaders("Accept-Encoding", "UTF-8");
        params.putHeaders("X-Token", Constant.BosonNLPToken);
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

    /**
     * 对content内容进行正负面百分比分析
     */
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

    /**
     * 关键词提取
     */
    public static Observable<String[]> getKeywordArray(String content) {
        final JSONArray array = new JSONArray().put(content);
        final HttpParams params = new HttpParams();
        params.putHeaders("Content-Type", "application/json");
        params.putHeaders("Accept", "application/json");
        params.putHeaders("Accept-Encoding", "UTF-8");
        params.putHeaders("X-Token", Constant.BosonNLPToken);
        params.putJsonParams(array.toString());
        return new RxVolley.Builder()
                .httpMethod(RxVolley.Method.POST)
                .params(params)
                .url(Constant.URL.keyword)
                .contentType(RxVolley.ContentType.JSON)
                .getResult()
                .subscribeOn(Schedulers.io())
                .map(result -> {
                    try {
                        String json = new String(result.data);
                        JSONArray tmp = new JSONArray(json).getJSONArray(0);
                        String[] strings = new String[tmp.length()];
                        for (int i = 0; i < tmp.length(); i++) {
                            String keyword = tmp.getJSONArray(i).getString(1);
                            strings[i] = keyword;
                        }
                        return strings;
                    } catch (JSONException e) {
                        Log.e("zy", "EmotionAnalyzeModel getKeywordArray error: " + e);
                    }
                    return null;
                });
    }

    public static Observable<String> getKeywords(String content) {
        return getKeywordArray(content)
                .flatMap(strings -> Observable.from(strings));
    }

    /**
     * 语义联想
     */
    public static Observable<String[]> getSuggest(String keyword) {
        final JSONArray array = new JSONArray().put(keyword);
        final HttpParams params = new HttpParams();
        params.putHeaders("Content-Type", "application/json");
        params.putHeaders("Accept", "application/json");
        params.putHeaders("X-Token", Constant.BosonNLPToken);
        params.putJsonParams(array.toString());

        return new RxVolley.Builder()
                .httpMethod(RxVolley.Method.POST)
                .params(params)
                .url(Constant.URL.suggest)
                .contentType(RxVolley.ContentType.JSON)
                .getResult()
                .subscribeOn(Schedulers.io())
                .map((Func1<Result, String[]>) result -> {
                    Log.i("zy", new String(result.data));
                    return null;
                });
    }

    /**
     * 语义联想
     */
    public static Observable<String[]> getSuggests(String[] keyword) {
        final JSONArray array = new JSONArray();
        for (String s : keyword)
            array.put(s);
        final HttpParams params = new HttpParams();
        params.putHeaders("Content-Type", "application/json");
        params.putHeaders("Accept", "application/json");
        params.putHeaders("X-Token", Constant.BosonNLPToken);
        params.putJsonParams(array.toString());

        return new RxVolley.Builder()
                .httpMethod(RxVolley.Method.POST)
                .params(params)
                .url(Constant.URL.suggest)
                .contentType(RxVolley.ContentType.JSON)
                .getResult()
                .subscribeOn(Schedulers.io())
                .map((Func1<Result, String[]>) result -> {
                    Log.i("zy", new String(result.data));
                    return null;
                });
    }
}
