package com.example.benetech.bbq;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.example.benetech.bbq.adapter.FragAdapter;
import com.example.benetech.bbq.base.mBaseActivity;
import com.example.benetech.bbq.bluetoothapi.BluetooehAttribute;
import com.example.benetech.bbq.dialog.ConnectDialog;
import com.example.benetech.bbq.dialog.CustomProgressDialog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import static android.content.ContentValues.TAG;

public class MainActivity extends mBaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    public static BluetoothGatt bluetoothGatt;
    public static BluetoothDevice device;
    public static BluetoothGattCharacteristic writeCharacteristic;
    public static BluetoothGattCharacteristic notificationCharacteristic;
    //定时器
    public static Timer timer;
    public static TimerTask task;
    public static boolean isConnected;
    //连接蓝牙
    private ListView bluetooth_list;
    private ConnectDialog mConnectDialog;
    //加载框
    private CustomProgressDialog mCustomProgress;

    //fragment
   // private List<Fragment> fragments;
    private ViewPager vp_main;
    private FragAdapter pagerAdapter;
    private RadioButton rb_temp,rb_record;
    private RadioGroup rg_tab_bar;

    private Boolean isConnect=false;



    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        init();
    }

    //显示连接dialog
    protected void showConnect() {
        mConnectDialog = new ConnectDialog(mContext, R.style.DialogTheme);
        bluetooth_list = mConnectDialog.getListView();
        bluetooth_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mCustomProgress = CustomProgressDialog.createDialog(mContext);
                mCustomProgress.setMessage(getResources().getString(R.string.connect_dialog));
                mCustomProgress.setCanceledOnTouchOutside(false);// 设置点击屏幕progressDialog不消失
                mCustomProgress.show();
                String address = mConnectDialog.getList().get(i).getAddress();
                device = bluetoothAdapter.getRemoteDevice(address);
                bluetoothGatt = device.connectGatt(mContext, true, bluetoothGattCallback);
            }
        });
    }

    protected void init(){
        //fragments=new ArrayList<>();
        vp_main=findViewById(R.id.vp_mian);
        rb_record=findViewById(R.id.rb_record);
        rb_temp=findViewById(R.id.rb_temp);
        rg_tab_bar=findViewById(R.id.rg_tab_bar);
        rg_tab_bar.setOnCheckedChangeListener(this);
        pagerAdapter=new FragAdapter(getSupportFragmentManager());
        vp_main.setAdapter(pagerAdapter);
        vp_main.setCurrentItem(0);
        vp_main.addOnPageChangeListener(this);
        rb_record.setChecked(true);
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
                isConnect=true;
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                isConnect=false;
                //断开连接
            }
        }

        /**
         * 获取到链接设备的GATT服务时的回调
         */
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (BluetoothGatt.GATT_SUCCESS == status) {
                //gatt.getServices();
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
            }
        }

        /**
         * 设备发出通知时会调用到该接口
         */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
        }
    };

    //根据writeCharateristic写入数据
    public void writeData(byte[] bytes) {
        if (bluetoothGatt != null && writeCharacteristic != null) {
            Log.e(TAG, "writeData: ");
            writeCharacteristic.setValue(bytes);
            writeCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
            bluetoothGatt.writeCharacteristic(writeCharacteristic);
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
           switch(checkedId){
               case R.id.rb_record:
                   vp_main.setCurrentItem(0);
                   break;
               case R.id.rb_temp:
                   vp_main.setCurrentItem(1);
                   break;
           }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
