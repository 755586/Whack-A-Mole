package com.ecjtu.whack_a_mole.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Pair;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.ecjtu.whack_a_mole.bean.IconText;
import com.ecjtu.whack_a_mole.bean.IconTextView;
import com.ecjtu.whack_a_mole.util.GameWord;
import com.ecjtu.whack_a_mole.util.MyRandom;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import static com.ecjtu.whack_a_mole.activity.R.id.itv_game_no5;

@ContentView(R.layout.activity_game)
public class GameActivity extends BaseActivity {
    @ViewInject(R.id.tv_game_title)
    private TextView tv_game_title;
    @ViewInject(R.id.pBar_game_time)
    private ProgressBar pBar_game_time;

    private boolean isGameOver;
    private int progress;
    private static final int timeRefresh = 100;
    private static final int maxProgress = 400;
    private static final int timeOut = 8000;
    private int[] moles = new int[3];
    private int errNum = 3;

    //handle 接收游戏线程反馈的消息
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x123:{//succuss
                    isGameOver = true;
                    initData();//游戏继续，重新初始化游戏界面，弹出地鼠
                    break;
                }
                case 0x124:{//error
                    errNum--;
                    System.out.println("errNum = " + errNum);
                    if(errNum<=0){
                        toast("Game Over");
                        isGameOver = true;
                        removeALLActivity();
                        startActivity(new Intent(GameActivity.this,MainActivity.class));
                    }else{
                        toast("生命值减一");
                        initData();
                    }
                    break;
                }
                case 0x125:{//update time
                    pBar_game_time.setProgress(progress);
                    break;
                }
                case 0x126:{//timeOut
                    errNum--;
                    System.out.println("errNum = " + errNum);
                    if(errNum<=0){
                        toast("Game Over");
                        isGameOver = true;
                        removeALLActivity();
                        startActivity(new Intent(GameActivity.this,MainActivity.class));
                    }else{
                        toast("生命值减一");
                        initData();
                    }
                    break;
                }
            }
            super.handleMessage(msg);
        }
    };
    //保存游戏页面的布局ID
    private int[] noIds = {R.id.itv_game_no1,R.id.itv_game_no2,R.id.itv_game_no3
            ,R.id.itv_game_no4, itv_game_no5,R.id.itv_game_no6
            ,R.id.itv_game_no7,R.id.itv_game_no8,R.id.itv_game_no9};
    //保存跳出的地鼠
    private List<IconTextView> itvList;
    private int choose = GameWord.getInstance().getChoose();
    private List<Pair<String, String>> wordlist = GameWord.getInstance().getWordListMap().get(choose);
    private int wordTotal = wordlist.size();
    private String chinese;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();//初始化视图及监听
        initData();//初始化数据
        startGameThread();//开始游戏
    }

    //初始化视图及监听
    private void initView() {
        itvList = new ArrayList<>();
        for(int id:noIds){
            IconTextView itv = (IconTextView) findViewById(id);
            //设置地鼠被点击的事件
            itv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //moles : 存储跳出的地鼠Id，默认下标为0的地鼠为正确答案，其他的事干扰项
                    if(view.getId()==itvList.get(moles[0]).getId()){
                        ((IconTextView) view).changeIcon(getResources().getDrawable(R.mipmap.ds3));
                        Message message = Message.obtain();
                        message.what = 0x123;
                        handler.sendMessage(message);
                    }else{
                        Message message = Message.obtain();
                        message.what = 0x124;
                        handler.sendMessage(message);
                    }
                }
            });
            itv.setClickable(false);
            itvList.add(itv);
        }

    }

    //初始化数据
    private void initData() {
        chinese = "";
        isGameOver = false;
        progress = maxProgress;//重置进度
        //重置界面布局属性（置空、不能点击）
        for(int i=0;i<itvList.size();i++){
            IconTextView iconTextView = itvList.get(i);
            iconTextView.setIconText(new IconText("",null));
            iconTextView.setClickable(false);
        }
        //随机获取地鼠弹出的号码
        int len = moles.length;
        for(int i=0;i<len;){
            int r = MyRandom.getNumble(itvList.size());
            boolean ok=true;
            for(int j=0;j<i;j++){//避免取得的随机数相同
                if(moles[j]==r){
                    ok=false;
                    break;
                }
            }
            if(ok){
                moles[i] =r;
                itvList.get(r).setClickable(true);
                String english = getEnglish(r,i);
                itvList.get(r).setIconText(new IconText(english,getResources().getDrawable(R.mipmap.ds4)));
                i++;
            }
        }
        System.out.println("=====moles====");
        for(int i=0;i<len;i++){
            System.out.println(moles[i]);
        }
        tv_game_title.setText(chinese);
        pBar_game_time.setMax(maxProgress);
        pBar_game_time.setProgress(maxProgress);
    }

    private String getEnglish(int r,int index) {
        System.out.println(r+"=="+index);
        int tem = MyRandom.getNumble(wordTotal/9);
        System.out.println("tem = " + tem);
        Pair<String, String> english = wordlist.get(tem * 9 + r);
        if(index==0) chinese =english.second;
        return english.first;
    }


    //开始游戏
    private void startGameThread(){
        //创建一个同步的线程，用来实时监听游戏进度
        Thread thread = new Thread(new Runnable() {
            @Override
            public synchronized  void run() {
                while(!isGameOver){//游戏未结束
                    Message message = Message.obtain();
                    if(progress<=0){//超时
                        message.what = 0x126;
                        handler.sendMessage(message);
                    }else{
                        progress-=(timeRefresh*maxProgress/timeOut);
                        message.what = 0x125;
                        handler.sendMessage(message);
                    }
                    try {
                        Thread.sleep(timeRefresh);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }
}
