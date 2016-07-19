package zy.chasegoddness.ui.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFlat;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import zy.chasegoddness.R;
import zy.chasegoddness.model.FriendsLoginModel;
import zy.chasegoddness.model.bean.User;
import zy.chasegoddness.ui.activity.FriendsRegisterActivity;

/**
 * 好友圈登陆界面
 */
public class FriendsLoginDialog extends DialogFragment {

    private EditText et_phone, et_pwd;
    private TextView tv_error;
    private ButtonFlat btn_login, btn_register;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_friends_login, container, false);

        et_phone = (EditText) view.findViewById(R.id.et_friends_phone);
        et_pwd = (EditText) view.findViewById(R.id.et_friends_pwd);
        tv_error = (TextView) view.findViewById(R.id.tv_friends_error);
        btn_login = (ButtonFlat) view.findViewById(R.id.btn_friends_login);
        btn_register = (ButtonFlat) view.findViewById(R.id.btn_friends_register);

        btn_login.setOnClickListener(v -> {
            hideError();

            final String phone = et_phone.getText().toString();
            final String pwd = et_pwd.getText().toString();

            User user = new User();
            user.setMobilePhoneNumber(phone);
            user.setUsername(phone);
            user.setPassword(pwd);
            FriendsLoginModel.login(user)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(u -> {/*登陆成功*/
                        dismiss();
                    }, throwable -> {/*登陆失败*/
                        Log.e("zy", "FriendsLoginDialog login error: " + throwable);
                        showError("登陆失败");
                    });
        });
        btn_register.setOnClickListener(v -> {
            dismiss();
            FriendsRegisterActivity.startActivity(getContext());
        });
        return view;
    }

    public void hideError() {
        if (tv_error != null) tv_error.setVisibility(View.INVISIBLE);
    }

    public void showError(String str) {
        if (tv_error != null) {
            tv_error.setVisibility(View.VISIBLE);
            tv_error.setText(str);
        }
    }
}
