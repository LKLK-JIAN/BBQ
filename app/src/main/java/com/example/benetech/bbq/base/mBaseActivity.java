package com.example.benetech.bbq.base;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.camera2.params.BlackLevelPattern;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.benetech.bbq.BluetoothActivity;
import com.example.benetech.bbq.service.Timedown2Service;
import com.example.benetech.bbq.service.Timedown3Service;
import com.example.benetech.bbq.service.Timedown4Service;
import com.example.benetech.bbq.service.TimedownService;

public abstract class mBaseActivity extends AppCompatActivity {

    public Context mContext;
    public BluetoothManager bluetoothManager;
    public BluetoothAdapter bluetoothAdapter;
    public SharedPreferences sp;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        Log.e("TAG", "onCreate:77777777777777 "+mContext.getApplicationContext() );

        setRequestedOrientation(getOrientation());
        // 无标题栏，无状态栏

        requestWindowFeature(Window.FEATURE_NO_TITLE);
       //取消状态栏
        getSupportActionBar().hide();
        //设置界面切换动画
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initWindowTrantation(getWindowTransition());
        }
        int layoutID=getLayoutID();
        if(layoutID!=0){
            setContentView(layoutID);
        }

        bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();
        sp = getSharedPreferences("bbq", MODE_PRIVATE);
        editor = sp.edit();
        addPermissin1();

        initEventAndData(savedInstanceState);

    }


    //获取布局ID
    protected abstract int getLayoutID();

    protected abstract void initEventAndData(Bundle savedInstanceState);

    //确定屏幕方向
    protected int getOrientation() {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    /*
    过度页面切换动画
    默认是淡入淡出
     */
    protected Transition getWindowTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new Slide();
        }
        return new Fade();
    }

    /*
    初始化过度动画
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void initWindowTrantation(Transition transition) {
        getWindow().setReturnTransition(transition);
        getWindow().setEnterTransition(transition);
        getWindow().setExitTransition(transition);
        getWindow().setReenterTransition(transition);
    }


    private void addPermissin1() {
        final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//如果 API level 是大于等于 23(Android 6.0) 时
            //判断是否具有权限
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                //判断是否需要向用户解释为什么需要申请该权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //请求权限
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            REQUEST_CODE_ACCESS_COARSE_LOCATION);
                    //Toast.makeText(this, "自Android 6.0开始需要打开位置权限才可以搜索到Ble设备", Toast.LENGTH_SHORT).show();
                }

            }

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                }

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveInitState();
        stopService(new Intent(mContext,TimedownService.class));
        stopService(new Intent(mContext,Timedown2Service.class));
        stopService(new Intent(mContext,Timedown3Service.class));
        stopService(new Intent(mContext,Timedown4Service.class));
    }

    private void  saveInitState(){
        editor.putInt("temp_unit01",    BluetoothActivity.temp_unit01);             //0代表℃，1代表℉
        editor.putInt("temp_unit02",    BluetoothActivity.temp_unit02);
        editor.putInt("temp_unit03",    BluetoothActivity.temp_unit03);
        editor.putInt("temp_unit04",    BluetoothActivity.temp_unit04);
        editor.putInt("settemp_type01",  BluetoothActivity.set_type01);              //0代表预设，1代表手动
        editor.putInt("settemp_type02",  BluetoothActivity.set_type02);              //0代表预设，1代表手动
        editor.putInt("settemp_type03",  BluetoothActivity.set_type03);              //0代表预设，1代表手动
        editor.putInt("settemp_type04",  BluetoothActivity.set_type04);              //0代表预设，1代表手动
        editor.putInt("preset_location01",  BluetoothActivity.preset_location01);   //预设位置
        editor.putInt("preset_location02",  BluetoothActivity.preset_location02);   //预设位置
        editor.putInt("preset_location03",  BluetoothActivity.preset_location03);   //预设位置
        editor.putInt("preset_location04",  BluetoothActivity.preset_location04);   //预设位置
        editor.putInt("preset_location201", BluetoothActivity.preset_location201);  //预设位置2
        editor.putInt("preset_location202", BluetoothActivity.preset_location201);  //预设位置2
        editor.putInt("preset_location203", BluetoothActivity.preset_location203);  //预设位置2
        editor.putInt("preset_location204", BluetoothActivity.preset_location04);   //预设位置2
        editor.putInt("temp_reminder01",    BluetoothActivity.temp_reminder01);
        editor.putInt("temp_reminder02", BluetoothActivity.temp_reminder02);
        editor.putInt("temp_reminder03", BluetoothActivity.temp_reminder03);
        editor.putInt("temp_reminder04", BluetoothActivity.temp_reminder04);
        editor.putInt("time_reminder01", BluetoothActivity.time_reminder01);
        editor.putInt("time_reminder02", BluetoothActivity.time_reminder02);
        editor.putInt("time_reminder03", BluetoothActivity.time_reminder03);
        editor.putInt("time_reminder04", BluetoothActivity.time_reminder04);
        editor.putInt("temp_manualc01",   BluetoothActivity.temp_manualc01);
        editor.putInt("temp_manualc02",   BluetoothActivity.temp_manualc02);
        editor.putInt("temp_manualc03",   BluetoothActivity.temp_manualc03);
        editor.putInt("temp_manualc04",   BluetoothActivity.temp_manualc04);
        editor.putInt("temp_manualf01",   BluetoothActivity.temp_manualf01);
        editor.putInt("temp_manualf02",   BluetoothActivity.temp_manualf02);
        editor.putInt("temp_manualf03",   BluetoothActivity.temp_manualf03);
        editor.putInt("temp_manualf04",   BluetoothActivity.temp_manualf04);
        editor.putInt("sound_reminder01",BluetoothActivity.sound_reminder01);
        editor.putInt("sound_remidner02",BluetoothActivity.sound_reminder02);
        editor.putInt("sound_reminder03",BluetoothActivity.sound_reminder03);
        editor.putInt("sound_reminder04",BluetoothActivity.sound_reminder04);
        editor.apply();
    }
}
