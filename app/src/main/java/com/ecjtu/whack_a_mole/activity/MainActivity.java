package com.ecjtu.whack_a_mole.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;
import com.ecjtu.whack_a_mole.util.DialogUtils;
import com.ecjtu.whack_a_mole.util.GameWord;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.*;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    private List<String> nameList = GameWord.getInstance().getAllTypeName();
    private Map<String,Integer> allTypeMap = GameWord.getInstance().getAllType();

    @ViewInject(R.id.tv_main_title)
    private TextView tv_main_title;

    @Event({R.id.btn_main_begin,R.id.btn_main_help,R.id.btn_main_exit})
    private void onClick(View v){
       switch (v.getId()){
           case R.id.btn_main_begin:{
//               startActivity(new Intent(MainActivity.this,GameActivity.class));
               getAllType();
               break;
           }
           case R.id.btn_main_help:{
//               DialogUtils.showAlertDialog(MainActivity.this,"","帮助");
               getWordByType(0);
               break;
           }
           case R.id.btn_main_exit:{
               removeALLActivity();
               break;
           }
       }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x123:{//succuss
                    int temp = msg.arg1;
                    if(temp==0){
                        showMenuDialog();
                    }else{
                        String err = msg.obj.toString();
                        toast(msg.obj.toString());
                        System.out.println(err);
                    }
                    break;
                }
            }
            super.handleMessage(msg);
        }
    };
    private void showMenuDialog() {
        final Set<Integer> yourChoices = new HashSet<>();
        final String[] items = new String[nameList.size()];
        // 设置默认选中的选项，全为false默认均未选中
        final boolean initChoiceSets[]=new boolean[nameList.size()];
        for(int i=0;i<nameList.size();i++){
            items[i] = nameList.get(i);
            initChoiceSets[i] = false;
        }
        yourChoices.clear();
        AlertDialog.Builder multiChoiceDialog =
                new AlertDialog.Builder(MainActivity.this);
        multiChoiceDialog.setTitle("请选择游戏模式");
        multiChoiceDialog.setMultiChoiceItems(items, initChoiceSets,
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which,
                                        boolean isChecked) {
                        //System.out.println(which + " == " +isChecked);
                        if (isChecked) {
                            yourChoices.add(which);
                        } else {
                            yourChoices.remove(which);
                        }
                    }
                });
        multiChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str = "";
                        for (Integer i:yourChoices) {
                            str += items[i] + " ";
                            //System.out.println(items[i]+ " " +i);
                        }
                        loadWord(yourChoices);
                    }
                });
        multiChoiceDialog.show();
    }

    private void loadWord(Set<Integer> yourChoices) {
        if(yourChoices.size()>0){
            int type = allTypeMap.get(nameList.get(0));
            getWordByType(type);
        }
    }


    private void getWordByType(int type) {
        DialogUtils.showWaitingDialog(MainActivity.this,"数据加载中");
        String url = "http://139.199.210.125:8097/mole/system/word?action=getListByType&type="+type;
        RequestParams params = new RequestParams(url);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                System.out.println(result.toString());
                toast(result.toString());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                toast("加载数据出错");
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {
                DialogUtils.hideWaitingDialog();
            }
        });
    }

    private void getAllType() {
        if(nameList!=null&&allTypeMap!=null){
            System.out.println("===存在数据===");
//            System.out.println(nameList.toString());
//            System.out.println(allTypeMap.toString());
            Message msg = Message.obtain();
            msg.what=0x123;
            msg.arg1=0;
            msg.obj="获取数据成功";
            handler.sendMessage(msg);
            return;
        }else{
            System.out.println("===不存在数据===");
        }
        final Message msg = Message.obtain();
        msg.what=0x123;
        msg.arg1=1;
        msg.obj="获取菜单数据中...";
        DialogUtils.showWaitingDialog(MainActivity.this,"正在加载菜单...");
        String url = "http://139.199.210.125:8097/mole/system/word?action=getAllType";
        RequestParams params = new RequestParams(url);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONArray allTypeName = (JSONArray) result.get("allTypeName");
                    JSONObject allType = (JSONObject) result.get("allType");

                    nameList = new ArrayList<String>();
                    allTypeMap = new HashMap<String, Integer>();
                    for(int i=0;i<allTypeName.length();i++){
                        Object o = allTypeName.get(i);
                        nameList.add(o.toString());
                    }
                    GameWord.getInstance().setAllTypeName(nameList);
                    Iterator iterator = allType.keys();
                    while(iterator.hasNext()){
                        String key = (String) iterator.next();
                        Integer value = allType.getInt(key);
                        allTypeMap.put(key,value);
                    }
                    GameWord.getInstance().setAllType(allTypeMap);
//                    System.out.println(GameWord.getInstance().getAllTypeName().toString());
//                    System.out.println(GameWord.getInstance().getAllType().toString());
                    msg.arg1=0;
                    msg.obj="菜单加载成功";
                } catch (JSONException e) {
                    e.printStackTrace();
                    //toast("菜单加载出现异常");
                    msg.arg1=1;
                    msg.obj="菜单加载出现异常";
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //toast("菜单加载出错");
                msg.arg1=1;
                msg.obj="菜单加载出错";
            }

            @Override
            public void onCancelled(CancelledException cex) {
                //toast("菜单加载已取消");
                msg.arg1=1;
                msg.obj="菜单加载已取消";
            }

            @Override
            public void onFinished() {
                handler.sendMessage(msg);
                DialogUtils.hideWaitingDialog();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
        //getAllType();
    }

    private void initView() {
        tv_main_title.setText("趣味英语打地鼠");
    }

}
