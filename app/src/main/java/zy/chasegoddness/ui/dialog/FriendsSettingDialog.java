package zy.chasegoddness.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.gc.materialdesign.views.ButtonFlat;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import zy.chasegoddness.R;
import zy.chasegoddness.global.RxBus;
import zy.chasegoddness.model.FriendsLoginModel;
import zy.chasegoddness.model.bean.User;

/**
 * Created by Administrator on 2016/8/9.
 */
public class FriendsSettingDialog extends DialogFragment {

    private EditText et_name, et_desc;
    private ButtonFlat btn_ok;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_friends_setting, container, false);
        initView(view);
        return view;
    }

    private void initView(View v) {
        btn_ok = (ButtonFlat) v.findViewById(R.id.btn_friends_setting_ok);
        et_name = (EditText) v.findViewById(R.id.et_friends_setting_name);
        et_desc = (EditText) v.findViewById(R.id.et_friends_setting_desc);

        btn_ok.setOnClickListener(view -> {
            String name = et_name.getText().toString();
            String desc = et_desc.getText().toString();
            User user = FriendsLoginModel.getCurrentUser();
            user.setDesc(desc);
            user.setNickName(name);
            user.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    RxBus.getInstance().send(new RxBus.RxEvent().desc("finish update user"));
                }
            });
            dismiss();
        });

        User user = FriendsLoginModel.getCurrentUser();
        if (user != null) {
            et_name.setText(user.getNickName());
            et_desc.setText(user.getDesc());
        }
    }

    public static FriendsSettingDialog showDialog(FragmentManager manager) {
        FriendsSettingDialog dialog = new FriendsSettingDialog();
        dialog.show(manager, "FriendsSettingDialog");
        return dialog;
    }
}
