package com.example.benetech.bbq;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.benetech.bbq.base.mBaseActivity;
import com.example.benetech.bbq.bluetoothapi.APIData;
import com.example.benetech.bbq.view.EasyPickerView;
import java.util.ArrayList;


public class TempReminderActivity extends mBaseActivity implements View.OnClickListener {
    private TextView tv_tempreminder_c, tv_tempreminder_f;
    private TextView tv_tempreminder_preset, tv_tempreminder_manual;
    private ImageView tv_tempreminder_back;
    private EasyPickerView pv_tempreminder_food, pv_tempreminder_level;
    private EasyPickerView pv_tempreminder_hundred, pv_tempreminder_ten, pv_tempreminder_one;
    private TextView tv_temp_value;
    private TextView tv_temp_unit;
    private TextView tv_temp_bai,tv_temp_ten,tv_temp_one;
    private ArrayList<String> data = new ArrayList<>();
    private ArrayList<String> data2 = new ArrayList<>();
    private ArrayList<String> data3 = new ArrayList<>();
    private ArrayList<String> data4 = new ArrayList<>();
    private ArrayList<String> data5 = new ArrayList<>();
    private ArrayList<String> data6 = new ArrayList<>();
    private String[] food, beef, lamb, veal, welldone;

    private LinearLayout ll_tempreminder_manual, ll_tempreminder_preset;

    //手动设置温度布局隐藏
    private LinearLayout ll_tempreminder_manualvalue;
    private int channel;              //通道
    private Intent intent;

    private int temp_unit;
    private int location;
    private int location2;
    private int value;
    private int manualTemp;
    private int set_type;
    private int manualTempc;
    private int manualTempf;

    public static final int[] foodf = {145, 145, 145, 160, 170, 180, 180, 140, 160, 160, 160, 160, 160, 165, 210, 190};
    public static final int[] foodc = {63, 63, 63, 71, 77, 82, 82, 82, 60, 71, 71, 71, 71, 71, 74, 99, 88};
    public static final int[][] levelc = {{63, 71, 77}, {63, 71, 77}, {63, 71, 77}, {71}, {77}, {82}, {82}, {82}
            , {60}, {71}, {71}, {71}, {71}, {71}, {74}, {99}, {88}};
    public static int[][] levelf = {{145, 160, 170}, {145, 160, 170}, {145, 160, 170}, {160}, {170}, {180},
            {180}, {180}, {140}, {160}, {160}, {160}, {160}, {160}, {165}, {210}, {190}};


    @Override
    protected int getLayoutID() {
        return R.layout.activity_temp_reminder;
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        intent = getIntent();
        channel = intent.getIntExtra("channel", 0);
        verdictChannel();
        tv_temp_bai=findViewById(R.id.tv_temp_bai);
        tv_temp_ten=findViewById(R.id.tv_temp_ten);
        tv_temp_one=findViewById(R.id.tv_temp_one);
        ll_tempreminder_manualvalue=findViewById(R.id.ll_tempreminder_manualvalue);
        tv_tempreminder_back = findViewById(R.id.iv_tempreminder_back);
        tv_tempreminder_back.setOnClickListener(this);
        tv_tempreminder_c = findViewById(R.id.tv_tempreminder_c);
        tv_tempreminder_f = findViewById(R.id.tv_tempreminder_f);
        tv_tempreminder_preset = findViewById(R.id.tv_tempreminder_preset);
        tv_tempreminder_manual = findViewById(R.id.tv_tempreminder_manual);
        pv_tempreminder_food = findViewById(R.id.pv_tempreminder_food);
        pv_tempreminder_level = findViewById(R.id.pv_tempreminder_level);
        pv_tempreminder_hundred = findViewById(R.id.pv_tempreminder_hundred);
        pv_tempreminder_ten = findViewById(R.id.pv_tempreminder_ten);
        pv_tempreminder_one = findViewById(R.id.pv_tempreminder_one);
        tv_temp_value = findViewById(R.id.tv_temp_value);
        ll_tempreminder_preset = findViewById(R.id.ll_tempreminder_preset);
        ll_tempreminder_manual = findViewById(R.id.ll_tempreminder_manual);
        tv_temp_value = findViewById(R.id.tv_temp_value);
        tv_temp_unit = findViewById(R.id.tv_temp_unit);
        tv_tempreminder_c.setOnClickListener(this);
        tv_tempreminder_f.setOnClickListener(this);
        tv_tempreminder_preset.setOnClickListener(this);
        tv_tempreminder_manual.setOnClickListener(this);
        Resources res = getResources();
        food=new String[]{res.getString(R.string.beef),res.getString(R.string.lamf),res.getString(R.string.veal),res.getString(R.string.pork)
         ,res.getString(R.string.hamburger),res.getString(R.string.chicken),res.getString(R.string.duck),
                res.getString(R.string.turkey),res.getString(R.string.cookedham),res.getString(R.string.freshham),
                res.getString(R.string.groundbeef),res.getString(R.string.groundlamf),res.getString(R.string.groundpork),
                res.getString(R.string.groundchicken),res.getString(R.string.potato),res.getString(R.string.corn)};
        lamb = new String[]{res.getString(R.string.medium),res.getString(R.string.mediumwell),res.getString(R.string.welldone)};
        beef = new String[]{res.getString(R.string.medium),res.getString(R.string.mediumwell),res.getString(R.string.welldone)};
        welldone = new String[]{res.getString(R.string.welldone)};
        for (int i = 0; i < food.length; i++) {
            data.add(food[i]);
        }
        for (int i = 0; i < beef.length; i++) {
            data2.add(beef[i]);
        }
        for (int i = 0; i < 10; i++) {
            data4.add(i + "");
            data6.add(i + "");
        }
        for (int i = 0; i < 6; i++) {
            data5.add(i + "");
        }
        data3.add(welldone[0]);
        pv_tempreminder_hundred.setDataList(data5);
        pv_tempreminder_ten.setDataList(data6);
        pv_tempreminder_one.setDataList(data4);
        initData();
        pv_tempreminder_food.setOnScrollChangedListener(new EasyPickerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int curIndex) {
                location = curIndex;
                if (temp_unit == 0) {
                    tv_temp_value.setText(foodc[location] + "");
                } else {
                    tv_temp_value.setText(foodf[location] + "");
                }
            }

            @Override
            public void onScrollFinished(int curIndex) {

                location = curIndex;
                if (location < 3) {
                    pv_tempreminder_level.setDataList(data2);
                    pv_tempreminder_level.moveTo(0);
                } else {
                    pv_tempreminder_level.setDataList(data3);
                    pv_tempreminder_level.moveTo(0);
                }
                if (temp_unit == 0) {
                    tv_temp_value.setText(foodc[location] + "");
                } else {
                    tv_temp_value.setText(foodf[location] + "");
                }

            }
        });
        pv_tempreminder_level.setOnScrollChangedListener(new EasyPickerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int curIndex){
                location2 = curIndex % 3;
                Log.e("TAG", "onScrollChanged:88888887level" + curIndex+"||"+location2);
                if (temp_unit == 0) {
                    tv_temp_value.setText(BluetoothActivity.levelc[location][location2] + "");
                } else {
                    tv_temp_value.setText(BluetoothActivity.levelc[location][location2] + "");
                }
            }

            @Override
            public void onScrollFinished(int curIndex){
                location2 = curIndex % 3;
                Log.e("TAG", "onScrollChanged:8888level" + curIndex+"||"+location2);
                if (temp_unit == 0) {
                    tv_temp_value.setText(BluetoothActivity.levelc[location][location2] + "");
                } else {
                    tv_temp_value.setText(BluetoothActivity.levelc[location][location2] + "");
                }
            }
        });
        pv_tempreminder_hundred.setOnScrollChangedListener(new EasyPickerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int curIndex){
                tv_temp_bai.setText(curIndex+"");
                if(curIndex==0){
                    tv_temp_bai.setText("");
                }
            }

            @Override
            public void onScrollFinished(int curIndex){
                tv_temp_bai.setText(curIndex+"");
                if(curIndex==0){
                    tv_temp_bai.setText("");
                }

                if(temp_unit==0){
                    if(getManualtemp()>250){
                        pv_tempreminder_hundred.moveTo(2);
                        pv_tempreminder_ten.moveTo(5);
                        pv_tempreminder_one.moveTo(0);
                    }
                }
                else{
                    if(getManualtemp()>482){
                        pv_tempreminder_hundred.moveTo(4);
                        pv_tempreminder_ten.moveTo(8);
                        pv_tempreminder_one.moveTo(2);
                    }
                }
            }
        });
        pv_tempreminder_ten.setOnScrollChangedListener(new EasyPickerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int curIndex) throws IllegalAccessException {
                  tv_temp_ten.setText(curIndex+"");
            }

            @Override
            public void onScrollFinished(int curIndex) throws IllegalAccessException {

                tv_temp_ten.setText(curIndex+"");

                if(temp_unit==0){
                    if(getManualtemp()>250){
                        pv_tempreminder_hundred.moveTo(2);
                        pv_tempreminder_ten.moveTo(5);
                        pv_tempreminder_one.moveTo(0);
                    }
                }
                else{
                    if(getManualtemp()>482){
                        pv_tempreminder_hundred.moveTo(4);
                        pv_tempreminder_ten.moveTo(8);
                        pv_tempreminder_one.moveTo(2);
                    }
                }
            }
        });
        pv_tempreminder_one.setOnScrollChangedListener(new EasyPickerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int curIndex) throws IllegalAccessException {
                tv_temp_one.setText(curIndex+"");

            }

            @Override
            public void onScrollFinished(int curIndex)  {
                tv_temp_one.setText(curIndex+"");
            }
        });
    }

    private void initData() {

        if (temp_unit == 0) {
            tv_tempreminder_f.setSelected(false);
            tv_tempreminder_c.setSelected(true);
            tv_tempreminder_c.setTextColor(Color.WHITE);
            tv_tempreminder_f.setTextColor(getResources().getColor(R.color.content_textcolor));
            tv_temp_unit.setText("℃");
        } else {
            tv_tempreminder_c.setSelected(false);
            tv_tempreminder_f.setSelected(true);
            tv_tempreminder_c.setTextColor(getResources().getColor(R.color.content_textcolor));
            tv_tempreminder_f.setTextColor(Color.WHITE);
            tv_temp_unit.setText("℉");
        }
        if (set_type == 0) {
            tv_tempreminder_preset.setSelected(true);
            tv_tempreminder_manual.setTextColor(getResources().getColor(R.color.content_textcolor));
            tv_tempreminder_preset.setTextColor(Color.WHITE);
            ll_tempreminder_preset.setVisibility(View.VISIBLE);
            tv_temp_value.setVisibility(View.VISIBLE);
            ll_tempreminder_manual.setVisibility(View.GONE);
            ll_tempreminder_manualvalue.setVisibility(View.GONE);
            if (set_type == 0) {
                tv_temp_value.setText(levelc[location][location2] + "");
            } else {
                tv_temp_value.setText(levelf[location][location2] + "");
            }
        } else {
            //tv_temp_value.setText(manualTemp + "");
            tv_tempreminder_manual.setSelected(true);
            tv_tempreminder_preset.setTextColor(getResources().getColor(R.color.content_textcolor));
            tv_tempreminder_manual.setTextColor(Color.WHITE);
            ll_tempreminder_preset.setVisibility(View.GONE);
            tv_temp_value.setVisibility(View.GONE);
            ll_tempreminder_manual.setVisibility(View.VISIBLE);
            ll_tempreminder_manualvalue.setVisibility(View.VISIBLE);
        }
        int hundred=value/100;
        int ten=value%100/10;
        int one=value%10;

        pv_tempreminder_hundred.moveTo(hundred);
        pv_tempreminder_ten.moveTo(ten);
        pv_tempreminder_one.moveTo(one);
        pv_tempreminder_food.setDataList(data);
        pv_tempreminder_food.moveTo(location);
        tv_temp_bai.setText(hundred+"");
        tv_temp_ten.setText(ten+"");
        tv_temp_one.setText(one+"");
        switch (location) {
            case 0:
                for (int i = 0; i < beef.length; i++) {
                    data2.add(beef[i]);
                }
                pv_tempreminder_level.setDataList(data2);
                pv_tempreminder_level.moveTo(location2);
                break;
            case 1:
                for (int i = 0; i < lamb.length; i++) {
                    data2.add(lamb[i]);
                }
                pv_tempreminder_level.setDataList(data2);
                pv_tempreminder_level.moveTo(location2);
                break;
            case 2:
                for (int i = 0; i < veal.length; i++) {
                    data2.add(veal[i]);
                }
                pv_tempreminder_level.setDataList(data2);
                pv_tempreminder_level.moveTo(location2);
                break;
            default:
                for (int i = 0; i < welldone.length; i++) {
                    data2.add(welldone[i]);
                }
                pv_tempreminder_level.setDataList(data2);
                pv_tempreminder_level.moveTo(location2);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_tempreminder_c:
                temp_unit = 0;
                tv_tempreminder_c.setSelected(true);
                tv_tempreminder_f.setSelected(false);
                tv_tempreminder_c.setTextColor(Color.WHITE);
                tv_tempreminder_f.setTextColor(getResources().getColor(R.color.content_textcolor));
                tv_temp_unit.setText("℃");
                if (set_type == 0) {
                    tv_temp_value.setText(levelc[location][location2] + "");
                } else {
                    tv_temp_value.setText(manualTempc + "");
                }
                break;
            case R.id.tv_tempreminder_f:
                temp_unit = 1;
                tv_tempreminder_c.setSelected(false);
                tv_tempreminder_f.setSelected(true);
                tv_tempreminder_c.setTextColor(getResources().getColor(R.color.content_textcolor));
                tv_tempreminder_f.setTextColor(Color.WHITE);
                tv_temp_unit.setText("℉");
                if (set_type == 0) {
                    tv_temp_value.setText(levelf[location][location2] + "");
                } else {
                    tv_temp_value.setText(manualTempf + "");
//
                }
                break;
            case R.id.tv_tempreminder_manual:
                set_type = 1;
                tv_tempreminder_manual.setSelected(true);
                tv_tempreminder_preset.setSelected(false);
                tv_tempreminder_preset.setTextColor(getResources().getColor(R.color.content_textcolor));
                tv_tempreminder_manual.setTextColor(Color.WHITE);
                ll_tempreminder_manual.setVisibility(View.VISIBLE);
                ll_tempreminder_preset.setVisibility(View.GONE);
                tv_temp_value.setText(manualTemp + "");
                ll_tempreminder_manualvalue.setVisibility(View.VISIBLE);
                tv_temp_value.setVisibility(View.GONE);
                break;
            case R.id.tv_tempreminder_preset:
                set_type = 0;
                tv_tempreminder_manual.setSelected(false);
                tv_tempreminder_preset.setSelected(true);
                tv_tempreminder_manual.setTextColor(getResources().getColor(R.color.content_textcolor));
                tv_tempreminder_preset.setTextColor(Color.WHITE);
                ll_tempreminder_manual.setVisibility(View.GONE);
                ll_tempreminder_preset.setVisibility(View.VISIBLE);
                ll_tempreminder_manualvalue.setVisibility(View.GONE);
                tv_temp_value.setVisibility(View.VISIBLE);
                if (temp_unit == 0) {
                    tv_temp_value.setText(levelc[location][location2] + "");
                } else {
                    tv_temp_value.setText(levelf[location][location2] + "");
                }
                break;
            case R.id.iv_tempreminder_back:
                backToSave();
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        backToSave();
        startActivity(new Intent(mContext,SettingActivity.class));
        return false;
        //return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onStop() {
        super.onStop();
        backToSave();
    }

    private int getManualtemp(){
        int manualTempValue=Integer.parseInt(tv_temp_bai.getText().toString()+tv_temp_ten.getText().toString()+tv_temp_one.getText());
        return manualTempValue;
    }

    private void verdictChannel() {
        if (channel == 1) {
            location     = BluetoothActivity.preset_location01;
            location2    = BluetoothActivity.preset_location201;
            value        = BluetoothActivity.temp_manualc01;
            manualTempf  = BluetoothActivity.temp_manualf01;
//            manualTemp   = BluetoothActivity.temp_manualc01;
            temp_unit    = BluetoothActivity.temp_unit01;
            set_type     = BluetoothActivity.set_type01;
        } else if (channel == 2) {
            location     = BluetoothActivity.preset_location02;
            location2    = BluetoothActivity.preset_location202;
            value        = BluetoothActivity.temp_manualc02;
            manualTempf  = BluetoothActivity.temp_manualf02;
            temp_unit    = BluetoothActivity.temp_unit02;
            set_type     = BluetoothActivity.set_type02;
        } else if (channel == 3) {
            location     = BluetoothActivity.preset_location03;
            location2    = BluetoothActivity.preset_location203;
            value        = BluetoothActivity.temp_manualc03;
            manualTempf  = BluetoothActivity.temp_manualf03;
            temp_unit    = BluetoothActivity.temp_unit03;
            set_type     = BluetoothActivity.set_type03;
        } else if (channel == 4) {
            location     = BluetoothActivity.preset_location04;
            location2    = BluetoothActivity.preset_location204;
            value        = BluetoothActivity.temp_manualc04;
            manualTempf  = BluetoothActivity.temp_manualf04;
            temp_unit    = BluetoothActivity.temp_unit04;
            set_type     = BluetoothActivity.set_type03;
        }
    }

    private void backToSave() {
        short limitvalue;
        value=getManualtemp();
        if(set_type==0){
            limitvalue= (short) levelc[location][location2];
        }else{
            if(temp_unit==0){
                limitvalue= (short) value;
            }else{
                limitvalue= (short) ((value-32)/1.8);
            }
        }
        Log.e("TAG", "backToSave:1111111111111 "+limitvalue );
        ChannelActivity.writeData(APIData.setTemp_limit((short) (limitvalue*10),(byte)channel));
        if(location2>3){location2=0;}
        if (channel == 1) {
            BluetoothActivity.preset_location01  = location;
            BluetoothActivity.preset_location201 = location2;
            BluetoothActivity.temp_manualc01     = value;
            BluetoothActivity.temp_manualf01     = manualTempf;
            BluetoothActivity.temp_unit01        = temp_unit;
            BluetoothActivity.set_type01         = set_type;
        } else if (channel == 2) {
            BluetoothActivity.preset_location02  = location;
            BluetoothActivity.preset_location202 = location2;
            BluetoothActivity.temp_manualc02     = value;
            BluetoothActivity.temp_manualf02     = manualTempf;
            BluetoothActivity.temp_unit02        = temp_unit;
            BluetoothActivity.set_type02         = set_type;
        } else if (channel == 3) {
            BluetoothActivity.preset_location03  = location;
            BluetoothActivity.preset_location203 = location2;
            BluetoothActivity.temp_manualc03     = value;
            BluetoothActivity.temp_manualf03     = manualTempf;
            BluetoothActivity.temp_unit03        = temp_unit;
            BluetoothActivity.set_type03         = set_type;
        } else if (channel == 4) {
            BluetoothActivity.preset_location04  = location;
            BluetoothActivity.preset_location204 = location2;
            BluetoothActivity.temp_manualc04     = value;
            BluetoothActivity.temp_manualf04     = manualTempf;
            BluetoothActivity.temp_unit04        = temp_unit;
            BluetoothActivity.set_type04         = set_type;
        }
        if(temp_unit==0){
            ChannelActivity.writeData(APIData.setUnit((byte) 1,(byte)channel));
        }else{
            ChannelActivity.writeData(APIData.setUnit((byte) 0,(byte)channel));
        }

        Intent intent = new Intent();
        intent.putExtra("change", 1);
        intent.setAction("com.project.moli.demobroad.MyService");
        sendBroadcast(intent);
    }
}

