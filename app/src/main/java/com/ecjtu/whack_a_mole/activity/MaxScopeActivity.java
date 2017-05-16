package com.ecjtu.whack_a_mole.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_max_scope)
public class MaxScopeActivity extends Activity {
    @ViewInject(R.id.tv_scope)
    private TextView tv_scope;
    @ViewInject(R.id.tv_name)
    private TextView tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
    }

    private void initView() {
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                Activity.MODE_PRIVATE);
        int maxScope = sharedPreferences.getInt("maxScope", 0);
        String maxScopeName = sharedPreferences.getString("maxScopeName", "匿名");
        tv_name.setText(maxScopeName);
        tv_scope.setText(String.valueOf(maxScope));
    }
}
