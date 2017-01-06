package com.ecjtu.utils;

import android.app.Application;
import android.util.Log;
import org.xutils.x;

/**
 * @author ConstQ on 2017/1/5.
 * @version 1.0
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
    }
}
