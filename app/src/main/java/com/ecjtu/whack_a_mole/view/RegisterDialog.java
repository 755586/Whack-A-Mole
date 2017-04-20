package com.ecjtu.whack_a_mole.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.ecjtu.whack_a_mole.activity.R;
import com.ecjtu.whack_a_mole.util.DialogUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

/**
 * @author Administrator on 2017-04-20.
 * @version 1.0
 */
public class RegisterDialog extends Dialog{
    private Context context;
    public RegisterDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_register);
        initView();
    }

    private void initView() {
        final EditText et_username =
                (EditText) findViewById(R.id.et_username);
        final EditText et_password =
                (EditText) findViewById(R.id.et_password);
        final EditText et_password2 =
                (EditText) findViewById(R.id.et_password2);
        Button btn_cancle = (Button)findViewById(R.id.btn_cancle);
        Button btn_register = (Button)findViewById(R.id.btn_register);
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("取消");
                RegisterDialog.this.dismiss();
            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = et_username.getText().toString();
                String password = et_password.getText().toString();
                String password2 = et_password2.getText().toString();
                String msg = "";
                if(name.equals("")){
                    msg="用户名不能为空";
                }else if(password.equals("")){
                    msg="密码不能为空";
                }else if(password.equals("")){
                    msg="请确认密码";
                }else if(!password.equals(password2)){
                    msg="确认密码有误";
                }
                if(msg.equals("")){
                    add(name,password);
                }else{
                    DialogUtils.showAlertDialog(context,"提示",msg);
                }
            }
        });
    }
    private void add(String name,String password) {
        DialogUtils.showWaitingDialog(context,"注册中...");
        String url = "http://139.199.210.125:8097/mole/system/user?action=add&name="+name+"&password="+password;
        RequestParams params = new RequestParams(url);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    Object success = result.get("success");
                    if(success.equals(true)){
                        System.out.println("注册成功-onSuccess");
                        RegisterDialog.this.dismiss();
                        Toast.makeText(context,"注册成功",Toast.LENGTH_LONG);
                    }else{
                        System.out.println("注册失败-onSuccess");
                        DialogUtils.showAlertDialog(context,"提示",result.get("errorMsg").toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("注册异常-JSONException");
                    DialogUtils.showAlertDialog(context,"提示","注册异常");
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                System.out.println("注册出错-onError");
                DialogUtils.showAlertDialog(context,"提示","注册出错");
            }

            @Override
            public void onCancelled(CancelledException cex) {
                System.out.println("注册取消-onCancelled");
                DialogUtils.showAlertDialog(context,"提示","注册取消");
            }

            @Override
            public void onFinished() {
                System.out.println("注册完成-onFinished");
                DialogUtils.hideWaitingDialog();
            }
        });
    }
}
