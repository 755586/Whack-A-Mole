package com.ecjtu.whack_a_mole.activity;

import android.app.Activity;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.ecjtu.whack_a_mole.bean.IconText;
import com.ecjtu.whack_a_mole.bean.IconTextView;
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
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0x123:{//succuss
                    isGameOver = true;
                    initData();
                    startGameThread();
                    break;
                }
                case 0x124:{//error
                    toast("游戏结束");
                    isGameOver = true;
                    break;
                }
                case 0x125:{//update time
                    pBar_game_time.setProgress(progress);
                    break;
                }
                case 0x126:{//timeOut
                    toast("超时了");
                    break;
                }
            }
            super.handleMessage(msg);
        }
    };
    private int[] noIds = {R.id.itv_game_no1,R.id.itv_game_no2,R.id.itv_game_no3
            ,R.id.itv_game_no4, itv_game_no5,R.id.itv_game_no6
            ,R.id.itv_game_no7,R.id.itv_game_no8,R.id.itv_game_no9};

    private List<IconTextView> itvList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        initView();
        initData();
        startGameThread();
    }

    private void initData() {
        isGameOver = false;
        progress = maxProgress;
        for(int i=0;i<itvList.size();i++){
            IconTextView iconTextView = itvList.get(i);
            iconTextView.setIconText(new IconText("",null));
            iconTextView.setClickable(false);
        }
        for(int i=0;i<moles.length;i++){
            moles[i] = MyRandom.getNumble(itvList.size());
            itvList.get(moles[i]).setClickable(true);
            itvList.get(moles[i]).setIconText(new IconText(""+i,getResources().getDrawable(R.mipmap.ds4)));
        }
        tv_game_title.setText("0");
        pBar_game_time.setMax(maxProgress);
        pBar_game_time.setProgress(maxProgress);
    }

    private void initView() {
        itvList = new ArrayList<>();
        for(int id:noIds){
            IconTextView itv = (IconTextView) findViewById(id);
            itv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
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
    private void startGameThread(){
        Thread thread = new Thread(new Runnable() {
            @Override
            public synchronized  void run() {
                while(!isGameOver){
                    Message message = Message.obtain();
                    if(progress<=0){
                        message.what = 0x126;
                        handler.sendMessage(message);
                        isGameOver = true;
                    }else{
                        message.what = 0x125;
                        handler.sendMessage(message);
                    }
                    progress-=(timeRefresh*maxProgress/timeOut);
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
