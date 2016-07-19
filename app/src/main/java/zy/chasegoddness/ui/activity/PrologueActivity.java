package zy.chasegoddness.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import zy.chasegoddness.R;

public class PrologueActivity extends BaseActivity {

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prologue);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }

    public static void startActivityForResult(Context context, int requestCode) {
        ((Activity) context).startActivityForResult(new Intent(context, PrologueActivity.class), requestCode);
    }
}
