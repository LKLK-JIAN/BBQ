package com.example.benetech.bbq.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class Timedown2Service extends Service {
    public int channel;
    //public int time1;
    public int time2;
    // public int time3;
    // public int time4;
    Timer timer = new Timer();

    public Timedown2Service() {
    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        time2 = intent.getIntExtra("time2", 0);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer.schedule(task, 500, 1000);
    }

    @Override
    public void onDestroy() {
//        if(timer!=null){
//            timer.cancel();
//            timer=null;
//        }
        super.onDestroy();
    }
    TimerTask task = new TimerTask() {
        @Override
        public void run() {

            time2--;
            Intent intent = new Intent();
            intent.putExtra("time2", time2);
            intent.setAction("com.project.moli.demobroad.MyService");
            if(time2>=0){
                sendBroadcast(intent);
            }
        }
    };

}
