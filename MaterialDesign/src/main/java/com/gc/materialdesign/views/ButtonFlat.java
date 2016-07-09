package com.gc.materialdesign.views;

import com.gc.materialdesign.R;
import com.gc.materialdesign.utils.Utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ButtonFlat extends Button {

    TextView textButton;

    public ButtonFlat(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    protected void setDefaultProperties() {
        minHeight = 32;
        minWidth = 48;
        rippleSize = 3;
        // Min size
        setMinimumHeight(Utils.dpToPx(minHeight, getResources()));
        setMinimumWidth(Utils.dpToPx(minWidth, getResources()));
        setBackgroundResource(R.drawable.background_transparent);
    }

    @Override
    protected void setAttributes(AttributeSet attrs) {
        // Set text button
        String text = null;
        float textSize = 18;
        int textResource = attrs.getAttributeResourceValue(ANDROIDXML, "text", -1);
        if (textResource != -1) {
            text = getResources().getString(textResource);
        } else {
            text = attrs.getAttributeValue(ANDROIDXML, "text");
        }

        String size = null;
        int sizeNum = attrs.getAttributeResourceValue(ANDROIDXML, "textSize", -1);
        if (sizeNum != -1) {
            size = getResources().getString(sizeNum);
        } else {
            size = attrs.getAttributeValue(ANDROIDXML, "textSize");
        }
        if (size != null) {
            size = size.substring(0, size.length() - 2);
            textSize = Float.valueOf(size);
        }
        int dp8 = Utils.dpToPx(8, getResources());

        if (text != null) {
            textButton = new TextView(getContext());
            textButton.setText(text.toUpperCase());
            textButton.setTextColor(backgroundColor);
            textButton.setTypeface(null, Typeface.BOLD);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            textButton.setPadding(dp8, dp8, dp8, dp8);
            textButton.setLayoutParams(params);
            textButton.setTextSize(textSize);
            addView(textButton);
        }
        int bacgroundColor = attrs.getAttributeResourceValue(ANDROIDXML, "background", -1);
        if (bacgroundColor != -1) {
            setBackgroundColor(getResources().getColor(bacgroundColor));
        } else {
            // Color by hexadecimal
            // Color by hexadecimal
            background = attrs.getAttributeIntValue(ANDROIDXML, "background", -1);
            if (background != -1)
                setBackgroundColor(background);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (x != -1) {

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(makePressColor());
            canvas.drawCircle(x, y, radius, paint);
            if (radius > getHeight() / rippleSize)
                radius += rippleSpeed;
            if (radius >= getWidth()) {
                x = -1;
                y = -1;
                radius = getHeight() / rippleSize;
                if (onClickListener != null && clickAfterRipple)
                    onClickListener.onClick(this);
            }
            invalidate();
        }

    }

    /**
     * Make a dark color to ripple effect
     *
     * @return
     */
    @Override
    protected int makePressColor() {
        return Color.parseColor("#88DDDDDD");
    }

    public void setText(String text) {
        textButton.setText(text.toUpperCase());
    }

    // Set color of background
    public void setBackgroundColor(int color) {
        backgroundColor = color;
        if (isEnabled())
            beforeBackground = backgroundColor;
        textButton.setTextColor(color);
    }

    @Override
    public TextView getTextView() {
        return textButton;
    }

    public String getText() {
        return textButton.getText().toString();
    }

}
