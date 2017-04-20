package com.ecjtu.whack_a_mole.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.ecjtu.whack_a_mole.util.DialogUtils;
import org.json.JSONObject;
import org.xutils.common.Callback;
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
               DialogUtils.showAlertDialog(MainActivity.this,"","帮助");
                break;
           }
           case R.id.btn_main_exit:{
               removeALLActivity();
               break;
           }
       }
    }

    private void help() {
        String url = "http://139.199.210.125:8097/mole/system/word?action=getListByType&type_name=";
        RequestParams params = new RequestParams(url);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                System.out.println(result.toString());
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
