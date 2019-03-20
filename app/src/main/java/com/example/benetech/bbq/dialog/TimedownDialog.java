package com.example.benetech.bbq.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.benetech.bbq.R;
import com.example.benetech.bbq.view.EasyPickerView;
import java.util.ArrayList;

public class TimedownDialog extends Dialog {


    private Button timedown_yes;//确定按钮
    private Button timedown_no;//取消按钮
    public EasyPickerView epv_timedown_h,epv_timedown_m;

    private ArrayList<String> datah=new ArrayList<>();
    private ArrayList<String> datam=new ArrayList<>();
    //确定文本和取消文本的显示内容
    private int hour, minute;
    private onNoOnclickListener noOnclickListener;//取消按钮被点击了的监听器
    private onYesOnclickListener yesOnclickListener;//确定按钮被点击了的监听器


    public TimedownDialog(@NonNull Context context) {
        super(context,R.style.BottomDialog);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
    }


    /**
     * 设置取消按钮的显示内容和监听
     *
     * @param onNoOnclickListener
     */
    public void setNoOnclickListener( onNoOnclickListener onNoOnclickListener) {

        this.noOnclickListener = onNoOnclickListener;
    }

    /**
     * 设置确定按钮的显示内容和监听
     *
     * @param onYesOnclickListener
     */
    public void setYesOnclickListener(onYesOnclickListener onYesOnclickListener) {

        this.yesOnclickListener = onYesOnclickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //按空白处不能取消动画
        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();
        //初始化界面控件的事件
        initEvent();
    }

    /**
     * 初始化界面的确定和取消监听器
     */
    private void initEvent() {

        //设置确定按钮被点击后，向外界提供监听
        timedown_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yesOnclickListener !=null ) {
                    yesOnclickListener.onYesClick(hour,minute);
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听
        timedown_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noOnclickListener !=null ) {
                    noOnclickListener.onNoClick();
                }
            }
        });
        epv_timedown_h.setOnScrollChangedListener(new EasyPickerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int curIndex) {
                hour=curIndex;
            }
            @Override
            public void onScrollFinished(int curIndex) {
                hour=curIndex;
            }
        });
        epv_timedown_m.setOnScrollChangedListener(new EasyPickerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int curIndex) {
                minute=curIndex;
            }

            @Override
            public void onScrollFinished(int curIndex) {
                minute=curIndex;
            }
        });
    }

    /**
     * 初始化界面控件的显示数据
     */
    public  void setData(int time){
       int h=time/3600;
       int m=time%3600/60;
       int s=time%3600%60;
       epv_timedown_h.moveTo(h);
       epv_timedown_m.moveTo(m);
    }

    /**
     * 初始化界面控件
     */
    public void initView() {
        setContentView(R.layout.dialog_time_down_set);
        epv_timedown_h=findViewById(R.id.epv_timedown_h);
        epv_timedown_m=findViewById(R.id.epv_timedown_m);
        timedown_yes=findViewById(R.id.timedown_yes);
        timedown_no=findViewById(R.id.timedown_no);
        for(int i=0;i<60;i++){
            datam.add(i+"");
        }
        for(int i=0;i<6;i++){
            datah.add(i+"");
        }
        epv_timedown_h.setDataList(datah);
        epv_timedown_m.setDataList(datam);
    }

    /**
     * 设置确定按钮和取消被点击的接口
     */
    public interface onYesOnclickListener {
        public void onYesClick(int hour,int minute);
    }

    public interface onNoOnclickListener {
        public void onNoClick();
    }

}
