package com.zfdang.multiple_images_selector;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.zfdang.multiple_images_selector.utilities.FileUtils;
import com.zfdang.multiple_images_selector.utilities.ImageUtil;
import com.zfdang.multiple_images_selector.view.ClipImageLayout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CropActivity extends Activity implements OnClickListener {
	private Button btn;
	private ClipImageLayout mClipImageLayout;
	List<String> mResults = new ArrayList<>();
	private String path;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crop);
//		path = getIntent().getStringExtra(SelectorSettings.SELECTOR_RESULTS);

		/**
		 * 设置状态栏颜色
		 */
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
		View statusBarView = new View(window.getContext());
		int statusBarHeight = getStatusBarHeight(window.getContext());
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
		params.gravity = Gravity.TOP;
		statusBarView.setLayoutParams(params);
		statusBarView.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
		decorViewGroup.addView(statusBarView);

		mResults = getIntent().getStringArrayListExtra(SelectorSettings.SELECTOR_RESULTS);
		assert mResults != null;
		StringBuilder sb = new StringBuilder();
		for (String result : mResults) {
			sb.append(result);
		}
		path = sb.toString();


		btn = (Button) findViewById(R.id.crop_button);
		btn.setOnClickListener(CropActivity.this);
		mClipImageLayout = (ClipImageLayout) findViewById(R.id.clipImageLayout);

		// 有的系统返回的图片是旋转了，有的没有旋转，所以处理
		int degreee = ImageUtil.readPictureDegree(path);
		Bitmap bitmap = ImageUtil.getSmallBitmap(path);
		if (bitmap != null) {
			if (degreee == 0) {
				mClipImageLayout.setImageBitmap(bitmap);
			} else {
				mClipImageLayout.setImageBitmap(ImageUtil.rotaingBitmap(degreee, bitmap));
			}
		}

	}

	@Override
	public void onClick(View v) {
		//得到裁剪的图片
		Bitmap bitmap = mClipImageLayout.clip();
		String imagePath = FileUtils.getPathAvatar() + "/avatar.jpg";
		//这里把裁剪过后的图片进行质量压缩，并且保存到本地

		ImageUtil.compressAndSave(bitmap, "avatar");
		Intent intent = new Intent();
		intent.putExtra("imageName", imagePath);
		setResult(Activity.RESULT_OK, intent);
		finish();
	}


	/**
	 * 获取状态栏高度
	 *
	 * @param context
	 * @return
	 */
	private static int getStatusBarHeight(Context context) {
		int statusBarHeight = 0;
		Resources res = context.getResources();
		int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			statusBarHeight = res.getDimensionPixelSize(resourceId);
		}
		return statusBarHeight;
	}
}
