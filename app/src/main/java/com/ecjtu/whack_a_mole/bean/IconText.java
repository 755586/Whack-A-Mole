package com.ecjtu.whack_a_mole.bean;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * @author ConstQ on 2017/1/7.
 * @version 1.0
 */
public class IconText implements Comparable<IconText> {
    private String text = "";
    private Drawable icon;
    private boolean selectable = true;

    public IconText(String text, Drawable icon) {
        this.text = text;
        this.icon = icon;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
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
           return this.text.compareTo(iconText.getText());
       }else{
           throw new IllegalArgumentException();
       }
    }
}
