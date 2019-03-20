package com.example.benetech.bbq.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.example.benetech.bbq.wight.TimeDownActivity;

import java.util.Timer;
import java.util.TimerTask;

public class TimedownService extends Service {

    public int channel;
    public int time1;
//    public int time2;
//    public int time3;
//    public int time4;

    public Timer timer = new Timer();

    public TimedownService(){
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        time1=intent.getIntExtra("time1", -1);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onDestroy() {
        if(timer!=null){
            timer.cancel();
            timer=null;
            Intent intent=new Intent();
            intent.putExtra("time1",0);
            intent.setAction("com.project.moli.demobroad.MyService");
        }
        super.onDestroy();
    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            time1--;
            Intent intent=new Intent();
            if(time1<0){time1=-1;}
            intent.putExtra("time1",time1);
            intent.setAction("com.project.moli.demobroad.MyService");
            sendBroadcast(intent);

        }
    };

}
