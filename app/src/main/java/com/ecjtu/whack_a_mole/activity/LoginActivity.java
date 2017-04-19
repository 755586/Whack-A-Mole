package com.ecjtu.whack_a_mole.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Map;

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
                String name = et_username.getText().toString();
                String password = et_password.getText().toString();
                login(name,password);
                break;
            }
            case R.id.btn_register:{
//                String name = et_username.getText().toString();
//                String password = et_password.getText().toString();
//                add(name,password);
                showCustomizeDialog();
                break;
            }
        }
    }

    private void login(String name,String password) {
        String url = "http://139.199.210.125:8097/mole/login?action=checkUser&username="+name+"&password="+password;
        RequestParams params = new RequestParams(url);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    Object user = result.get("user");
                    if(user==null){
                        System.out.println("登录失败");
                        toast(result.getString("msg"));
                    }else{
                        System.out.println("登录成功");
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        if(!cb_password.isChecked()){
                            et_password.setText("");
                        }
                        //toast("登录成功");
                    }
                } catch (JSONException e) {
                    System.out.println("登录失败");
                    toast("登录失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //toast("error");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                //toast("cancel");
            }

            @Override
            public void onFinished() {
                //toast("finish");
            }
        });
    }
    private void add(String name,String password) {
        String url = "http://139.199.210.125:8097/mole/system/user?action=add&name="+name+"&password="+password;
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

    private void showCustomizeDialog() {
    /* @setView 装入自定义View ==> R.layout.dialog_customize
     * 由于dialog_customize.xml只放置了一个EditView，因此和图8一样
     * dialog_customize.xml可自定义更复杂的View
     */
        AlertDialog.Builder customizeDialog =
                new AlertDialog.Builder(LoginActivity.this);
        final View dialogView = LayoutInflater.from(LoginActivity.this)
                .inflate(R.layout.dialog_register,null);
        customizeDialog.setTitle("注册新用户");
        customizeDialog.setView(dialogView);
        EditText et_username =
                (EditText) dialogView.findViewById(R.id.et_username);
        EditText et_password =
                (EditText) dialogView.findViewById(R.id.et_password);
        EditText et_password2 =
                (EditText) dialogView.findViewById(R.id.et_password2);
        customizeDialog.setPositiveButton("注册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 获取EditView中的输入内容

            }
        });
        customizeDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        customizeDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
    }

    private void initView() {
        cb_password.setChecked(false);
    }
}
