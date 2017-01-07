package com.ecjtu.whack_a_mole.activity;

import android.app.Activity;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.ecjtu.whack_a_mole.bean.IconText;
import com.ecjtu.whack_a_mole.bean.IconTextView;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_game)
public class GameActivity extends BaseActivity {
    @ViewInject(R.id.itv_game_test)
    private IconTextView itv_game_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initData();
    }

    private void initData() {
        itv_game_test.setIconText(new IconText("apple",getResources().getDrawable(R.mipmap.ds4)));
        itv_game_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((IconTextView) view).changeIcon(getResources().getDrawable(R.mipmap.ds3));
                toast(((IconTextView) view).getText());
            }
        });
    }
}
