package com.ecjtu.whack_a_mole.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Toast;
import com.ecjtu.whack_a_mole.util.MyApplication;

/**
 * @author Administrator on 2017/1/6.
 * @version 1.0
 */
public class BaseActivity extends Activity{
    private MyApplication application;
    private BaseActivity context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(application == null){
            application = (MyApplication) getApplication();
        }
        context = this;
        addActivity();
    }
    // 添加Activity方法
    public void addActivity() {
        application.addActivity(context);// 调用myApplication的添加Activity方法
    }
    //销毁当个Activity方法
    public void removeActivity() {
        application.removeActivity(context);// 调用myApplication的销毁单个Activity方法
    }
    //销毁所有Activity方法
    public void removeALLActivity() {
        application.removeALLActivity();// 调用myApplication的销毁所有Activity方法
    }

    /* 把Toast定义成一个方法  可以重复使用，使用时只需要传入需要提示的内容即可*/
    public void toast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
}
