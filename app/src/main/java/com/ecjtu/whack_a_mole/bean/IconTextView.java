package com.ecjtu.whack_a_mole.bean;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author ConstQ on 2017/1/7.
 * @version 1.0
 */
public class IconTextView extends LinearLayout{
    private TextView mText;
    private ImageView mIcon;


    public IconTextView(Context context,IconText iconText) {
        super(context);
        this.setOrientation(HORIZONTAL);
        this.setGravity(Gravity.CENTER_VERTICAL);

        mIcon  = new ImageView(context);
        mIcon.setImageDrawable(iconText.getmIcon());
        mIcon.setPadding(0,2,5,2);
        addView(mIcon,new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));

        mText = new TextView(context);
        mText.setText(iconText.getmText());
        mText.setTextSize(20);
        mText.setPadding(5,2,2,0);
        addView(mText,new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));

    }

    public TextView getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText.setText(mText);
    }

    public ImageView getmIcon() {
        return mIcon;
    }

    public void setmIcon(Drawable mIcon) {
        this.mIcon.setImageDrawable(mIcon);
    }
}
