package com.example.benetech.bbq;


import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.benetech.bbq.adapter.DialogListViewAdapter;
import com.example.benetech.bbq.base.mBaseActivity;
import com.example.benetech.bbq.bluetoothapi.APIBuletooth;
import com.example.benetech.bbq.service.Timedown2Service;
import com.example.benetech.bbq.service.Timedown3Service;
import com.example.benetech.bbq.service.Timedown4Service;
import com.example.benetech.bbq.service.TimedownService;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class BluetoothActivity extends mBaseActivity {

    private ListView lv_bluetooth;
    private TextView tv_bluetooth;
    private DialogListViewAdapter adapter;
    protected List<BluetoothDevice> list = new ArrayList<>();

    public SharedPreferences sp;
    private SharedPreferences.Editor editor;

    public static int temp_unit01;
    public static int set_type01;
    public static int preset_location01;
    public static int preset_location201;
    public static int temp_reminder01;
    public static int time_reminder01;
    public static int temp_manualc01;
    public static int temp_manualf01;
    public static int sound_reminder01;

    public static int temp_unit02;
    public static int set_type02;
    public static int preset_location02;
    public static int preset_location202;
    public static int temp_reminder02;
    public static int time_reminder02;
    public static int temp_manualc02;
    public static int temp_manualf02;
    public static int sound_reminder02;

    public static int temp_unit03;
    public static int set_type03;
    public static int preset_location03;
    public static int preset_location203;
    public static int temp_reminder03;
    public static int time_reminder03;
    public static int temp_manualc03;
    public static int temp_manualf03;
    public static int sound_reminder03;

    public static int temp_unit04;
    public static int set_type04;
    public static int preset_location04;
    public static int preset_location204;
    public static int temp_reminder04;
    public static int time_reminder04;
    public static int temp_manualc04;
    public static int temp_manualf04;
    public static int sound_reminder04;

    public static int rawId;
    public static String rawName;
    public static Field[] fields;
    public static ArrayList<String> data=new ArrayList<>();

    public static int[] foodf = {145, 145, 145, 160, 170, 180, 180, 140, 160, 160, 160, 160, 160, 165, 210, 190};
    public static int[] foodc = {63, 63, 63, 71, 77, 82, 82, 82, 60, 71, 71, 71, 71, 71, 74, 99, 88};
    public static int[][] levelc = {{63, 71, 77}, {63, 71, 77}, {63, 71, 77}, {71}, {77}, {82}, {82}, {82}
            , {60}, {71}, {71}, {71}, {71}, {71}, {74}, {99}, {88}};
    public static int[][] levelf = {{145, 160, 170}, {145, 160, 170}, {145, 160, 170}, {160}, {170}, {180},
              {180}, {180}, {140}, {160}, {160}, {160}, {160}, {160}, {165}, {210}, {190}};



    @Override
    protected int getLayoutID() {
        return R.layout.activity_bluetooth;
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        Log.e(TAG, "initEventAndData: 77777777777777是否是第一次" );
        init();
        //取消严格模式  FileProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        addPermissin1();
    }

    protected void init() {
        lv_bluetooth = findViewById(R.id.lv_bluetooth);
        tv_bluetooth = findViewById(R.id.tv_bluetooth);
        if (!APIBuletooth.getInstance(mContext).isOpen()) {
            APIBuletooth.getInstance(mContext).bleOpen();
        }
        adapter = new DialogListViewAdapter(mContext, list);
        lv_bluetooth.setAdapter(adapter);
        createData();
        tv_bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIBuletooth.getInstance(mContext).scanBLE(new BluetoothAdapter.LeScanCallback() {
                    @Override
                    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {

                        if (!list.contains(device)) {
                            //list.add(device);
                            adapter.addDevice(device);
                        }
                    }
                }, 5000);
            }
        });

        lv_bluetooth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String address = list.get(position).getAddress();
                Intent intent = new Intent(getBaseContext(), ChannelActivity.class);
                intent.putExtra("address", address);
                Log.e(TAG, "onItemClick:888888888888 " + address);
                startActivity(intent);
            }
        });
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
    }

    private void createData() {
        sp = getSharedPreferences("bbq", MODE_PRIVATE);
        editor = sp.edit();
        if (sp.getInt("temp_unit01", 404) == 404) {
            editor.putInt("temp_unit01", 0);  //0代表℃，1代表℉
        }
        if(sp.getInt("temp_unit02",404)==404){
            editor.putInt("temp_unit02",0);
        }
        if(sp.getInt("temp_unit03",404)==404){
            editor.putInt("temp_unit03",0);
        }
        if(sp.getInt("temp_unit04",404)==404){
            editor.putInt("temp_unit04",0);
        }
        if (sp.getInt("settemp_type01", 404) == 404) {
            editor.putInt("settemp_type01", 0); //0代表预设，1代表手动
        }
        if (sp.getInt("settemp_type02", 404) == 404) {
            editor.putInt("settemp_type02", 0); //0代表预设，1代表手动
        }
        if (sp.getInt("settemp_type03", 404) == 404) {
            editor.putInt("settemp_type03", 0); //0代表预设，1代表手动
        }
        if (sp.getInt("settemp_type04", 404) == 404) {
            editor.putInt("settemp_type04", 0); //0代表预设，1代表手动
        }
        if (sp.getInt("preset_location01", 404) == 404){
            editor.putInt("preset_location01", 0); //预设位置
        }
        if (sp.getInt("preset_location02", 404) == 404){
            editor.putInt("preset_location02", 0); //预设位置
        }
        if (sp.getInt("preset_location03", 404) == 404){
            editor.putInt("preset_location03", 0); //预设位置
        }
        if (sp.getInt("preset_location04", 404) == 404){
            editor.putInt("preset_location04", 0); //预设位置
        }
        if (sp.getInt("preset_location201", 404) == 404){
            editor.putInt("preset_location201", 0); //预设位置2
        }
        if (sp.getInt("preset_location202", 404) == 404){
            editor.putInt("preset_location202", 0); //预设位置2
        }
        if (sp.getInt("preset_location203", 404) == 404){
            editor.putInt("preset_location203", 0); //预设位置2
        }
        if (sp.getInt("preset_location204", 404) == 404){
            editor.putInt("preset_location204", 0); //预设位置2
        }
        if (sp.getInt("temp_reminder01", 404) == 404){
            editor.putInt("temp_reminder01", 88);
        }
        if (sp.getInt("temp_reminder02", 404) == 404){
            editor.putInt("temp_reminder02", 88);
        }
        if (sp.getInt("temp_reminder03", 404) == 404){
            editor.putInt("temp_reminder03", 88);
        }
        if (sp.getInt("temp_reminder04", 404) == 404){
            editor.putInt("temp_reminder04", 88);
        }
        if (sp.getInt("time_reminder01", 404) == 404){
            editor.putInt("time_reminder01", 60);
        }
        if (sp.getInt("time_reminder02", 404) == 404){
            editor.putInt("time_reminder02", 60);
        }
        if (sp.getInt("time_reminder03", 404) == 404){
            editor.putInt("time_reminder03", 60);
        }
        if (sp.getInt("time_reminder04", 404) == 404){
            editor.putInt("time_reminder04", 60);
        }
        if(sp.getInt("temp_manualc01",404) == 404){
            editor.putInt("temp_manualc01",100);
        }
        if(sp.getInt("temp_manualc02",404) == 404){
            editor.putInt("temp_manualc02",100);
        }
        if(sp.getInt("temp_manualc03",404) == 404){
            editor.putInt("temp_manualc03",100);
        }
        if(sp.getInt("temp_manualc04",404) == 404){
            editor.putInt("temp_manualc04",100);
        }
        if(sp.getInt("temp_manualf01",404) == 404){
            editor.putInt("temp_manualf01",100);
        }
        if(sp.getInt("temp_manualf02",404) == 404){
            editor.putInt("temp_manualf02",100);
        }
        if(sp.getInt("temp_manualf03",404) == 404){
            editor.putInt("temp_manualf03",100);
        }
        if(sp.getInt("temp_manualf04",404) == 404){
            editor.putInt("temp_manualf04",100);
        }
        if(sp.getInt("sound_reminder01",404)==404){
            editor.putInt("sound_reminder01",0);
        }
        if(sp.getInt("sound_reminder02",404)==404){
            editor.putInt("sound_remidner02",0);
        }
        if(sp.getInt("sound_reminder03",404)==404){
            editor.putInt("sound_reminder03",0);
        }
        if(sp.getInt("sound_reminder04",404)==404){
            editor.putInt("sond_reminder04",0);
        }

        editor.apply();

        temp_unit01 = sp.getInt("temp_unit01", 404);
        set_type01 = sp.getInt("settemp_type01", 404);
        preset_location01 = sp.getInt("preset_location01", 404);
        preset_location201 = sp.getInt("preset_location201", 404);
        temp_reminder01 = sp.getInt("temp_reminder01", 404);
        time_reminder01 = sp.getInt("time_reminder01", 404);
        temp_manualc01=sp.getInt("temp_manualc01",404);
        temp_manualf01=sp.getInt("temp_manualf01",404);
        sound_reminder01=sp.getInt("sound_reminder01",0);

        temp_unit02 = sp.getInt("temp_unit02", 404);
        set_type02 = sp.getInt("settemp_type02", 404);
        preset_location02 = sp.getInt("preset_location02", 404);
        preset_location202 = sp.getInt("preset_location202", 404);
        temp_reminder02 = sp.getInt("temp_reminder02", 404);
        time_reminder02= sp.getInt("time_reminder02", 404);
        temp_manualc02=sp.getInt("temp_manualc02",404);
        temp_manualf02=sp.getInt("temp_manualf02",404);

        sound_reminder02=sp.getInt("sound_reminder02",0);

        temp_unit03 = sp.getInt("temp_unit03", 404);
        set_type03 = sp.getInt("settemp_type03", 404);
        preset_location03 = sp.getInt("preset_location03", 404);
        preset_location203 = sp.getInt("preset_location203", 404);
        temp_reminder03 = sp.getInt("temp_reminder03", 404);
        time_reminder03 = sp.getInt("time_reminder03", 404);
        temp_manualc03=sp.getInt("temp_manualc03",404);
        temp_manualf03=sp.getInt("temp_manualf03",404);
        sound_reminder03=sp.getInt("sound_reminder03",0);

        temp_unit04 = sp.getInt("temp_unit04", 404);
        set_type04 = sp.getInt("settemp_type04", 404);
        preset_location04= sp.getInt("preset_location04", 404);
        preset_location204 = sp.getInt("preset_location204", 404);
        temp_reminder04 = sp.getInt("temp_reminder04", 404);
        time_reminder04 = sp.getInt("time_reminder04", 404);
        temp_manualc04=sp.getInt("temp_manualc04",404);
        temp_manualf04=sp.getInt("temp_manualf04",404);
        sound_reminder04=sp.getInt("sound_reminder04",0);
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "initEventAndData1123: 77777777777777是否是第一次" );
        super.onDestroy();

        editor.putInt("temp_unit01",    temp_unit01);  //0代表℃，1代表℉
        editor.putInt("temp_unit02",    temp_unit02);
        editor.putInt("temp_unit03",    temp_unit03);
        editor.putInt("temp_unit04",    temp_unit04);
        editor.putInt("settemp_type01", set_type01); //0代表预设，1代表手动
        editor.putInt("settemp_type02", set_type02); //0代表预设，1代表手动
        editor.putInt("settemp_type03", set_type03); //0代表预设，1代表手动
        editor.putInt("settemp_type04", set_type04); //0代表预设，1代表手动
        editor.putInt("preset_location01",  preset_location01);   //预设位置
        editor.putInt("preset_location02",  preset_location02);   //预设位置
        editor.putInt("preset_location03",  preset_location03);   //预设位置
        editor.putInt("preset_location04",  preset_location04);   //预设位置
        editor.putInt("preset_location201", preset_location201);  //预设位置2
        editor.putInt("preset_location202", preset_location201);  //预设位置2
        editor.putInt("preset_location203", preset_location203);  //预设位置2
        editor.putInt("preset_location204", preset_location04);   //预设位置2
        editor.putInt("temp_reminder01",    temp_reminder01);
        editor.putInt("temp_reminder02", temp_reminder02);
        editor.putInt("temp_reminder03", temp_reminder03);
        editor.putInt("temp_reminder04", temp_reminder04);
        editor.putInt("time_reminder01", time_reminder01);
        editor.putInt("time_reminder02", time_reminder02);
        editor.putInt("time_reminder03", time_reminder03);
        editor.putInt("time_reminder04", time_reminder04);
        editor.putInt("temp_manualc01",   temp_manualc01);
        editor.putInt("temp_manualc02",   temp_manualc02);
        editor.putInt("temp_manualc03",   temp_manualc03);
        editor.putInt("temp_manualc04",   temp_manualc04);
        editor.putInt("temp_manualf01",   temp_manualf01);
        editor.putInt("temp_manualf02",   temp_manualf02);
        editor.putInt("temp_manualf03",   temp_manualf03);
        editor.putInt("temp_manualf04",   temp_manualf04);
        editor.putInt("sound_reminder01",sound_reminder01);
        editor.putInt("sound_remidner02",sound_reminder02);
        editor.putInt("sound_reminder03",sound_reminder03);
        editor.putInt("sonnd_reminder04",sound_reminder04);
        editor.apply();
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //return super.onKeyDown(keyCode, event);
        return false;
    }

    private void  saveInitState(){
        editor.putInt("temp_unit01",    temp_unit01);             //0代表℃，1代表℉
        editor.putInt("temp_unit02",    temp_unit02);
        editor.putInt("temp_unit03",    temp_unit03);
        editor.putInt("temp_unit04",    temp_unit04);
        editor.putInt("settemp_type01",  set_type01);              //0代表预设，1代表手动
        editor.putInt("settemp_type02",  set_type02);              //0代表预设，1代表手动
        editor.putInt("settemp_type03",  set_type03);              //0代表预设，1代表手动
        editor.putInt("settemp_type04",  set_type04);              //0代表预设，1代表手动
        editor.putInt("preset_location01",  preset_location01);   //预设位置
        editor.putInt("preset_location02",  preset_location02);   //预设位置
        editor.putInt("preset_location03",  preset_location03);   //预设位置
        editor.putInt("preset_location04",  preset_location04);   //预设位置
        editor.putInt("preset_location201", preset_location201);  //预设位置2
        editor.putInt("preset_location202", preset_location201);  //预设位置2
        editor.putInt("preset_location203", preset_location203);  //预设位置2
        editor.putInt("preset_location204", preset_location04);   //预设位置2
        editor.putInt("temp_reminder01",    temp_reminder01);
        editor.putInt("temp_reminder02", temp_reminder02);
        editor.putInt("temp_reminder03", temp_reminder03);
        editor.putInt("temp_reminder04", temp_reminder04);
        editor.putInt("time_reminder01", time_reminder01);
        editor.putInt("time_reminder02", time_reminder02);
        editor.putInt("time_reminder03", time_reminder03);
        editor.putInt("time_reminder04", time_reminder04);
        editor.putInt("temp_manualc01",   temp_manualc01);
        editor.putInt("temp_manualc02",   temp_manualc02);
        editor.putInt("temp_manualc03",   temp_manualc03);
        editor.putInt("temp_manualc04",   temp_manualc04);
        editor.putInt("temp_manualf01",   temp_manualf01);
        editor.putInt("temp_manualf02",   temp_manualf02);
        editor.putInt("temp_manualf03",   temp_manualf03);
        editor.putInt("temp_manualf04",   temp_manualf04);
        editor.putInt("sound_reminder01",sound_reminder01);
        editor.putInt("sound_remidner02",sound_reminder02);
        editor.putInt("sound_reminder03",sound_reminder03);
        editor.putInt("sound_reminder04",sound_reminder04);
        editor.apply();
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
                    //Toast.makeText(this, "自Android 6.0开始需要打开位置权限才可以搜索到Ble设备", Toast.LENGTH_SHORT).show();
                }
                //请求权限
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ACCESS_COARSE_LOCATION);
            }

            if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION)
                    !=PackageManager.PERMISSION_GRANTED){

                if(ActivityCompat.shouldShowRequestPermissionRationale(
                        this,Manifest.permission.ACCESS_COARSE_LOCATION )){}

                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION },1);
            }
        }
    }
}

