package com.ecjtu.whack_a_mole.util;

import android.app.Activity;
import android.app.Application;
import org.xutils.common.util.LogUtil;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ConstQ on 2017/1/5.
 * @version 1.0
 */
public class MyApplication extends Application {
    private List<Activity> activityList;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        activityList = new ArrayList<>();
        LogUtil.e("MyAppliacation onCreate()");
    }

    /**
     * 添加Activity
     */
    public void addActivity(Activity activity) {
        // 判断当前集合中不存在该Activity
        LogUtil.d(activity.toString());
        if (!activityList.contains(activity)) {
            activityList.add(activity);//把当前Activity添加到集合中
            LogUtil.e("MyAppliacation addActivity() "+activity.getLocalClassName());
        }
    }

    /**
     * 销毁单个Activity
     */
    public void removeActivity(Activity activity) {
        //判断当前集合中存在该Activity
        if (activityList.contains(activity)) {
            activityList.remove(activity);//从集合中移除
            activity.finish();//销毁当前Activity
            LogUtil.e("MyAppliacation removeActivity() "+activity.getLocalClassName());
        }
    }

    /**
     * 销毁所有的Activity
     */
    public void removeALLActivity() {
        //通过循环，把集合中的所有Activity销毁
        LogUtil.e("MyAppliacation removeALLActivity() begin");
        for (Activity activity : activityList) {
            activity.finish();
            LogUtil.e(activity.getLocalClassName());
        }
        LogUtil.e("MyAppliacation removeALLActivity() over");
    }
}
