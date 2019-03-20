package com.example.benetech.bbq.bluetoothapi;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Looper;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by android on 2018/4/14.
 */

public class APIBuletooth {

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothManager manager;
    public  static APIBuletooth instance;
    protected BluetoothGatt bluetoothGatt;
    protected BluetoothGattCharacteristic writeCharacteristic;
    public BluetoothGattCharacteristic notificationCharacteristic;
    public  Context mContext;
    protected boolean isScan = true;//是否开始扫描
    protected boolean isScanning = false;//是否正在扫描
    protected android.os.Handler handler = new android.os.Handler(Looper.myLooper());


    public APIBuletooth(Context context) {
        Log.e(TAG, "APIBuletooth:88888888 " +context);
        mContext=context.getApplicationContext();
        Log.e(TAG, "APIBuletooth:888888888888888 "+mContext );
        manager = (BluetoothManager)mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = manager.getAdapter();
    }

    //单例
    public static APIBuletooth getInstance(Context context) {
        if (instance == null) {
                instance = new APIBuletooth(context);
        }
        return instance;
    }

    //蓝牙状态
    public boolean isOpen() {
        if(bluetoothAdapter!=null){
            return bluetoothAdapter.isEnabled();
        }
        return false;
    }

    //打开蓝牙
    public void bleOpen(){
        bluetoothAdapter.enable();
    }

    //关闭蓝牙
    public void bleClose(){
        bluetoothAdapter.disable();
    }

    //关闭bluetoothGatt
    public  void closeBluetoothGatt(){
        bluetoothGatt.close();
    }

    //打开蓝牙
    public void bluetoothOpen() {
        if (!isOpen())
            bluetoothAdapter.enable();
    }

    //扫描蓝牙设备
    public void scanBLE(final BluetoothAdapter.LeScanCallback leScanCallback, int outTime) {
        if (isScan) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    bluetoothAdapter.stopLeScan(leScanCallback);
                }
            }, outTime);
            bluetoothAdapter.startLeScan(leScanCallback);
        } else {
            bluetoothAdapter.stopLeScan(leScanCallback);
        }
    }

    //根据writeCharateristic写入数据
    public void writeData(byte[] bytes) {
        if (bluetoothGatt != null && writeCharacteristic != null) {
            Log.e(TAG, "writeData: ");
            writeCharacteristic.setValue(bytes);
            writeCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
            bluetoothGatt.writeCharacteristic(writeCharacteristic);
        }
    }
}
