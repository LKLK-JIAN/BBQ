package com.example.benetech.bbq.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class Timedown3Service extends Service {
    public int channel;

    public int time3;

    Timer timer = new Timer();

    public Timedown3Service(){
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);

        time3=intent.getIntExtra("time3", 0);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timer.schedule(task,500,1000);

    }

    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            time3--;
            Intent intent=new Intent();
            intent.putExtra("time3",time3);

            intent.setAction("com.project.moli.demobroad.MyService");
            if(time3>=0){
                sendBroadcast(intent);
            }
//            if(time3==0){
//                if(task!=null){
//                    task.cancel();
//                }
//                if(timer!=null){
//                    timer.cancel();
//                }
//            }
        }
    };
}
