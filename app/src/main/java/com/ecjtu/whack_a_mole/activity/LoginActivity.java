package com.ecjtu.whack_a_mole.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.ecjtu.whack_a_mole.util.MyDialog;
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
        MyDialog.showWaitingDialog(LoginActivity.this,"登录中...");
        String url = "http://139.199.210.125:8097/mole/login?action=checkUser&username="+name+"&password="+password;
        RequestParams params = new RequestParams(url);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    //System.out.println("===========onSuccess================");
                    Object user = result.get("user");
                    if(user.equals(null)){
                        System.out.println(result.getString("msg"));
                        toast(result.getString("msg"));
                    }else{
                        System.out.println(result.getString("msg"));
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                        if(!cb_password.isChecked()){
                            et_password.setText("");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("登录失败-JSONException");
                    toast("登录失败");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //toast("error");
                System.out.println("登录出错-JSONException");
                toast("登录出错");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                //toast("cancel");
                System.out.println("登录取消-onCancelled");
                toast("登录取消");
            }

            @Override
            public void onFinished() {
                System.out.println("登录结束-onFinished");
                MyDialog.hideWaitingDialog();
                //toast("finish");
            }
        });
    }
    private void add(String name,String password) {
        MyDialog.showWaitingDialog(LoginActivity.this,"加载中...");
        String url = "http://139.199.210.125:8097/mole/system/user?action=add&name="+name+"&password="+password;
        RequestParams params = new RequestParams(url);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    Object success = result.get("success");
                    if(success.equals(true)){
                        System.out.println("注册成功-onSuccess");
                        toast("注册成功");
                    }else{
                        System.out.println("注册失败-onSuccess");
                        toast(result.get("errorMsg").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("注册异常-JSONException");
                    toast("注册异常");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("注册出错-onError");
                toast("注册出错");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                System.out.println("注册取消-onCancelled");
                toast("注册取消");
            }

            @Override
            public void onFinished() {
                System.out.println("注册完成-onFinished");
                MyDialog.hideWaitingDialog();
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
        final EditText et_username =
                (EditText) dialogView.findViewById(R.id.et_username);
        final EditText et_password =
                (EditText) dialogView.findViewById(R.id.et_password);
        EditText et_password2 =
                (EditText) dialogView.findViewById(R.id.et_password2);
        et_password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){//获取到焦点

                }else{//失去焦点

                }
            }
        });
        et_password2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){

                }else{

                }
            }
        });
        customizeDialog.setPositiveButton("注册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 获取EditView中的输入内容
                String username = et_username.getText().toString();
                String password = et_password.getText().toString();
                add(username,password);
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
