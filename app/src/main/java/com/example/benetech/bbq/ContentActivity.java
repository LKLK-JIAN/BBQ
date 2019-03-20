package com.example.benetech.bbq;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.benetech.bbq.basesqlite.TempMeterService;
import com.example.benetech.bbq.bean.Document;
import com.example.benetech.bbq.bean.TemValue;
import com.example.benetech.bbq.dialog.CustomProgressDialog;
import com.example.benetech.bbq.dialog.ImportDialog;
import com.example.benetech.bbq.view.RingProgressBar;
import com.example.benetech.bbq.wight.MenuPopupContent;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ContentActivity extends AppCompatActivity implements View.OnClickListener, OnChartValueSelectedListener {

    private TextView tv_chart_value, tv_chart_maxormin, tv_chart_hold, tv_chart_fastorslow, tv_chart_ac;
    private LineChart lineChart;
    private LinearLayout content_chart;
    private Handler handler;
    private Bundle bundle;
    private Document d;
    private ArrayList<TemValue> list;
    private TempMeterService service;
    private TextView  tv_content_center;
    private ImageView tv_content_right;
    private ImageView tv_content_left;
    private TextView tv_content_value;
    private CustomProgressDialog progressDialog;
    private MenuPopupContent menuPopupContent;
    private RelativeLayout rl_content;
    // private ListView lv_content_datalist;
    private Context mContext;
    private String channeltable;
    private Intent intent;
    final DecimalFormat mFormat = new DecimalFormat("0.0");

    private RingProgressBar  rb_content_value;
    private TextView tv_content_c,tv_content_f;
    private int unitStyle;
    private float tempf=0;
    private float tempc=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(getOrientation());
        // 无标题栏，无状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getSupportActionBar().hide();
        setContentView(R.layout.activity_content);
        intent=getIntent();
        channeltable=intent.getStringExtra("channeltable");
        mContext=this;
        service = new TempMeterService(mContext);
        list = new ArrayList<>();
        bundle = this.getIntent().getExtras();
        d = (Document) bundle.getSerializable("document");
        //content_chart = findViewById(R.id.content_chart);
        lineChart = findViewById(R.id.linechart_chart_data);
        tv_content_c=findViewById(R.id.tv_content_c);
        tv_content_f=findViewById(R.id.tv_content_f);
        tv_content_c.setOnClickListener(this);
        tv_content_f.setOnClickListener(this);
        tv_content_c.setSelected(true);
        tv_content_c.setTextColor(Color.WHITE);
        tv_content_f.setTextColor(getResources().getColor(R.color.content_textcolor));
        unitStyle=0;
        //tv_content_value=findViewById(R.id.tv_content_value);
        rb_content_value=findViewById(R.id.rb_content_value);
//        tv_chart_value = findViewById(R.id.tv_chart_value);
//        tv_chart_maxormin = findViewById(R.id.tv_chart_maxormin);
//        tv_chart_hold = findViewById(R.id.tv_chart_hold);
//        tv_chart_fastorslow = findViewById(R.id.tv_chart_fastorslow);
//        tv_chart_ac = findViewById(R.id.tv_chart_ac);
        tv_content_left = findViewById(R.id.tv_content_left);
        tv_content_center = findViewById(R.id.tv_content_center);
        tv_content_right = findViewById(R.id.tv_content_right);
        tv_content_left.setOnClickListener(this);
        tv_content_right.setOnClickListener(this);
        tv_content_center.setText(d.getFile_title()+"");
        progressDialog = CustomProgressDialog.createDialog(this);
        progressDialog.setMessage(this.getResources().getString(R.string.loading));
        progressDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕progressDialog不消失
        progressDialog.setCancelable(false); // 设置点击返回键progressDialog不消失
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case 0x123:
                        creatLineChart();
                        progressDialog.dismiss();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
       // progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cursor cursor=service.getAllMeterdata(d.getId(),channeltable);
                //Cursor cursor = service.getAllValue(d.getmID());
                while (cursor.moveToNext()) {
                    float  temp= cursor.getFloat(cursor.getColumnIndex("tem"));
                    list.add(new TemValue(temp));
                }
                handler.sendEmptyMessage(0x123);
            }
        }).start();
    }

    private void creatLineChart() {
        lineChart.setDrawBorders(true);  //是否在折线图上添加边框
        lineChart.setDescription(" ");     // 数据描述
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        lineChart.setNoDataTextDescription(" ");
        lineChart.setBackgroundColor(Color.argb(10, 0xFF, 0xFF, 0xFF));
        lineChart.setDrawGridBackground(false);         // 是否显示表格颜色
        lineChart.setGridBackgroundColor(Color.BLACK);  // 表格的的颜色，在这里是是给颜色设置一个透明度
        lineChart.setTouchEnabled(true);                // 设置是否可以触摸
        lineChart.setDragEnabled(true);                 // 是否可以拖拽
        lineChart.setScaleEnabled(true);                // 是否可以缩放
        lineChart.setPinchZoom(false);
        //chart.setBackgroundColor(Color.argb(10, 0xFF, 0xFF, 0xFF));// 设置背景
        lineChart.setData(getLineData(list));                    // 设置数据
        Legend mLegend = lineChart.getLegend();         // 设置比例图标示，就是那个一组y的value的
        mLegend.setForm(Legend.LegendForm.CIRCLE);  // 样式
        mLegend.setFormSize(6f);                     // 字体
        mLegend.setTextColor(Color.WHITE);          // 颜色
        lineChart.setOnChartValueSelectedListener(this);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(mContext.getResources().getColor(R.color.white));
        YAxis yAxis = lineChart.getAxisLeft();
//        yAxis.setTextColor(mContext.getResources().getColor(R.color.textgreen));
//        YAxis yAxis1 = lineChart.getAxisRight();
//        yAxis1.setTextColor(mContext.getResources().getColor(R.color.textgreen));
    }


    private void createLineChart(LineData data){
        lineChart.setDrawBorders(true);  //是否在折线图上添加边框
        lineChart.setDescription(" ");     // 数据描述
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        lineChart.setNoDataTextDescription(" ");
        lineChart.setBackgroundColor(Color.argb(10, 0xFF, 0xFF, 0xFF));
        lineChart.setDrawGridBackground(false);         // 是否显示表格颜色
        lineChart.setGridBackgroundColor(Color.BLACK);  // 表格的的颜色，在这里是是给颜色设置一个透明度
        lineChart.setTouchEnabled(true);                // 设置是否可以触摸
        lineChart.setDragEnabled(true);                 // 是否可以拖拽
        lineChart.setScaleEnabled(true);                // 是否可以缩放
        lineChart.setPinchZoom(false);
        //chart.setBackgroundColor(Color.argb(10, 0xFF, 0xFF, 0xFF));// 设置背景
        lineChart.setData(data);                    // 设置数据
        Legend mLegend = lineChart.getLegend();         // 设置比例图标示，就是那个一组y的value的
        mLegend.setForm(Legend.LegendForm.CIRCLE);  // 样式
        mLegend.setFormSize(6f);                     // 字体
        mLegend.setTextColor(Color.WHITE);          // 颜色
        lineChart.setOnChartValueSelectedListener(this);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(mContext.getResources().getColor(R.color.white));
        YAxis yAxis = lineChart.getAxisLeft();
    }

    private LineData getLineDatac(ArrayList<TemValue> listLine) {
        ArrayList<String> xValues = new ArrayList<String>();
        // y轴的数据
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        for (int i = 0; i < listLine.size(); i++) {
            // x轴显示的数据，这里默认使用数字下标显示
            xValues.add((i + 1) + "");
            yValues.add(new Entry((float) (listLine.get(i).getTemp()), i));
        }
        /*显示在比例图上*/
        // y轴的数据集合
        LineDataSet lineDataSet = new LineDataSet(yValues, "℃");
        lineDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                return mFormat.format(v);
            }
        });
        //用y轴的集合来设置参数
        lineDataSet.setLineWidth(1.75f); // 线宽
        lineDataSet.setValueTextColor(Color.parseColor("#2B8AC2"));
        lineDataSet.setCircleSize(2f);// 显示的圆形大小
        lineDataSet.setColor(Color.parseColor("#2B8AC2"));// 显示颜色
        lineDataSet.setCircleColor(Color.parseColor("#2B8AC2"));// 圆形的颜色
        lineDataSet.setHighLightColor(Color.parseColor("#2B8AC2")); // 高亮的线的颜色
        lineDataSet.setDrawFilled(false);    //设置允许填充
        lineDataSet.setFillColor(Color.parseColor("#2B8AC2"));
        ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
        lineDataSets.add(lineDataSet); // add the datasets
        LineData lineData = new LineData(xValues, lineDataSets);
        return lineData;
    }

    private LineData getLineDataf(ArrayList<TemValue> listLine) {
        ArrayList<String> xValues = new ArrayList<String>();
        // y轴的数据
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        for (int i = 0; i < listLine.size(); i++) {
            // x轴显示的数据，这里默认使用数字下标显示
            xValues.add((i + 1) + "");
            yValues.add(new Entry((float) ((listLine.get(i).getTemp())*1.8+32), i));
        }
        /*显示在比例图上*/
        // y轴的数据集合
        LineDataSet lineDataSet = new LineDataSet(yValues, "℉");
        lineDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                return mFormat.format(v);
            }
        });
        //用y轴的集合来设置参数
        lineDataSet.setLineWidth(1.75f); // 线宽
        lineDataSet.setValueTextColor(Color.parseColor("#2B8AC2"));
        lineDataSet.setCircleSize(2f);// 显示的圆形大小
        lineDataSet.setColor(Color.parseColor("#2B8AC2"));// 显示颜色
        lineDataSet.setCircleColor(Color.parseColor("#2B8AC2"));// 圆形的颜色
        lineDataSet.setHighLightColor(Color.parseColor("#2B8AC2")); // 高亮的线的颜色
        lineDataSet.setDrawFilled(false);    //设置允许填充
        lineDataSet.setFillColor(Color.parseColor("#2B8AC2"));
        ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
        lineDataSets.add(lineDataSet); // add the datasets
        LineData lineData = new LineData(xValues, lineDataSets);
        return lineData;
    }

    /**
     * 生成一个数据
     *
     * @return
     */
    private LineData getLineData(ArrayList<TemValue> listLine) {
        ArrayList<String> xValues = new ArrayList<String>();
        // y轴的数据
        ArrayList<Entry> yValues = new ArrayList<Entry>();
        for (int i = 0; i < listLine.size(); i++) {
            // x轴显示的数据，这里默认使用数字下标显示
            xValues.add((i + 1) + "");
            yValues.add(new Entry((float) (listLine.get(i).getTemp()), i));
        }
        /*显示在比例图上*/
        // y轴的数据集合
        LineDataSet lineDataSet = new LineDataSet(yValues, "℃");
        lineDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float v, Entry entry, int i, ViewPortHandler viewPortHandler) {
                return mFormat.format(v);
            }
        });
        //用y轴的集合来设置参数
        lineDataSet.setLineWidth(1.75f); // 线宽
        lineDataSet.setValueTextColor(Color.parseColor("#2B8AC2"));
        lineDataSet.setCircleSize(2f);// 显示的圆形大小
        lineDataSet.setColor(Color.parseColor("#2B8AC2"));// 显示颜色
        lineDataSet.setCircleColor(Color.parseColor("#2B8AC2"));// 圆形的颜色
        lineDataSet.setHighLightColor(Color.parseColor("#2B8AC2")); // 高亮的线的颜色
        lineDataSet.setDrawFilled(false);    //设置允许填充
        lineDataSet.setFillColor(Color.parseColor("#2B8AC2"));
        ArrayList<LineDataSet> lineDataSets = new ArrayList<LineDataSet>();
        lineDataSets.add(lineDataSet); // add the datasets
        LineData lineData = new LineData(xValues, lineDataSets);
        return lineData;
    }

    @Override
    public void onValueSelected(Entry entry, int i, Highlight highlight) {
       // content_chart.setVisibility(View.VISIBLE);
        //tv_content_value.setText(mFormat.format(entry.getVal())+"℃");

        if(unitStyle==0) {
            tempc=entry.getVal();
            tempf= (float) (entry.getVal()*1.8+32);
            rb_content_value.setProgress(entry.getVal(),"℃");
        }else{
            rb_content_value.setProgress(entry.getVal(),"℉");
            tempc=list.get(i).getTemp();
            tempf= (float) (entry.getVal()*1.8+32);
        }

        //showValue(i);
    }
    @Override
    public void onNothingSelected() {
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_content_left:
                finish();
                break;
            case R.id.tv_content_right:
                ImportDialog importDialog=new ImportDialog(mContext,d,list,unitStyle);
                importDialog.show();
                break;
            case R.id.tv_content_c:
                tv_content_c.setSelected(true);
                tv_content_f.setSelected(false);
                unitStyle=0;
                tv_content_c.setTextColor(Color.WHITE);
                tv_content_f.setTextColor(getResources().getColor(R.color.content_textcolor));
                lineChart.setData(getLineDatac(list));
                lineChart.notifyDataSetChanged();
                lineChart.invalidate();
                rb_content_value.setProgress(tempc,"℃");
                break;
            case R.id.tv_content_f:
                tv_content_c.setSelected(false);
                tv_content_f.setSelected(true);
                tv_content_f.setTextColor(Color.WHITE);
                tv_content_c.setTextColor(getResources().getColor(R.color.content_textcolor));
                lineChart.setData(getLineDataf(list));
                lineChart.notifyDataSetChanged();
                lineChart.invalidate();
                rb_content_value.setProgress(tempf,"℉");
                unitStyle=1;
                break;
        }
    }
    /*
       初始化过度动画
        */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void initWindowTrantation(Transition transition) {
        getWindow().setReturnTransition(transition);
        getWindow().setEnterTransition(transition);
        getWindow().setExitTransition(transition);
        getWindow().setReenterTransition(transition);
    }
    //确定屏幕方向
    protected int getOrientation() {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    }

    /*
    过度页面切换动画
    默认是淡入淡出
     */
    protected Transition getWindowTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new Slide();
        }
        return new Fade();
    }

}
