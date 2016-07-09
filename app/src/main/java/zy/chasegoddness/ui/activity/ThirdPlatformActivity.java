package zy.chasegoddness.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import zy.chasegoddness.R;

public class ThirdPlatformActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_platform);
    }

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, ThirdPlatformActivity.class));
    }
}
