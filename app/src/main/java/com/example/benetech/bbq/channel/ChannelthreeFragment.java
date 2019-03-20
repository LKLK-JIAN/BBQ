package com.example.benetech.bbq.channel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.benetech.bbq.BluetoothActivity;
import com.example.benetech.bbq.BroadcastAddress;
import com.example.benetech.bbq.ChannelActivity;
import com.example.benetech.bbq.ContentActivity;
import com.example.benetech.bbq.R;
import com.example.benetech.bbq.SettingActivity;
import com.example.benetech.bbq.bean.TemValue;
import com.example.benetech.bbq.dialog.SaveDialog;
import com.example.benetech.bbq.view.BatteryView;
import com.example.benetech.bbq.view.MenuPopupWindow;
import com.example.benetech.bbq.view.RingProgressBar;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ChannelthreeFragment extends Fragment implements View.OnClickListener {

    private View v;
    private ImageView iv_channelthree_back, iv_channelthree_setting, iv_channelthree_file;
    private TextView tv_channelthree_value, tv_channelthree_unit;
    private LineChart linechart_channelthree;
    private TextView tv_channelthree_battery, tv_channelthree_food, tv_channelthree_level, tv_channelthree_limit;
    private Context mContext;
    public static TextView tv_channelthree_timedown;
    private mBroadcastReceiver mBroadcastReceiver;
    private MenuPopupWindow menuPopupWindow;

    private short limitTemp;
    private String food;
    private String level;

    private ArrayList<TemValue> list;

    private RingProgressBar rb_channelthree_value;
    private int tempunit03;

    //食物
    private String[] foodarray, beefarray, lambarray, vealarray, welldonearray;
    private BatteryView bv_channelthree_battery;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_channelthree, null);
        mContext = v.getContext();
        tempunit03=BluetoothActivity.temp_unit03;
        init();
        return v;
    }


    private void init() {
        list = new ArrayList<>();
        rb_channelthree_value=v.findViewById(R.id.rb_channelthree_value);
        bv_channelthree_battery=v.findViewById(R.id.bv_channelthree_battery);
        tv_channelthree_value = v.findViewById(R.id.tv_channelthree_value);
        tv_channelthree_unit = v.findViewById(R.id.tv_channelthree_unit);
        iv_channelthree_back = v.findViewById(R.id.iv_channelthree_back);
        iv_channelthree_back.setOnClickListener(this);
        iv_channelthree_setting = v.findViewById(R.id.iv_channelthree_setting);
        iv_channelthree_setting.setOnClickListener(this);
        iv_channelthree_file = v.findViewById(R.id.iv_channelthree_file);
        iv_channelthree_file.setOnClickListener(this);
        linechart_channelthree = v.findViewById(R.id.linechart_channelthree);
        //  tv_channelthree_battery=v.findViewById(R.id.tv_channelthree_battery);
        tv_channelthree_food = v.findViewById(R.id.tv_channelthree_food);
        tv_channelthree_level = v.findViewById(R.id.tv_channelthree_level);
        tv_channelthree_limit = v.findViewById(R.id.tv_channelthree_limit);
        tv_channelthree_timedown = v.findViewById(R.id.tv_channelthree_timedown);

        Resources res = getResources();
        foodarray = res.getStringArray(R.array.food);
        beefarray = res.getStringArray(R.array.beef);
        welldonearray = res.getStringArray(R.array.pork);
        createChart();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            menuPopupWindow.dismiss();
            switch (view.getId()) {
                case R.id.setting_menu:
                    menu_Button(v);
//                    Intent intent1=new Intent(mContext,SettingActivity.class);
//                    intent1.putExtra("channel",3);
//                    startActivity(intent1);
                    break;
                case R.id.save_menu:
                    SaveDialog saveDialog = new SaveDialog(mContext, list, "channelthree",3);
                    saveDialog.show();
                    //startActivity(new Intent(mContext, SaveActivity.class));
                    break;
                case R.id.clear_menu:
                    linechart_channelthree.getLineData().clearValues();
                    linechart_channelthree.notifyDataSetChanged();
                    linechart_channelthree.invalidate();
                    list.clear();
                    // ClearChartData();
                    break;
            }
        }
    };

    //菜单按钮
    public void menu_Button(View view) {
//        View menupopu= getActivity().getLayoutInflater().inflate(
//                R.layout. );
        menuPopupWindow = new MenuPopupWindow(mContext,
                onClickListener);
        // 显示窗口
        menuPopupWindow.showAtLocation(
                view.findViewById(R.id.iv_channelthree_setting),
                Gravity.TOP | Gravity.TOP, 200, 200);       // 设置layout在PopupWindow中显示的位置
    }

    public void setTemp(short temp) {
        bv_channelthree_battery.setPower(ChannelActivity.battery);
        if (temp != 32767) {
            float tempvalue = (short) (temp * 0.1);
            list.add(new TemValue(tempvalue));
            if (BluetoothActivity.temp_unit03 == 0) {
                rb_channelthree_value.setProgress(tempvalue,"℃");
                tv_channelthree_value.setText(String.format("%.1f", tempvalue) + "");
                addEntry(tempvalue);
                tv_channelthree_unit.setText("℃");
            } else {
                float tempf = (short) (temp * 0.1 * 1.8 + 32);
                rb_channelthree_value.setProgress(tempf,"℉");
                tv_channelthree_value.setText(String.format("%.1f", tempf) + "");
                addEntry(tempf);
                tv_channelthree_unit.setText("℉");
            }
        } else {
            tv_channelthree_value.setText("------");
        }
    }

    private void createChart() {
        linechart_channelthree.setDrawBorders(false);  //是否在折线图上添加边框
        linechart_channelthree.setDescription(" ");     // 数据描述
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        linechart_channelthree.setNoDataTextDescription(" ");
        linechart_channelthree.setDrawGridBackground(false);         // 是否显示表格颜色
        linechart_channelthree.setGridBackgroundColor(Color.BLACK);  // 表格的的颜色，在这里是是给颜色设置一个透明度
        linechart_channelthree.setTouchEnabled(true);                // 设置是否可以触摸
        linechart_channelthree.setDragEnabled(true);                 // 是否可以拖拽
        linechart_channelthree.setScaleEnabled(true);                // 是否可以缩放
        linechart_channelthree.setPinchZoom(false);
        linechart_channelthree.setBackgroundColor(Color.argb(10, 0xFF, 0xFF, 0xFF));// 设置背景
        LineData lineData = new LineData();
        linechart_channelthree.setData(lineData);                    // 设置数据
        Legend mLegend = linechart_channelthree.getLegend();         // 设置比例图标示，就是那个一组y的value的
        mLegend.setForm(Legend.LegendForm.CIRCLE);  // 样式
        mLegend.setFormSize(6f);                     // 字体
        mLegend.setTextColor(Color.BLACK);          // 颜色
//        chart.animateX(2500);                       // 立即执行的动画,x轴
        XAxis xAxis = linechart_channelthree.getXAxis();
        xAxis.setEnabled(true);
        //设置显示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置是否绘制x轴
        xAxis.setDrawAxisLine(true);
        //设置是否显示x轴的坐标
        xAxis.setDrawLabels(true);
        xAxis.setTextSize(10f);
    }

    // 添加进去一个坐标点
    public void addEntry(float wind) {
        LineData data = linechart_channelthree.getData();
        LineDataSet set = data.getDataSetByIndex(0);
        // 如果该统计折线图还没有数据集，则创建一条出来，如果有则跳过此处代码。
        if (set == null) {
            set = createLineDataSet();
            final DecimalFormat mFormat = new DecimalFormat("0.0");
            set.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                    return mFormat.format(v);
                }
            });
            data.addDataSet(set);
        }
        // 因为是从0开始，data.getXValCount()每次返回的总是全部x坐标轴上总数量，所以不必多此一举的加1
        data.addXValue((data.getXValCount() + 1) + "");
        // 如从0开始一样的数组下标，那么不必多次一举的加1
        Entry entry = new Entry(wind, set.getEntryCount());
        data.addEntry(entry, 0);
        // 像ListView那样的通知数据更新
        linechart_channelthree.notifyDataSetChanged();
        // 当前统计图表中最多在x轴坐标线上显示的总量
        linechart_channelthree.setVisibleXRangeMaximum(8);
        // 此代码将刷新图表的绘图
        linechart_channelthree.moveViewToX(set.getEntryCount() - 9);
        linechart_channelthree.invalidate();
    }

    // 初始化数据集，添加一条统计折线，可以简单的理解是初始化y坐标轴线上点的表征
    private LineDataSet createLineDataSet() {
        LineDataSet set = new LineDataSet(null, "");
        set.setLineWidth(1.75f); // 线宽
        set.setValueTextColor(Color.BLACK);
        set.setCircleSize(2f);// 显示的圆形大小
        set.setColor(Color.parseColor("#2B8AC2"));// 显示颜色
        set.setCircleColor(Color.parseColor("#2B8AC2"));// 圆形的颜色
        set.setHighLightColor(Color.parseColor("#2B8AC2")); // 高亮的线的颜色
        set.setDrawFilled(true);    //设置允许填充
        set.setFillColor(Color.parseColor("#2B8AC2"));
        return set;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_channelthree_back:
                ChannelActivity.setCurrentPage(0);
                break;
            case R.id.iv_channelthree_setting:
                Intent intent1 = new Intent(mContext, SettingActivity.class);
                intent1.putExtra("channel", 3);
                startActivity(intent1);
                break;
            case R.id.iv_channelthree_file:
                Intent intent=new Intent(mContext,ContentActivity.class);
                intent.putExtra("table", "channelthree");
                startActivity(intent);
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
        intentFilter.addAction(BroadcastAddress.addresschangethree);
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
            int changethree = intent.getIntExtra("change", -1);
            if (time3 == -1) {
                //tv_channelone_timedown.setText("");
            } else {
                tv_channelthree_timedown.setText(switchHms(time3));
            }
            if (changethree == 1) {
                getlimitTemp();
                if(tempunit03!=BluetoothActivity.temp_unit02){
                    Log.e(TAG, "onReceive:777777 "+tempunit03+BluetoothActivity.temp_unit02 );
                    tempunit03=BluetoothActivity.temp_unit02;
                    linechart_channelthree.getLineData().clearValues();
                    linechart_channelthree.notifyDataSetChanged();
                    linechart_channelthree.invalidate();
                    list.clear();
                }
            }
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

    public void getlimitTemp() {
        if (BluetoothActivity.set_type01 == 0) {
            if (BluetoothActivity.temp_unit01 == 0) {
                limitTemp = (short) BluetoothActivity.levelc[BluetoothActivity.preset_location03][BluetoothActivity.preset_location203];
            } else {
                limitTemp = (short) BluetoothActivity.levelf[BluetoothActivity.preset_location03][BluetoothActivity.preset_location203];
            }
        } else {
            limitTemp = (short) BluetoothActivity.temp_manualc01;
        }
        food = foodarray[BluetoothActivity.preset_location03];
        if (BluetoothActivity.preset_location04 < 3) {
            level = beefarray[BluetoothActivity.preset_location203];
        } else {
            level = welldonearray[BluetoothActivity.preset_location203];
        }
        if (BluetoothActivity.set_type03 == 0) {
            tv_channelthree_food.setText(food);
            tv_channelthree_level.setText(level);
            tv_channelthree_limit.setText(limitTemp + "");
        } else {
            tv_channelthree_food.setText("");
            tv_channelthree_level.setText("");
            tv_channelthree_limit.setText(limitTemp + "");
        }

        if (BluetoothActivity.temp_unit03 == 0) {
            tv_channelthree_limit.setText(limitTemp + "℃");
        } else {
            tv_channelthree_limit.setText(limitTemp + "℉");
        }
    }
}
