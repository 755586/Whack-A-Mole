package com.ecjtu.whack_a_mole.bean;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * @author ConstQ on 2017/1/7.
 * @version 1.0
 */
public class IconText implements Comparable<IconText> {
    private String mText = "";
    private Drawable mIcon;
    private boolean selectable = true;
    public IconText(String mText, Drawable mIcon) {
        this.mText = mText;
        this.mIcon = mIcon;
    }

    public String getmText() {
        return mText;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }

    public Drawable getmIcon() {
        return mIcon;
    }

    public void setmIcon(Drawable mIcon) {
        this.mIcon = mIcon;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    @Override
    public int compareTo(@NonNull IconText iconText) {
       if(iconText!=null){
           return this.mText.compareTo(iconText.getmText());
       }else{
           throw new IllegalArgumentException();
       }
    }
}
