package com.example.benetech.bbq;

import android.app.Dialog;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.benetech.bbq.base.mBaseActivity;
import com.example.benetech.bbq.bluetoothapi.APIData;
import com.example.benetech.bbq.channel.ChannelfourFragment;
import com.example.benetech.bbq.channel.ChanneloneFragment;
import com.example.benetech.bbq.channel.ChannelthreeFragment;
import com.example.benetech.bbq.channel.ChanneltwoFragment;
import com.example.benetech.bbq.dialog.ReminderDialog;
import com.example.benetech.bbq.dialog.TimedownDialog;
import com.example.benetech.bbq.service.Timedown2Service;
import com.example.benetech.bbq.service.Timedown3Service;
import com.example.benetech.bbq.service.Timedown4Service;
import com.example.benetech.bbq.service.TimedownService;
import com.example.benetech.bbq.wight.MyTimePickerDialog;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class SettingActivity extends mBaseActivity implements View.OnClickListener, MyTimePickerDialog.TimePickerDialogInterface {
    //
    private RelativeLayout rl_setting_unit, rl_setting_timing, rl_setting_remindersetting,
            rl_setting_tempreminder, rl_setting_templimit, rl_setting_aboutus, rl_setting_feedback;
    private MyTimePickerDialog timePickerDialog;
    private TextView tv_setting_unit, tv_setting_timing, tv_setting_remindersetting, tv_setting_tempreminder;
    private ImageView iv_setting_back;

    public  mBroadcastReceiver mBroadcastReceiver;
    private int channel;
    private int sound_location;
    private int unit_location;

    private ArrayList<String> data=new ArrayList<>();
    private int rawId;
    private String rawName;
    private Field[] fields;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        Intent intent=getIntent();
        channel=intent.getIntExtra("channel",0);
        unitState();
        Log.e("TAG", "66666initEventAndData: "+sound_location);
        init();
    }

    private void init() {
        rl_setting_unit = findViewById(R.id.rl_setting_unit);
        rl_setting_unit.setOnClickListener(this);
        rl_setting_timing = findViewById(R.id.rl_setting_timing);
        rl_setting_timing.setOnClickListener(this);
        rl_setting_remindersetting = findViewById(R.id.rl_setting_remindersetting);
        rl_setting_remindersetting.setOnClickListener(this);
        rl_setting_tempreminder = findViewById(R.id.rl_setting_tempreminder);
        rl_setting_tempreminder.setOnClickListener(this);
        tv_setting_unit = findViewById(R.id.tv_setting_unit);
        tv_setting_timing = findViewById(R.id.tv_setting_timing);
        tv_setting_remindersetting = findViewById(R.id.tv_setting_remindersetting);
        tv_setting_tempreminder = findViewById(R.id.tv_setting_tempreminder);
        iv_setting_back=findViewById(R.id.iv_setting_back);
        iv_setting_back.setOnClickListener(this);
        timePickerDialog = new MyTimePickerDialog(SettingActivity.this);
        fields = R.raw.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                rawId = fields[i].getInt(R.raw.class);
                rawName = fields[i].getName();
                data.add(rawName);
                Log.e("TAG", "onClick:1111111 "+rawName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.e("TAG", "88888init: "+sound_location);
        tv_setting_remindersetting.setText(fields[sound_location].getName());
        if(unit_location==0){
            tv_setting_unit.setText("℃");
        }
        else{
            tv_setting_unit.setText("℉");
        }

    }

    //温度单位选择dialog
    private void setDialog() {
        final Dialog mCameraDialog = new Dialog(this, R.style.BottomDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(
                R.layout.bottom_dialog, null);
        //初始化视图
        root.findViewById(R.id.btn_dialog_c).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_setting_unit.setText("℃");
                unit_location=0;
                ChannelActivity.writeData(APIData.setUnit((byte)channel,(byte)1));
                mCameraDialog.dismiss();

            }
        });
        root.findViewById(R.id.btn_dialog_f).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_setting_unit.setText("℉");
                unit_location=1;
                ChannelActivity.writeData(APIData.setUnit((byte)channel,(byte)0));
                mCameraDialog.dismiss();
            }
        });
        root.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraDialog.dismiss();
            }
        });
        mCameraDialog.setContentView(root);
        Window dialogWindow = mCameraDialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        // 添加动画
        // dialogWindow.setWindowAnimations(R.style.dialogstyle);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.x = 0; // 新位置X坐标
        lp.y = 0; // 新位置Y坐标
        lp.width = getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        mCameraDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.rl_setting_unit:
                setDialog();
                break;
            case R.id.rl_setting_timing:
                Log.e("TAG", "onClick: "+BluetoothActivity.preset_location01 );
                BluetoothActivity.preset_location01=10;
                Log.e("TAG", "66666666onClick: "+BluetoothActivity.preset_location01 );
                final TimedownDialog dialog = new TimedownDialog(mContext);
                dialog.initView();
                dialog.setData(50000);
                dialog.setNoOnclickListener(new TimedownDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        dialog.dismiss();
                    }
                });
                dialog.setYesOnclickListener(new TimedownDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick(int hour, int minute) {
                        tv_setting_timing.setText(hour + ":" + minute);
                        if(channel==1){

                            Intent intent = new Intent(mContext, TimedownService.class);
                            intent.putExtra("time1", hour * 3600+ minute * 60);
                            startService(intent);
                        }else if(channel==2){
                            Intent intent = new Intent(mContext, Timedown2Service.class);
                            intent.putExtra("time2", hour * 3600+ minute * 60);
                            startService(intent);
                        }else if(channel==3){
                            Intent intent = new Intent(mContext, Timedown3Service.class);
                            intent.putExtra("time3", hour * 3600+ minute * 60);
                            startService(intent);
                        }else if(channel==4){
                            Intent intent = new Intent(mContext, Timedown4Service.class);
                            intent.putExtra("time4", hour * 3600+ minute * 60);
                            startService(intent);
                        }
                        ChannelActivity.writeData(APIData.setTiming_data(hour*3600+minute*60, (byte) channel));
                        dialog.dismiss();
                    }
                });
                dialog.show();

                break;
            case R.id.rl_setting_remindersetting:
                final ReminderDialog dialog1 = new ReminderDialog(mContext);
                dialog1.setOnNoOnclickListener(new ReminderDialog.onNoOnclickListener() {
                    @Override
                    public void onNoClick() {
                        if (dialog1.mp!=null) dialog1.mp.stop();
                        dialog1.dismiss();
                    }
                });
                dialog1.setOnYesOnclickListener(new ReminderDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick(int location) {
                        if (dialog1.mp!=null) dialog1.mp.stop();
                        sound_location=location;
                        tv_setting_remindersetting.setText(fields[location].getName());
                        dialog1.dismiss();
                    }
                });
                dialog1.show();
                break;
            case R.id.rl_setting_tempreminder:
                saveState();
                Intent intent=new Intent(mContext,TempReminderActivity.class);
                intent.putExtra("channel",channel);
                startActivity(intent);
                break;
            case R.id.iv_setting_back:
                saveState();
                startActivity(new Intent(mContext,ChannelActivity.class));
                break;
        }
    }

    @Override
    public void positiveListener() {
    }

    @Override
    public void negativeListener() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        mBroadcastReceiver = new mBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();

        // 2. 设置接收广播的类型
        intentFilter.addAction("com.project.moli.demobroad.MyService");

        // 3. 动态注册：调用Context的registerReceiver（）方法
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveState();
        unregisterReceiver(mBroadcastReceiver);
        Intent intent=new Intent();
        intent.putExtra("change",1);
        intent.setAction("com.project.moli.demobroad.MyService");
        sendBroadcast(intent);
    }

    // 继承BroadcastReceivre基类
    public class mBroadcastReceiver extends BroadcastReceiver {

        // 复写onReceive()方法
        // 接收到广播后，则自动调用该方法
        @Override
        public void onReceive(Context context, Intent intent) {
            //写入接收广播后的操作
            int time=intent.getIntExtra("time1",-1);
            int time2=intent.getIntExtra("time2",-1);
            int time3=intent.getIntExtra("time3",-1);
            int time4=intent.getIntExtra("time4",-1);
            int change=intent.getIntExtra("change",0);
            if(channel==1){
                if(time==-1){
                   // tv_setting_timing.setText("");
                }else{
                    ChanneloneFragment.tv_channelone_timedown.setText(switchHms(time)+"");
                    tv_setting_timing.setText(switchHms(time)+"");
                }
            }else if(channel==2){
                if(time2==-1){
                   // tv_setting_timing.setText("");
                }
                else{
                    ChanneltwoFragment.tv_channeltwo_timedown.setText(switchHms(time2)+"");
                    tv_setting_timing.setText(switchHms(time2)+"");
                }
            }else if(channel==3){
                if(time3==-1){
                    //tv_setting_timing.setText("");
                }
                else{
                    ChannelthreeFragment.tv_channelthree_timedown.setText(switchHms(time3)+"");
                    tv_setting_timing.setText(switchHms(time3)+"");
                }
            }else if(channel==4){
                if(time4==-1){
                   // tv_setting_timing.setText("");
                }
                else{
                    ChannelfourFragment.tv_channelfour_timedown.setText(switchHms(time4)+"");
                    tv_setting_timing.setText(switchHms(time4)+"");
                }
            }
            if(change==1){
               setunit();
            }
        }
    }

    private void unitState(){
        switch(channel){
            case 1:
                unit_location=BluetoothActivity.temp_unit01;
                sound_location=BluetoothActivity.sound_reminder01;
                break;
            case 2:
                unit_location=BluetoothActivity.temp_unit02;
                sound_location=BluetoothActivity.sound_reminder02;
                break;
            case 3:
                unit_location =BluetoothActivity.temp_unit03;
                sound_location=BluetoothActivity.sound_reminder03;
                break;
            case 4:
                unit_location=BluetoothActivity.temp_unit04;
                sound_location=BluetoothActivity.sound_reminder04;
                break;
        }
    }

    private void saveState(){
        switch(channel){
            case 1:
                BluetoothActivity.temp_unit01=unit_location;
                BluetoothActivity.sound_reminder01=sound_location;
                break;
            case 2:
                BluetoothActivity.temp_unit02=unit_location;
                BluetoothActivity.sound_reminder02=sound_location;
                break;
            case 3:
                BluetoothActivity.temp_unit03=unit_location;
                BluetoothActivity.sound_reminder03=sound_location;
                break;
            case 4:
                BluetoothActivity.temp_unit04=unit_location;
                BluetoothActivity.sound_reminder04=sound_location;
                break;
        }
    }

    private void setunit(){
        switch(channel){
            case 1:
                unit_location=BluetoothActivity.temp_unit01;
                if(BluetoothActivity.temp_unit01==0){
                    tv_setting_unit.setText("℃");
                }else{
                    tv_setting_unit.setText("℉");
                }
                break;
            case 2:
                unit_location=BluetoothActivity.temp_unit02;
                if(BluetoothActivity.temp_unit02==0){
                    tv_setting_unit.setText("℃");
                }else{
                    tv_setting_unit.setText("℉");
                }
                break;
            case 3:
                unit_location=BluetoothActivity.temp_unit03;
                if(BluetoothActivity.temp_unit03==0){
                    tv_setting_unit.setText("℃");
                }else{
                    tv_setting_unit.setText("℉");
                }
                break;
            case 4:
                unit_location=BluetoothActivity.temp_unit04;
                if(BluetoothActivity.temp_unit04==0){
                    tv_setting_unit.setText("℃");
                }else{
                    tv_setting_unit.setText("℉");
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

         startActivity(new Intent(mContext,ChannelActivity.class));
        //return super.onKeyDown(keyCode, event);
        return false;
    }

    public String switchHms(int time) {
        int hour = time / 3600;
        int minute = time % 3600 / 60;
        int second = time % 3600 % 60;
        String hourS;
        String minuteS;
        String secondS;
        if (hour < 10) {
            hourS = "0" + hour;
        } else {
            hourS = hour + "";
        }
        if (minute < 10) {
            minuteS = "0" + minute;
        } else {
            minuteS = "" + minute;
        }
        if (second < 10) {
            secondS = "0" + second;
        } else {
            secondS = "" + second;
        }
        return hourS + ":" + minuteS + ":" + secondS + "";
    }
}
