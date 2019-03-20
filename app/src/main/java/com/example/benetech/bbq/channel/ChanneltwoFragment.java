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
import android.widget.ImageButton;
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

public class ChanneltwoFragment extends Fragment implements View.OnClickListener {

    private View v;
    private ImageView iv_channeltwo_back,iv_channeltwo_setting;
    private TextView tv_channeltwo_value,tv_channeltwo_unit;

    private TextView tv_channeltwo_battery,tv_channeltwo_food,tv_channeltwo_level,tv_channeltwo_limit;
    private LineChart linechart_channeltwo;
    private Context mContext;
    private mBroadcastReceiver mBroadcastReceiver;
    private ImageButton iv_channeltwo_file;
    public static TextView tv_channeltwo_timedown;
    private short limitTemp;
    private String food;
    private String level;

    private MenuPopupWindow menuPopupWindow;
    private ArrayList<TemValue> list;
    //食物
    private String[] foodarray, beefarray, lambarray, vealarray, welldonearray;
    private BatteryView bv_channeltwo_battery;

    private RingProgressBar rb_channeltwo_value;
    private int tempunit02;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_channeltwo, null);
        mContext=v.getContext();
        tempunit02=BluetoothActivity.temp_unit02;
        init();
        getlimitTemp();
        return v;
    }

    private void init(){
        list=new ArrayList<>();
        rb_channeltwo_value=v.findViewById(R.id.rb_channeltwo_value);
        bv_channeltwo_battery=v.findViewById(R.id.bv_channeltwo_battery);
        tv_channeltwo_value=v.findViewById(R.id.tv_channeltwo_value);
        tv_channeltwo_unit=v.findViewById(R.id.tv_channeltwo_unit);
        iv_channeltwo_back=v.findViewById(R.id.iv_channeltwo_back);
        iv_channeltwo_back.setOnClickListener(this);
        iv_channeltwo_setting=v.findViewById(R.id.iv_channeltwo_setting);
        iv_channeltwo_setting.setOnClickListener(this);
        iv_channeltwo_file=v.findViewById(R.id.iv_channeltwo_file);
        iv_channeltwo_file.setOnClickListener(this);
        linechart_channeltwo=v.findViewById(R.id.linechart_channeltwo);
     //   tv_channeltwo_battery=v.findViewById(R.id.tv_channeltwo_battery);
        tv_channeltwo_food=v.findViewById(R.id.tv_channeltwo_food);
        tv_channeltwo_level=v.findViewById(R.id.tv_channeltwo_level);
        tv_channeltwo_limit=v.findViewById(R.id.tv_channeltwo_limit);
        tv_channeltwo_timedown=v.findViewById(R.id.tv_channeltwo_timedown);
        Resources res = getResources();
        foodarray = res.getStringArray(R.array.food);
        beefarray = res.getStringArray(R.array.beef);
        welldonearray = res.getStringArray(R.array.pork);
        createChart();

    }

    public void setTemp(short temp){
        bv_channeltwo_battery.setPower(ChannelActivity.battery);
        if(temp!=32767){
            float tempValue= (float) (temp*0.1);
            list.add(new TemValue(tempValue));
            if(BluetoothActivity.temp_unit02==0){
                rb_channeltwo_value.setProgress(tempValue ,"℃");
                tv_channeltwo_value.setText(String.format("%.1f",tempValue)+"");
                tv_channeltwo_unit.setText("℃");
                addEntry(tempValue);
            }else{
                float tempf= (float) (tempValue*1.8+32);
                tv_channeltwo_value.setText(String.format("%.1f",tempf)+"");
                tv_channeltwo_unit.setText("℉");
                rb_channeltwo_value.setProgress(tempf,"℉");
                addEntry(tempf);
            }
        }else{
            tv_channeltwo_value.setText("------");
        }


    }

    private void createChart() {
        linechart_channeltwo.setDrawBorders(false);  //是否在折线图上添加边框
        linechart_channeltwo.setDescription(" ");     // 数据描述
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        linechart_channeltwo.setNoDataTextDescription(" ");
        linechart_channeltwo.setDrawGridBackground(false);         // 是否显示表格颜色
        linechart_channeltwo.setGridBackgroundColor(Color.BLACK);  // 表格的的颜色，在这里是是给颜色设置一个透明度
        linechart_channeltwo.setTouchEnabled(true);                // 设置是否可以触摸
        linechart_channeltwo.setDragEnabled(true);                 // 是否可以拖拽
        linechart_channeltwo.setScaleEnabled(true);                // 是否可以缩放
        linechart_channeltwo.setPinchZoom(false);
        linechart_channeltwo.setBackgroundColor(Color.argb(10, 0xFF, 0xFF, 0xFF));// 设置背景
        LineData lineData = new LineData();
        linechart_channeltwo.setData(lineData);                    // 设置数据
        Legend mLegend = linechart_channeltwo.getLegend();         // 设置比例图标示，就是那个一组y的value的
        mLegend.setForm(Legend.LegendForm.CIRCLE);  // 样式
        mLegend.setFormSize(6f);                     // 字体
        mLegend.setTextColor(Color.BLACK);          // 颜色
//        chart.animateX(2500);                       // 立即执行的动画,x轴
        XAxis xAxis=linechart_channeltwo.getXAxis();
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
        LineData data = linechart_channeltwo.getData();
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
        linechart_channeltwo.notifyDataSetChanged();
        // 当前统计图表中最多在x轴坐标线上显示的总量
        linechart_channeltwo.setVisibleXRangeMaximum(8);
        // 此代码将刷新图表的绘图
        linechart_channeltwo.moveViewToX(set.getEntryCount() - 9);
        linechart_channeltwo.invalidate();
    }

    // 初始化数据集，添加一条统计折线，可以简单的理解是初始化y坐标轴线上点的表征
    private LineDataSet createLineDataSet() {
        LineDataSet set = new LineDataSet(null,"");
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
        switch(v.getId()){
            case R.id.iv_channeltwo_back:
                ChannelActivity.setCurrentPage(0);
                break;
            case R.id.iv_channeltwo_setting:
                menu_Button(v);
                break;
            case R.id.iv_channeltwo_file:
                Intent intent=new Intent(mContext,ContentActivity.class);
                intent.putExtra("table","channeltwo");
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
        intentFilter.addAction(BroadcastAddress.addresschangetwo);
        // 3. 动态注册：调用Context的registerReceiver（）方法
        getActivity().registerReceiver(mBroadcastReceiver, intentFilter);
    }


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            menuPopupWindow.dismiss();
            switch (view.getId()) {
                case R.id.setting_menu:
                   // menu_Button(v);
                    Intent intent1=new Intent(mContext,SettingActivity.class);
                    intent1.putExtra("channel",2);
                    startActivity(intent1);
                    break;
                case R.id.save_menu:
                    SaveDialog dialog=new SaveDialog(mContext,list,"channelone",2);
                    dialog.show();
                    //startActivity(new Intent(mContext, SaveActivity.class));
                    break;
                case R.id.clear_menu:
                    linechart_channeltwo.getLineData().clearValues();
                    linechart_channeltwo.notifyDataSetChanged();
                    linechart_channeltwo.invalidate();
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
                view.findViewById(R.id.iv_channeltwo_setting),
                Gravity.TOP | Gravity.TOP, 200, 200);       // 设置layout在PopupWindow中显示的位置
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
            int time  = intent.getIntExtra("time1", -1);
            int time2 = intent.getIntExtra("time2", -1);
            int time3 = intent.getIntExtra("time3", -1);
            int time4 = intent.getIntExtra("time4", -1);
            int changtemp=intent.getIntExtra("change",0);
            if(time2==-1){
                //tv_channelone_timedown.setText("");
            }else{
                tv_channeltwo_timedown.setText(switchHms(time2));
            }
            if(changtemp==1){
                getlimitTemp();
                if(tempunit02!=BluetoothActivity.temp_unit02){
                    Log.e(TAG, "onReceive:777777 "+tempunit02+BluetoothActivity.temp_unit01 );
                    tempunit02=BluetoothActivity.temp_unit02;
                    linechart_channeltwo.setData(new LineData());
                    linechart_channeltwo.getLineData().clearValues();
                    linechart_channeltwo.notifyDataSetChanged();
                    linechart_channeltwo.invalidate();
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
        if(hour<10){
            hourS="0"+hour;
        }else{
            hourS=hour+"";
        }
        if(minute<10){
            minuteS="0"+minute;
        }else{
            minuteS=""+minute;
        }
        if(second<10){
            secondS="0"+second;
        }else{
            secondS=""+second;
        }
        return hourS+ ":" + minuteS + ":" + secondS + "";
    }

    public void  getlimitTemp(){
        if(BluetoothActivity.set_type02==0){
            if(BluetoothActivity.temp_unit02==0){
                limitTemp= (short) BluetoothActivity.levelc[BluetoothActivity.preset_location02][BluetoothActivity.preset_location202];
            }else{
                limitTemp= (short) BluetoothActivity.levelf[BluetoothActivity.preset_location02][BluetoothActivity.preset_location202];
            }
        }else{
            limitTemp= (short) BluetoothActivity.temp_manualc02;
        }
        food =foodarray[BluetoothActivity.preset_location02];

        if(BluetoothActivity.preset_location02<3){
            level=beefarray[BluetoothActivity.preset_location202];
        }else{
            level=welldonearray[BluetoothActivity.preset_location202];
        }

        if(BluetoothActivity.set_type02==0){
            tv_channeltwo_food.setText(food);
            tv_channeltwo_level.setText(level);
            tv_channeltwo_limit.setText(limitTemp+"");
        }else{
            tv_channeltwo_food.setText("");
            tv_channeltwo_level.setText("");
            tv_channeltwo_limit.setText(limitTemp+"");
        }

        if(BluetoothActivity.temp_unit02==0){
            tv_channeltwo_limit.setText(limitTemp+"℃");
        }else{
            tv_channeltwo_limit.setText(limitTemp+"℉");
        }
    }
}
