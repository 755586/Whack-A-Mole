package com.ecjtu.whack_a_mole.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewInject(R.id.tv_main_title)
    private TextView tv_main_title;

    @Event({R.id.btn_main_begin,R.id.btn_main_help,R.id.btn_main_exit})
    private void onClick(View v){
       switch (v.getId()){
           case R.id.btn_main_begin:{
               startActivity(new Intent(MainActivity.this,GameActivity.class));
               break;
           }
           case R.id.btn_main_help:{
                toast("游戏帮助");
                help();
                break;
           }
           case R.id.btn_main_exit:{
               removeALLActivity();
               break;
           }
       }
    }

    private void help() {
        String url = "http://192.168.31.9:8080/Demo/system/center/user?action=list&page=1&rows=5";
        RequestParams params = new RequestParams(url);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    Object total = result.get("total");
                    System.out.println("total = " + total);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                toast(result.toString());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                toast("error");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                toast("cancel");
            }

            @Override
            public void onFinished() {
                toast("finish");
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
    }

    private void initView() {
        tv_main_title.setText("趣味英语打地鼠");
    }

}
