package com.example.benetech.bbq;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.benetech.bbq.base.mBaseActivity;
import com.example.benetech.bbq.wight.PickerView;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;



public class ReminderActivity extends mBaseActivity implements View.OnClickListener {

    private ImageView iv_reminder_back;
    private TextView tv_reminder_play;
    private PickerView pv_reminder_options;
    private List<String> mDatas = new ArrayList<>();
    private String mSelected;
    private int rawId;
    private String rawName;
    private Field[] fields;
    private MediaPlayer mp;
    private Context context;

    /* 自定义的类型 */
    public static final int ButtonRingtone = 0;
    public static final int ButtonAlarm = 1;
    public static final int ButtonNotification = 2;
    /* 铃声文件夹 */
    private String strRingtoneFolder = "/sdcard/music/ringtones";
    private String strAlarmFolder = "/sdcard/music/alarms";
    private String strNotificationFolder = "/sdcard/music/notifications";

    @Override
    protected int getLayoutID() {
        return R.layout.activity_reminder;
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        iv_reminder_back = findViewById(R.id.iv_reminder_back);
        pv_reminder_options = findViewById(R.id.pv_tempreminder_options);
        tv_reminder_play=findViewById(R.id.tv_reminder_play);
        context=this;
        tv_reminder_play.setOnClickListener(this);
        fields = R.raw.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            try {
                rawId = fields[i].getInt(R.raw.class);
                rawName = fields[i].getName();
                mDatas.add(rawName);
                Log.e("TAG", "onClick:1111111 "+rawName);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        pv_reminder_options.setData(mDatas);
        pv_reminder_options.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                if(mp!=null){
                    mp.pause();
                }
                mSelected=text;
                for(int i=0;i<fields.length;i++){
                    if(mSelected==fields[i].getName()){
                        try {
                            rawId=fields[i].getInt(R.raw.class);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
                mp=MediaPlayer.create(context,rawId);
                mp.start();
            }
        });
        //MediaPlayer mp=MediaPlayer.create(this,R.raw.alarm1);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_reminder_play:
                for(int i=0;i<fields.length;i++){
                    if(mSelected==fields[i].getName()){
                        try {
                            rawId=fields[i].getInt(R.raw.class);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
                mp=MediaPlayer.create(this,rawId);
                mp.start();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mp!=null){
            mp.stop();
        }
    }

    /* 当设置铃声之后的回调函数 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case ButtonRingtone:
                try {
                    //得到我们选择的铃声
                    Uri pickedUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    //将我们选择的铃声设置成为默认
                    if (pickedUri != null) {
                        RingtoneManager.setActualDefaultRingtoneUri(ReminderActivity.this, RingtoneManager.TYPE_RINGTONE, pickedUri);
                    }
                } catch (Exception e) {
                }
                break;
            case ButtonAlarm:
                try {
                    //得到我们选择的铃声
                    Uri pickedUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    //将我们选择的铃声设置成为默认
                    if (pickedUri != null) {
                        RingtoneManager.setActualDefaultRingtoneUri(ReminderActivity.this, RingtoneManager.TYPE_ALARM, pickedUri);
                    }
                } catch (Exception e) {
                }
                break;
            case ButtonNotification:
                try {
                    //得到我们选择的铃声
                    Uri pickedUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    //将我们选择的铃声设置成为默认
                    if (pickedUri != null) {
                        RingtoneManager.setActualDefaultRingtoneUri(ReminderActivity.this, RingtoneManager.TYPE_NOTIFICATION, pickedUri);
                    }
                } catch (Exception e) {
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //检测是否存在指定的文件夹
    //如果不存在则创建
    private boolean bFolder(String strFolder) {
        boolean btmp = false;
        File f = new File(strFolder);
        if (!f.exists()) {
            if (f.mkdirs()) {
                btmp = true;
            } else {
                btmp = false;
            }
        } else {
            btmp = true;
        }
        return btmp;
    }

    public void playone() {

        if (bFolder(strRingtoneFolder))
        {
            //打开系统铃声设置
            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            //类型为来电RINGTONE
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
            //设置显示的title
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "设置来电铃声");
            //当设置完成之后返回到当前的Activity
            startActivityForResult(intent, ButtonRingtone);
        }
    }

    public void playtwo() {
        if (bFolder(strAlarmFolder)) {
            //打开系统铃声设置
            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            //设置铃声类型和title
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.EXTRA_RINGTONE_TYPE);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "设置闹铃铃声");
            //当设置完成之后返回到当前的Activity
            startActivityForResult(intent, ButtonAlarm);
        }
    }

    public void playthree() {
        if (bFolder(strNotificationFolder)) {
            //打开系统铃声设置
            Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
            //设置铃声类型和title
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION);
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "设置通知铃声");
            //当设置完成之后返回到当前的Activity
            startActivityForResult(intent, ButtonNotification);
        }
    }
}

