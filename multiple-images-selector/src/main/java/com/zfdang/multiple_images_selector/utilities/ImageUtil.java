package com.zfdang.multiple_images_selector.utilities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;


public class ImageUtil {

	/***
	 * @Title: readPictureDegree
	 * @Description: 获得图片旋转角度
	 * @param @param path
	 * @param @return
	 * @return int 该图片的旋转角度
	 * @throws
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/**
	 * @Title: rotaingImageView
	 * @Description: 将图片旋转为正的角度
	 * @param @param angle 要旋转的角度
	 * @param @param bitmap 要旋转的Bitmap对象
	 * @param @return
	 * @return Bitmap 旋转正的图片
	 * @throws
	 */
	public static Bitmap rotaingBitmap(int angle, Bitmap bitmap) {
		if (angle != 0) {
			// 旋转图片 动作
			Matrix matrix = new Matrix();
			matrix.postRotate(angle);
			// 创建新的图片
			Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			bitmap.recycle();
			return resizedBitmap;
		} else {
			return bitmap;
		}
	}

	/****
	 * 
	 * @Title: getBitmap
	 * @Description: 根据路径得到Bitmap原图�?
	 * @param @param path
	 * @param @return
	 * @return Bitmap
	 * @throws
	 */
	public static Bitmap getBitmap(String path) {
		return BitmapFactory.decodeFile(path);
	}

	// 计算图片的缩放值
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	// 根据路径获得图片并压缩，返回bitmap用于显示,这里采用的是按比例进行压缩，压缩的图片在300K左右,而且不失真
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 1280, 800);
		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filePath, options);
	}

	// 这里是根据质量压缩.可以吧图片压缩到100K以下,然后保存到本地文件
	public static void compressAndSave(Bitmap image, String imageName) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		System.out.println(baos.toByteArray().length + "");
		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 10;// 每次都减少10
		}
		FileUtils.savaBitmap(baos, imageName);
		image.recycle();
		image = null;
		// 在这里已经压缩到100以下了，但是只要调用了decodeStream就又会涨到200K,所以在操作之前把图片先保存到本地
		// 如果需要返回一个bitmap对象，则调用如下方法
		// ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		// 把压缩后的数据baos存放到ByteArrayInputStream中
		// Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
		// 把ByteArrayInputStream数据生成图片
	}

}
