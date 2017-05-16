package com.ecjtu.whack_a_mole.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import com.ecjtu.whack_a_mole.util.DialogUtils;
import com.ecjtu.whack_a_mole.util.LoginUser;
import com.ecjtu.whack_a_mole.view.RegisterDialog;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;

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
//                add(name,password);
                showRegisterDialog();
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
    }

    private void login(final String name, final String password) {
        DialogUtils.showWaitingDialog(LoginActivity.this,"登录中...");
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
                        SharedPreferences sharedPreferences= getSharedPreferences("user",
                                Activity.MODE_PRIVATE);
                        //实例化SharedPreferences.Editor对象
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        //用putString的方法保存数据
                        editor.putString("username",name);
                        System.out.println("====cb_password:"+cb_password.isChecked());
                        if(!cb_password.isChecked()){
                            et_password.setText("");
                            editor.putBoolean("isChecked",false);
                        }else{
                            editor.putString("password", password);
                            editor.putBoolean("isChecked",true);
                            //提交当前数据
                        }
                        editor.apply();
                        LoginUser.getInstance().setName(name);
                        LoginUser.getInstance().setLoginTime(new Date());
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
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
                DialogUtils.hideWaitingDialog();
                //toast("finish");
            }
        });
    }


    private void showRegisterDialog() {
        RegisterDialog registerDialog = new RegisterDialog(LoginActivity.this);
        registerDialog.setTitle("新用户注册");
        registerDialog.setCancelable(false);
        registerDialog.show();
    }



    private void initView() {
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                Activity.MODE_PRIVATE);
        String username=sharedPreferences.getString("username","");
        String password=sharedPreferences.getString("password","");
        boolean isChecked=sharedPreferences.getBoolean("isChecked",false);
        System.out.println("isChecked = " + isChecked);
        cb_password.setChecked(isChecked);
        et_username.setText(username);
        if(isChecked){
            et_password.setText(password);
        }
    }
}
