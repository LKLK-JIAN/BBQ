package com.example.benetech.bbq;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * Author: Blincheng.
 * Date: 2017/4/14.
 * Description:
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
