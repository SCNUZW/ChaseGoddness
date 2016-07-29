package zy.chasegoddness.util;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2016/7/27.
 */
public class FileUtil {

    private FileUtil() {
    }

    public static File getDiskCacheDir(Context context, String uniqueName) {
        return new File(getDiskCacheDirPath(context, uniqueName));
    }

    public static String getDiskCacheDirPath(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath + File.separator + uniqueName;
    }

    public static String getDiskDirPath(Context context, String type) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalFilesDir(type).getPath();
        } else {
            cachePath = context.getFilesDir().getPath();
        }
        return cachePath;
    }

    public static File getDiskFile(Context context, String type, String fileName) {
        String path = getDiskDirPath(context, type);
        return new File(path, fileName);
    }

    public static String getDiskFilePath(Context context, String type, String fileName) {
        return getDiskFile(context, type, fileName).getAbsolutePath();
    }
}
