package com.ecjtu.whack_a_mole.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    @ViewInject(R.id.et_username)
    private EditText et_username;

    @ViewInject(R.id.et_password)
    private EditText et_password;

    @ViewInject(R.id.cb_password)
    private CheckBox cb_password;

    @Event({R.id.btn_login,R.id.btn_register})
    private void onClick(View v){
        switch (v.getId()){
            case R.id.btn_login:{
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                //login(username,password);
                break;
            }
            case R.id.btn_register:{
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                add(username,password);
                break;
            }
        }
    }

    private void add(String username,String password) {
        String url = "http://139.199.210.125:8097/mole/system/user?action=add&username="+username+"&password="+password;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }
}
