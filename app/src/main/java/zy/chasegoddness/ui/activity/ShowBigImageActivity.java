package zy.chasegoddness.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import zy.chasegoddness.R;

public class ShowBigImageActivity extends BaseActivity {

    private ImageView iv_big_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_big_image);

        initView();
    }

    private void initView() {
        iv_big_image = (ImageView) findViewById(R.id.iv_big_image);

        final String url = getIntent().getStringExtra("url");
        if (url != null) {
            Glide.with(this).load(url)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(iv_big_image);
        }
    }

    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, ShowBigImageActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }
}
