package zy.chasegoddness.ui.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;

import org.json.JSONArray;

import java.util.Map;

import zy.chasegoddness.R;
import zy.chasegoddness.model.EmotionAnalyzeModel;

public class EmotionAnalyzeDialog extends DialogFragment {

    private TextView tv_analyze;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_emotion_analyze, container, false);
        initView(view);
        return view;
    }

    private void initView(View v) {
        tv_analyze = (TextView) v.findViewById(R.id.tv_emotion_analyze);

        if (getArguments() != null) {
            final String content = getArguments().getString("content");
            if (content != null) {
                EmotionAnalyzeModel.getClassfy(content);
                EmotionAnalyzeModel.getSentiment(content);
            }
        }
    }

    public static EmotionAnalyzeDialog showDialog(FragmentManager manager, String content) {
        EmotionAnalyzeDialog dialog = new EmotionAnalyzeDialog();
        Bundle bundle = new Bundle();
        bundle.putString("content", content);
        dialog.setArguments(bundle);
        dialog.show(manager, "EmotionAnalyzeDialog");
        return dialog;
    }
}
