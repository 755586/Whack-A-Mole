package com.ecjtu.whack_a_mole.activity;

import android.app.Activity;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.widget.ListView;
import com.ecjtu.whack_a_mole.adapter.IconTextListAdapter;
import com.ecjtu.whack_a_mole.bean.IconText;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_game)
public class GameActivity extends BaseActivity {

    @ViewInject(R.id.game_lv_it)
    private ListView game_lv_it;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initData();
    }

    private void initData() {
        List<IconText> items = new ArrayList<>();
        for(int i=0;i<9;i++){
            String text = String.valueOf(96+i);
            IconText item = new IconText(text,getResources().getDrawable(R.mipmap.ds3));
            items.add(item);
        }
        IconTextListAdapter adapter = new IconTextListAdapter(this);
        adapter.setListItems(items);
        game_lv_it.setAdapter(adapter);
    }
}
