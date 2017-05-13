package com.ecjtu.whack_a_mole.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Pair;
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
    private int chooseMenuItem;

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
               toast("游戏帮助");
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
    }

    private void initView() {
        tv_main_title.setText("趣味英语打地鼠");
    }

    private void showMenuDialog() {
        chooseMenuItem = 0;
        final String[] items = new String[nameList.size()];
        for(int i=0;i<nameList.size();i++){
            items[i] = nameList.get(i);
        }
        if(items.length>0){
            AlertDialog.Builder singleChoiceDialog =
                    new AlertDialog.Builder(MainActivity.this);
            singleChoiceDialog.setTitle("请选择游戏模式");
            singleChoiceDialog.setSingleChoiceItems(items, 0,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            chooseMenuItem = which;
                        }
                    });
            singleChoiceDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (chooseMenuItem == -1) {
                                toast("请选择模式");
                            }else{
                                int type = allTypeMap.get(nameList.get(chooseMenuItem));
                                getWordByType(type);
                            }
                        }
                    });
            singleChoiceDialog.show();
        }else{
            toast("系统维护中...");
        }
    }

    //根据所选类型查询词汇信息
    private void getWordByType(int type) {
        //定义一个变量用于存储返回结果的解析数据，并尝试通过工程类获取结果
        List<Pair<String, String>> wordList = GameWord.getInstance().getWordListMap().get(chooseMenuItem);
        //当前已存在数据，无须解析
        if(wordList!=null){
            System.out.println("存在词汇信息");
            if(wordList.size()>=9){
                System.out.println("加载词汇数据成功,正在进入游戏...");
                GameWord.getInstance().setChoose(chooseMenuItem);
                startActivity(new Intent(MainActivity.this,GameActivity.class));
            }else{
                toast("当前无法进入该模式");
            }
        }else{
            System.out.println("不存在词汇信息");
            DialogUtils.showWaitingDialog(MainActivity.this,"数据加载中");//弹出遮罩效果，提示正在加载数据
            //根据词汇类型type查询词汇数据的接口地址
            String url = "http://139.199.210.125:8097/mole/system/word?action=getListByType&type="+type;
            RequestParams params = new RequestParams(url);
            x.http().post(params, new Callback.CommonCallback<JSONObject>() {
                @Override
                public void onSuccess(JSONObject result) {//请求成功，并获取返回值
                    //System.out.println(result.toString());
                    //toast(result.toString());
                    loadGame(result);//解析返回值
                }
                @Override
                public void onError(Throwable ex, boolean isOnCallback) {//请求发生错误
                    toast("加载数据出错");
                }
                @Override
                public void onCancelled(CancelledException cex) {//取消请求回调方法
                }
                @Override
                public void onFinished() {//请求结束返回方法
                    DialogUtils.hideWaitingDialog();//隐藏数据加载遮罩框
                }
            });
        }
    }
    //请求结果返回值解析，并进入下一步游戏界面
    private void loadGame(JSONObject result) {
        //定义一个变量用于存储返回结果的解析数据，并尝试通过工程类获取结果
        List<Pair<String, String>> wordList = GameWord.getInstance().getWordListMap().get(chooseMenuItem);
        wordList = new ArrayList<>();
        try {
            JSONArray rows = (JSONArray) result.get("rows");//根据key值获取词汇数据
            for(int i=0;i<rows.length();i++){//将获取到的json数据进行解析成数组数据并保存
                JSONObject word_obj = rows.getJSONObject(i);
                String chinese = word_obj.get("chinese").toString();
                String english = word_obj.get("english").toString();
                Pair<String,String> word = new Pair<>(english,chinese);
                wordList.add(word);
            }
            GameWord.getInstance().getWordListMap().put(chooseMenuItem,wordList);
            if(wordList.size()>=9){
                System.out.println("加载词汇数据成功,正在进入游戏...");
                GameWord.getInstance().setChoose(chooseMenuItem);
                startActivity(new Intent(MainActivity.this,GameActivity.class));
            }else{
                toast("当前无法进入该模式");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            toast("加载数据出现异常");
        }
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
        String url = "http://139.199.210.125:8097/mole/system/wordType?action=getAllType";
        RequestParams params = new RequestParams(url);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    JSONArray allType = (JSONArray) result.get("allType");
                    nameList = new ArrayList<String>();
                    allTypeMap = new HashMap<String, Integer>();
                    for(int i=0;i<allType.length();i++){
                        JSONObject type = (JSONObject) allType.get(i);
                        Integer id = (Integer) type.get("id");
                        String type_name = (String) type.get("type_name");
                        nameList.add(type_name)
                        ;                       allTypeMap.put(type_name,id);
                    }
                    GameWord.getInstance().setAllTypeName(nameList);
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

}
