package com.ecjtu.whack_a_mole.bean;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.support.annotation.Nullable;
import android.support.v7.widget.ViewUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ecjtu.whack_a_mole.activity.R;
import org.xutils.view.annotation.ViewInject;

/**
 * @author ConstQ on 2017/1/7.
 * @version 1.0
 */
public class IconTextView extends LinearLayout{

    private TextView mText;
    private ImageView mIcon;

    public IconTextView(Context context,AttributeSet attrs) {
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.view_icontext,this);
        mText = (TextView) findViewById(R.id.tv_icontext_title);
        mIcon = (ImageView) findViewById(R.id.img_icontext_ds);
    }

    public void setIconText(IconText iconText){
        mText.setTextColor(Color.CYAN);
        mText.setText(iconText.getText());
        mIcon.setImageDrawable(iconText.getIcon());
    }

    public String getText(){
        return (String) mText.getText();
    }
    public void changeIcon(Drawable icon){
        mIcon.setImageDrawable(icon);
    }
}
