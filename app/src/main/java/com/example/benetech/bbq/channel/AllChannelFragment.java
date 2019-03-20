package com.example.benetech.bbq.channel;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.benetech.bbq.BluetoothActivity;
import com.example.benetech.bbq.ChannelActivity;
import com.example.benetech.bbq.R;

import static android.content.ContentValues.TAG;

public class AllChannelFragment extends Fragment implements View.OnClickListener {

    private View v;
    //通道数据
    private TextView tv_allchannelone_value, tv_allchanneltwo_value, tv_allchannelthree_value, tv_allchannelfour_value;

    private RelativeLayout rl_channel_one;
    private RelativeLayout rl_channel_two;
    public static RelativeLayout rl_channel_three;
    public static RelativeLayout rl_channel_four;
    private ImageView iv_channel_back;
    private Context mContext;
    private MediaPlayer mp;

    //时间倒计时
    private TextView tv_allchannel1_time, tv_allchannel2_time, tv_allchannel3_time, tv_allchannel4_time;

    public mBroadcastReceiver mBroadcastReceiver;

    private short limitTemp;
    private String food;
    private String level;

    //食物
    private String[] foodarray, beefarray, lambarray, vealarray, welldonearray;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_allchannel, null);
        mContext = v.getContext();
        tv_allchannelone_value = v.findViewById(R.id.tv_allchannelone_value);
        tv_allchanneltwo_value = v.findViewById(R.id.tv_allchanneltwo_value);
        tv_allchannelthree_value = v.findViewById(R.id.tv_allchannelthree_value);
        tv_allchannelfour_value = v.findViewById(R.id.tv_allchannelfour_value);
        iv_channel_back = v.findViewById(R.id.iv_channel_back);
        iv_channel_back.setOnClickListener(this);
        rl_channel_one = v.findViewById(R.id.rl_channel_one);
        rl_channel_one.setOnClickListener(this);
        rl_channel_two = v.findViewById(R.id.rl_channel_two);
        rl_channel_two.setOnClickListener(this);
        rl_channel_three = v.findViewById(R.id.rl_channel_three);
        rl_channel_three.setOnClickListener(this);
        rl_channel_four = v.findViewById(R.id.rl_channel_four);
        rl_channel_four.setOnClickListener(this);
        tv_allchannel1_time = v.findViewById(R.id.tv_allchannel1_time);
        tv_allchannel2_time = v.findViewById(R.id.tv_allchannel2_time);
        tv_allchannel3_time = v.findViewById(R.id.tv_allchannel3_time);
        tv_allchannel4_time = v.findViewById(R.id.tv_allchannel4_time);
        Resources res = getResources();
        foodarray = res.getStringArray(R.array.food);
        beefarray = res.getStringArray(R.array.beef);
        welldonearray = res.getStringArray(R.array.pork);
        return v;
    }

    public void setTemp(short temp1, short temp2, short temp3, short temp4) {
        if (temp1 != 32767) {
            //rl_channel_one.setClickable(true);
            if (BluetoothActivity.temp_unit01 == 0) {
                float tempc= (float) (temp1*0.1);
                tv_allchannelone_value.setText(String.format("%.1f",tempc)+ "℃");
            } else {
                float tempf= (float) (temp1*0.1*1.8+32);
                tv_allchannelone_value.setText(String.format("%.1f",tempf) + "℉");
            }
        } else {
            //rl_channel_one.setClickable(false);
            tv_allchannelone_value.setText("------");
        }
        if (temp2 != 32767) {
            //rl_channel_two.setClickable(true);
            if (BluetoothActivity.temp_unit02 == 0) {
                float tempc= (float) (temp2*0.1);
                tv_allchanneltwo_value.setText(String.format("%.1f",tempc) + "℃");
            } else {
                float tempf= (float) (temp2*0.1*1.8+32);
                tv_allchanneltwo_value.setText(String.format("%.1f",tempf) + "℉");
            }
        } else {
           // rl_channel_two.setClickable(false);
            tv_allchanneltwo_value.setText("------");
        }
        if (temp3 != 32767) {
            //rl_channel_three.setClickable(true);
            if (BluetoothActivity.temp_unit03 == 0) {
                float tempc= (float) (temp3*0.1);
                tv_allchannelthree_value.setText(String.format("%.1f",tempc) + "℃");
            } else {
                float tempf= (float) (temp3*0.1*1.8+32);
                tv_allchannelthree_value.setText(String.format("%.1f",tempf)+ "℉");
            }
        } else {
            //rl_channel_three.setClickable(false);
            tv_allchannelthree_value.setText("------");
        }
        if (temp4 != 32767) {
           // rl_channel_four.setClickable(true);
            if (BluetoothActivity.temp_unit04 == 0) {
                float tempc= (float) (temp4*0.1);
                tv_allchannelfour_value.setText(String.format("%.1f",tempc) + "℃");
            } else {
                float tempf= (float) (temp4*0.1*1.8+32);
                tv_allchannelfour_value.setText(String.format("%.1f",tempf) + "℉");
            }
        } else {
           // rl_channel_four.setClickable(false);
            tv_allchannelfour_value.setText("------");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_channel_one:
                ChannelActivity.setCurrentPage(1);
                break;
            case R.id.rl_channel_two:
                ChannelActivity.setCurrentPage(2);
                break;
            case R.id.rl_channel_three:
                ChannelActivity.setCurrentPage(3);
                break;
            case R.id.rl_channel_four:
                ChannelActivity.setCurrentPage(4);
                break;
            case R.id.iv_channel_back:
                ChannelActivity.endToFinish();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        mBroadcastReceiver = new mBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        // 2. 设置接收广播的类型
        intentFilter.addAction("com.project.moli.demobroad.MyService");
        // 3. 动态注册：调用Context的registerReceiver（）方法
        getActivity().registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mBroadcastReceiver);
    }

    // 继承BroadcastReceivre基类
    public class mBroadcastReceiver extends BroadcastReceiver {

        // 复写onReceive()方法
        // 接收到广播后，则自动调用该方法
        @Override
        public void onReceive(Context context, Intent intent) {
            //写入接收广播后的操作
            int time = intent.getIntExtra("time1", -1);
            int time2 = intent.getIntExtra("time2", -1);
            int time3 = intent.getIntExtra("time3", -1);
            int time4 = intent.getIntExtra("time4", -1);
            if (time == -1) {
                // tv_allchannel1_time.setText("");
            } else {
                tv_allchannel1_time.setText(switchHms(time));
            }
            if (time2 == -1) {
                // tv_allchannel2_time.setText("");
            } else {
                tv_allchannel2_time.setText(switchHms(time2));
            }
            if (time3 == -1) {
                //tv_allchannel3_time.setText("");
            } else {
                tv_allchannel3_time.setText(switchHms(time3));
            }
            if (time4 == -1) {
                // tv_allchannel4_time.setText("");
            } else {
                tv_allchannel4_time.setText(switchHms(time4));
            }

            if (time == 0) {
                try {
                    showDialog(mContext.getResources().getString(R.string.channel_one), BluetoothActivity.sound_reminder01);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (time2 == 0) {
                try {
                    showDialog(mContext.getResources().getString(R.string.channel_two), BluetoothActivity.sound_reminder02);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (time3 == 0) {
                try {
                    showDialog(mContext.getResources().getString(R.string.channel_three), BluetoothActivity.sound_reminder03);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (time4 == 0) {
                try {
                    showDialog(mContext.getResources().getString(R.string.channel_four), BluetoothActivity.sound_reminder04);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            Log.e(TAG, "onReceive: 8888888channel" + time + time2 + time3 + time4);
        }
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

    private void showDialog(String channel, int location) throws IllegalAccessException {
        mp = MediaPlayer.create(mContext, BluetoothActivity.fields[location].getInt(R.raw.class));
        mp.start();
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(channel);
        builder.setMessage(mContext.getResources().getString(R.string.time_down));
        builder.setPositiveButton(mContext.getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                mp.stop();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
