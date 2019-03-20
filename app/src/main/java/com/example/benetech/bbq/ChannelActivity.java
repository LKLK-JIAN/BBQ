package com.example.benetech.bbq;


import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.MenuPopupWindow;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.benetech.bbq.adapter.MyViewPagerAdapter;
import com.example.benetech.bbq.base.mBaseActivity;
import com.example.benetech.bbq.bluetoothapi.APIData;
import com.example.benetech.bbq.bluetoothapi.BluetooehAttribute;
import com.example.benetech.bbq.channel.AllChannelFragment;
import com.example.benetech.bbq.channel.ChannelfourFragment;
import com.example.benetech.bbq.channel.ChanneloneFragment;
import com.example.benetech.bbq.channel.ChannelthreeFragment;
import com.example.benetech.bbq.channel.ChanneltwoFragment;
import com.example.benetech.bbq.dialog.ConnectDialog;
import com.example.benetech.bbq.dialog.CustomProgressDialog;
import com.example.benetech.bbq.view.MyViewPager;
import com.example.benetech.bbq.wight.ToastUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class ChannelActivity extends mBaseActivity {

    private RelativeLayout rl_channel_one;
    private RelativeLayout rl_channel_two;
    private RelativeLayout rl_channel_three;
    private RelativeLayout rl_channel_four;
    private ImageView iv_channel_back;
    public static BluetoothGatt bluetoothGatt;
    public static BluetoothDevice device;
    public static BluetoothGattCharacteristic writeCharacteristic;
    public static BluetoothGattCharacteristic notificationCharacteristic;
    //通道数据
    private TextView tv_channelone_value, tv_channeltwo_value, tv_channelthree_value, tv_channelfour_value;
    //定时器
    public static Timer timer = null;
    public static TimerTask task = null;
    public static boolean isConnect = false;
    public static boolean isStart;
    //连接蓝牙
    private ListView bluetooth_list;
    private ConnectDialog mConnectDialog;
    //加载框
    private CustomProgressDialog mCustomProgress;
    private MenuPopupWindow menuPopupWindow;
    //返回数据类型
    public static String mBackDataType;
    public static int save_type;
    public static Handler handler;
    //发送频率
    private int mSample = 2000;

    public static MyViewPager vp_channel_content;
    private MyViewPagerAdapter pagerAdapter;
    private List<Fragment> fragments = null;

    //all fragment
    private AllChannelFragment allChannelFragment;
    private ChanneloneFragment channeloneFragment;
    private ChanneltwoFragment channeltwoFragment;
    private ChannelthreeFragment channelthreeFragment;
    private ChannelfourFragment channelfourFragment;

    public static int battery;


    @Override
    protected int getLayoutID() {
        return R.layout.activity_channel;
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        init();
    }

    private void init() {
        vp_channel_content = findViewById(R.id.vp_channel_content);
        fragments = new ArrayList<Fragment>();
        pagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(),
                fragments);
        vp_channel_content.setOffscreenPageLimit(5);
        vp_channel_content.setAdapter(pagerAdapter);
        allChannelFragment = new AllChannelFragment();
        channeloneFragment = new ChanneloneFragment();
        channeltwoFragment = new ChanneltwoFragment();
        channelthreeFragment = new ChannelthreeFragment();
        channelfourFragment = new ChannelfourFragment();
        fragments.add(allChannelFragment);
        fragments.add(channeloneFragment);
        fragments.add(channeltwoFragment);
        fragments.add(channelthreeFragment);
        fragments.add(channelfourFragment);
        pagerAdapter.notifyDataSetChanged();
        setCurrentPage(0);
        Log.e(TAG, "init:000000000000 " + vp_channel_content.getCurrentItem());

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 0x123:
                        ToastUtils.showToast(mContext, getResources().getString(R.string.connect_success));
                        if (timer == null) {
                            timer = new Timer();
                        }
                        if (task == null) {
                            task = new TimerTask() {
                                @Override
                                public void run() {
                                    writeData(APIData.getAllChannelTemp());
                                }
                            };
                            Log.e(TAG, "onClick: ============" + mSample);
                            timer.schedule(task, 10, mSample);
                        }

                        break;
                    case 0x456:
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }
                        if (task != null) {
                            task.cancel();
                            task = null;
                        }
                        if (bluetoothGatt != null && notificationCharacteristic != null) {
                            bluetoothGatt.disconnect();
                            bluetoothGatt.setCharacteristicNotification(
                                    notificationCharacteristic, false);
                        }
                        startActivity(new Intent(mContext, BluetoothActivity.class));
                        break;
                    //连接中断
                    case 0x404:
                        break;
                }
                return true;
            }
        });
    }

    public static void setCurrentPage(int pageIndex) {
        vp_channel_content.setCurrentItem(pageIndex);
    }

    private BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {

        /**
         * 返回链接状态
         */
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);

            if (newState == BluetoothProfile.STATE_CONNECTED) {
                //连接成功
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isConnect = true;

                    }
                });
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                //断开连接
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtils.showToast(mContext, getResources().getString(R.string.ftemp_disconnect));
                        isConnect = false;
                        isStart = false;
                        Message message = new Message();
                        message.what = 0x456;
                        handler.sendMessage(message);

                    }
                });
            }
        }

        /**
         * 获取到链接设备的GATT服务时的回调
         */
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (BluetoothGatt.GATT_SUCCESS == status) {
                gatt.getServices();
                String uuid = null;
                ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
                ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();
                for (BluetoothGattService gattService : gatt.getServices()) {
                    HashMap<String, String> currentServiceData = new HashMap<String, String>();
                    uuid = gattService.getUuid().toString();
                    Log.e(TAG, "onServicesDiscovered+gattService: " + uuid);
                    currentServiceData.put("uuid", uuid);
                    gattServiceData.add(currentServiceData);
                    ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();
                    List<BluetoothGattCharacteristic> gattCharacteristics = gattService
                            .getCharacteristics();
                    ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();
                    for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                        // 遍历每条服务里的所有Characteristic,保存所需Characteristics
                        Log.d(TAG, "characteristics uuid: " + gattCharacteristic.getUuid());
                        if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(
                                BluetooehAttribute.Notification_charateristic)) {
                            //接收数据characteristic
                            notificationCharacteristic = gattCharacteristic;
                            //设置notification通知，按键回调在onCharacteristicChanged
                            //以下代码一定不能少，否则将无法接收到设备发送的数据
                            boolean isEnableNotification = bluetoothGatt.setCharacteristicNotification(
                                    notificationCharacteristic, true);
                            if (isEnableNotification) {

                                BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(UUID
                                        .fromString(BluetooehAttribute.mluetoothGattDescriptor));
                                if (descriptor != null) {
                                    System.out.println("write descriptor");
                                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                    bluetoothGatt.writeDescriptor(descriptor);
                                }
                            }
                        }
                        if (gattCharacteristic.getUuid().toString().equalsIgnoreCase(
                                BluetooehAttribute.Write_charateristic)) {
                            //写数据characteristic
                            writeCharacteristic = gattCharacteristic;
                        }
                    }
                }
                writeData(APIData.getChannelquantity());
                Message message = new Message();
                message.what = 0x123;
                handler.sendMessage(message);
            }
        }

        /**
         * 设备发出通知时会调用到该接口
         */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            final byte[] data = characteristic.getValue();
            Log.e(TAG, "onCharacteristicChanged: "
                    + characteristic.getValue().length);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mBackDataType.equals("allchanneltemp")) {
                        StringBuffer stringbuffer = new StringBuffer();
                        for (byte i : data) {
                            stringbuffer.append(i + "||");
                        }
                        if(data[2]==6){
                            if(data[6]==2){
                                AllChannelFragment.rl_channel_four.setVisibility(View.GONE);
                                AllChannelFragment.rl_channel_three.setVisibility(View.GONE);
                            }
                            else if(data[6]==4){
                                AllChannelFragment.rl_channel_four.setVisibility(View.VISIBLE);
                                AllChannelFragment.rl_channel_three.setVisibility(View.VISIBLE);
                            }
                        }
                        if(data[3]==1){
                        short channelone = (short) ((data[7] << 8) | (data[6] & 0xff));
                        short channeltwo = (short) ((data[9] << 8) | (data[8] & 0xff));
                        short channelthree = (short) ((data[11] << 8) | (data[10] & 0xff));
                        short channelfour = (short) ((data[13] << 8) | (data[12] & 0xff));
                        battery=data[14];
                        Log.e(TAG, "run: " + stringbuffer.toString());
                        Log.e(TAG, "run: " + channelone + "+" + channeltwo + "+" + channelthree + "+" + channelfour);
                        allChannelFragment.setTemp(channelone, channeltwo, channelthree, channelfour);
                        channeloneFragment.setTemp(channelone);
                        channeltwoFragment.setTemp(channeltwo);
                        channelthreeFragment.setTemp(channelthree);
                        channelfourFragment.setTemp(channelfour);}
                    } else if (mBackDataType.equals("")) {
                    } else if (mBackDataType.equals("")) {
                    } else if (mBackDataType.equals("")) {
                    } else if (mBackDataType.equals("")) {
                    } else if (mBackDataType.equals("")) {
                    } else if (mBackDataType.equals("")) {
                    } else if (mBackDataType.equals("")) {
                    } else {
                    }
                }
            });
        }
    };

    //根据writeCharateristic写入数据
    public static void writeData(byte[] bytes) {
        if (bluetoothGatt != null && writeCharacteristic != null) {
            Log.e(TAG, "writeData: ");
            writeCharacteristic.setValue(bytes);
            writeCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
            bluetoothGatt.writeCharacteristic(writeCharacteristic);
        }
    }

    public static void endToFinish() {
        Message message = new Message();
        message.what = 0x456;
        handler.sendMessage(message);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (vp_channel_content.getCurrentItem() > 0) {
            setCurrentPage(0);
            return false;
        } else {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            if (task != null) {
                task.cancel();
                task = null;
            }
            if (bluetoothGatt != null && notificationCharacteristic != null) {
                bluetoothGatt.disconnect();
                bluetoothGatt.setCharacteristicNotification(
                        notificationCharacteristic, false);
            }
            startActivity(new Intent(mContext, BluetoothActivity.class));
            return false;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isConnect) {
            Intent intent = getIntent();
            String address = intent.getStringExtra("address");
            device = bluetoothAdapter.getRemoteDevice(address);
            Log.e(TAG, "7777777777777" + address);
            bluetoothGatt = device.connectGatt(mContext, false, bluetoothGattCallback);
        }

//        mBroadcastReceiver = new mBroadcastReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        // 2. 设置接收广播的类型
//        intentFilter.addAction("com.project.moli.demobroad.MyService");
//        // 3. 动态注册：调用Context的registerReceiver（）方法
//        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        // unregisterReceiver(mBroadcastReceiver);

    }

    // 继承BroadcastReceivre基类
    public class mBroadcastReceiver extends BroadcastReceiver {

        // 复写onReceive()方法
        // 接收到广播后，则自动调用该方法
        @Override
        public void onReceive(Context context, Intent intent) {
            //写入接收广播后的操作
            int time=intent.getIntExtra("time1",0);
            int time2=intent.getIntExtra("time2",0);
            int time3=intent.getIntExtra("time3",0);
            int time4=intent.getIntExtra("time4",0);
            Log.e(TAG, "onReceive: 8888888channel"+time+time2+time3+time4 );
        }
    }
}
