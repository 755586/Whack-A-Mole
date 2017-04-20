package com.ecjtu.whack_a_mole.activity;

import android.content.Intent;
import android.os.Bundle;
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

    private void getWordByType(int type) {
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

    private void getAllType() {
        if(GameWord.getInstance().getAllTypeName()!=null&&GameWord.getInstance().getAllType()!=null){
            System.out.println("===存在数据===");
            System.out.println(GameWord.getInstance().getAllTypeName().toString());
            System.out.println(GameWord.getInstance().getAllType().toString());
        }else{
            System.out.println("===不存在数据===");
        }
        String url = "http://139.199.210.125:8097/mole/system/word?action=getAllType";
        RequestParams params = new RequestParams(url);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONArray allTypeName = (JSONArray) result.get("allTypeName");
                    JSONObject allType = (JSONObject) result.get("allType");
                    List<String> nameList = new ArrayList<String>();
                    Map<String,Integer> allTypeMap = new HashMap<String, Integer>();
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
                //toast("finish");
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
