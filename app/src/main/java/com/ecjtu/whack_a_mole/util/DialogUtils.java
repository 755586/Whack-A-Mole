package com.ecjtu.whack_a_mole.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import com.ecjtu.whack_a_mole.activity.LoginActivity;

/**
 * @author Administrator on 2017-04-20.
 * @version 1.0
 */
public class DialogUtils {
    private static ProgressDialog waitingDialog = null;//加载中的弹框
    private static AlertDialog.Builder normalDialog = null;//含确认取消按钮的提示框

    public static void showWaitingDialog(Context context,String msg) {
    /* 等待Dialog具有屏蔽其他控件的交互能力
     * @setCancelable 为使屏幕不可点击，设置为不可取消(false)
     * 下载等事件完成后，主动调用函数关闭该Dialog
     */
        waitingDialog= new ProgressDialog(context);
//        waitingDialog.setTitle("我是一个等待Dialog");
        waitingDialog.setMessage(msg);
        waitingDialog.setIndeterminate(true);
        waitingDialog.setCancelable(false);
        waitingDialog.show();
    }
    public static void hideWaitingDialog() {
    /* 等待Dialog具有屏蔽其他控件的交互能力
     * @setCancelable 为使屏幕不可点击，设置为不可取消(false)
     * 下载等事件完成后，主动调用函数关闭该Dialog
     */
        waitingDialog.hide();
    }
    public static void showAlertDialog(Context context,String title,String msg){
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        normalDialog = new AlertDialog.Builder(context);
        normalDialog.setTitle(title);
        normalDialog.setMessage(msg);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.dismiss();;
                    }
                });
        normalDialog.create().show();
    }
}
