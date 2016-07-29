package zy.chasegoddness.ui.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;

import zy.chasegoddness.R;
import zy.chasegoddness.global.LocalDB;
import zy.chasegoddness.global.RxBus;
import zy.chasegoddness.model.FormatCheckModel;
import zy.chasegoddness.model.SetAccountModel;

public class SetAccountDialog extends DialogFragment {

    private TextView tv_wrongNumber;
    private EditText et_phone;
    private ButtonFlat btn_ok, btn_cancel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_set_account, container, false);
        initView(view);
        return view;
    }

    private final void initView(View v) {
        tv_wrongNumber = (TextView) v.findViewById(R.id.tv_wrongNumber);
        et_phone = (EditText) v.findViewById(R.id.et_goddness_phone);
        btn_ok = (ButtonFlat) v.findViewById(R.id.btn_goddness_ok);
        btn_cancel = (ButtonFlat) v.findViewById(R.id.btn_cancel);

        btn_ok.setOnClickListener(view -> {
            String phoneNum = et_phone.getText().toString();
            if (FormatCheckModel.isPhoneNumber(phoneNum)) {
                tv_wrongNumber.setVisibility(View.INVISIBLE);

                SetAccountModel.saveAccount(getContext(), phoneNum);
                RxBus.getInstance().send(new RxBus.RxEvent().desc("update ChatActivity"));
                dismiss();
            } else {
                tv_wrongNumber.setVisibility(View.VISIBLE);
            }
        });

        btn_cancel.setOnClickListener(v1 -> dismiss());
    }
}
