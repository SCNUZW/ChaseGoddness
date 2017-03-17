package zy.chasegoddness.presenter;

import android.util.Log;

import java.util.Date;

import rx.android.schedulers.AndroidSchedulers;
import zy.chasegoddness.global.LocalDB;
import zy.chasegoddness.model.EvaluationModel;
import zy.chasegoddness.ui.activity.EvaluationActivity;

/**
 * Created by Administrator on 2016/11/8.
 */

public class EvaluationPresenter {
    private EvaluationActivity view;

    public EvaluationPresenter(EvaluationActivity view) {
        this.view = view;
    }

    public void init() {
        final long time = new Date().getTime();
        final int favorability = new LocalDB(view.getContext()).getFavorability();
        EvaluationModel.getEvaluation(time, favorability)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(evaluation -> {
                    String content = evaluation.getContent();
                    String title = evaluation.getTitle();
                    view.setTitle(title);
                    view.setContent("\t\t\t\t" + content);
                }, throwable -> {
                    view.showToast("加载请求失败！");
                    Log.e("zy", "Evaluation presenter init get evaluation fail:" + throwable.toString());
                });
    }
}
