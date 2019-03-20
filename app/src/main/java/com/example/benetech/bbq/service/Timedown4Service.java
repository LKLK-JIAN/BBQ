package com.example.benetech.bbq.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.util.Timer;
import java.util.TimerTask;

public class Timedown4Service extends Service {

    public int channel;
    public int time4;
    Timer timer = new Timer();

    public Timedown4Service(){
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        time4=intent.getIntExtra("time4", 0);
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

            time4--;
            if(time4<0){
                time4=0;
            }
            Intent intent=new Intent();

            intent.putExtra("time4",time4);
            intent.setAction("com.project.moli.demobroad.MyService");
            if(time4>=0){
                sendBroadcast(intent);
            }

//            if(time4==0){
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
