package zy.chasegoddness.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.NinePatchDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016/3/2.
 */
public class CircleImageView extends ImageView {
    public final static String TAG = "zy";

    public CircleImageView(Context context) {
        super(context);
    }

    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CircleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() != null) {
            if (!(getDrawable() instanceof NinePatchDrawable)) {
                Bitmap bitmap = ((BitmapDrawable) getDrawable()).getBitmap();
                if (bitmap != null) {
                    Matrix matrix = new Matrix();
                    matrix.setScale(getWidth() * 1f / bitmap.getWidth(), getHeight() * 1f / bitmap.getHeight());

                    int offset;
                    if (bitmap.getWidth() > bitmap.getHeight()) {//横图
                        offset = (bitmap.getWidth() - bitmap.getHeight()) / 2;
                        matrix.postTranslate(-offset, 0);
                    } else {
                        offset = (bitmap.getHeight() - bitmap.getWidth()) / 2;
                        matrix.postTranslate(0, -offset);
                    }

                    Paint paint = new Paint();
                    int layer = canvas.saveLayer(0, 0, getWidth(), getHeight(), paint, Canvas.ALL_SAVE_FLAG);

                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
                    canvas.drawBitmap(bitmap, 0, 0, paint);

                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
                    Bitmap mask = getMaskBitmap(getWidth(), getHeight());
                    canvas.drawBitmap(mask, 0, 0, paint);

                    canvas.restoreToCount(layer);
                }
            }
        }
    }

    protected Bitmap getMaskBitmap(int width, int height) {
        Bitmap mask = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(mask);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawCircle(width / 2f, width / 2f, width / 2f, paint);

        return mask;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
        setMeasuredDimension(width, width);
    }
}
