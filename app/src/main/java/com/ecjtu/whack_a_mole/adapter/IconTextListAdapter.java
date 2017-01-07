package com.ecjtu.whack_a_mole.adapter;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.ecjtu.whack_a_mole.bean.IconText;
import com.ecjtu.whack_a_mole.bean.IconTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ConstQ on 2017/1/7.
 * @version 1.0
 */
public class IconTextListAdapter extends BaseAdapter{
    private Context mContext;
    private List<IconText> mItems = new ArrayList<>();

    public IconTextListAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void addItem(IconText item){
        mItems.add(item);
    }

    public boolean isSelectable(int i){
        return mItems.get(i).isSelectable();
    }

    public void setListItems(List<IconText> items){
        mItems = items;
    }



    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        IconTextView view;
        if(convertView ==null){
            view = new IconTextView(mContext,mItems.get(i));
        }else{
            view = (IconTextView) convertView;
            view.setmText(mItems.get(i).getmText());
            view.setmIcon(mItems.get(i).getmIcon());
        }
        return view;
    }
}
