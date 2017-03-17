package zy.chasegoddness.model;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.v3.BmobQuery;
import rx.Observable;
import rx.schedulers.Schedulers;
import zy.chasegoddness.model.bean.Evaluation;

public class EvaluationModel {
    private EvaluationModel() {
    }

    public static Observable<Integer> getCountOfEvaluation(int favorability) {
        favorability = normalization(favorability);
        BmobQuery<Evaluation> query = new BmobQuery<>();
        query.addWhereEqualTo("favorability", favorability);
        return query.countObservable(Evaluation.class)
                .subscribeOn(Schedulers.io());
    }

    public static Observable<Evaluation> getEvaluation(long time, int favorability) {
        return getCountOfEvaluation(favorability)
                .flatMap(size -> {
                    Log.i("zy", "evaluation size = " + size);
                    BmobQuery<Evaluation> query = new BmobQuery<>();
                    int favor = normalization(favorability);
                    int hash = hashCodeOfDate(time);
                    int id = Math.abs(hash) % size;
                    query.addWhereEqualTo("favorability", favor);
                    query.addWhereEqualTo("id", id);
                    return query.findObjectsObservable(Evaluation.class);
                }).flatMap(evaluations -> {
                    for (Evaluation e : evaluations) {
                        Log.i("zy", e.getTitle() + e.getContent());
                    }
                    return Observable.from(evaluations);
                })
                .take(1);
    }

    /**
     * 确保同一天的日期会生成同一个hash值，
     * 而不同日期会生成不同的hash值
     */
    private static int hashCodeOfDate(long time) {
        Date date = new Date(time);
        String dateFormate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        return dateFormate.hashCode();
    }

    /**
     * 把好感度数值标准化到特定的几个阀值上
     */
    private static int normalization(int number) {
        return number / 10 * 10;
    }
}