package com.example.benetech.bbq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import com.example.benetech.bbq.R;
import com.example.benetech.bbq.view.EasyPickerView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ReminderDialog extends Dialog {

    private EasyPickerView  epv_reminder_sound;
    private int location;
    private Button reminder_yes;//确定按钮
    private Button reminder_no;//取消按钮
    private ArrayList<String>  data=new ArrayList<>();
    private onNoOnclickListener onNoOnclickListener;
    private onYesOnclickListener onYesOnclickListener;

    private int rawId;
    private String rawName;
    private Field[] fields;
    public MediaPlayer mp;
    private Context context;


    public ReminderDialog(@NonNull Context context) {
        super(context,R.style.BottomDialog);
        this.context=context;
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = getWindow();
        lp.copyFrom(window.getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
    }


    /**
     * 设置确定和取消的按钮的监听
     *
     */
    public void setOnNoOnclickListener(onNoOnclickListener onNoOnclickListener){
        this.onNoOnclickListener=onNoOnclickListener;
    }

    public void setOnYesOnclickListener(onYesOnclickListener onYesOnclickListener){
        this.onYesOnclickListener=onYesOnclickListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_reminder);
        //点击空白处，不能取消动画
        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();
        //初始化界面控件事件
        initEvent();

    }


    //初始化界面控件
    private void initView(){
        epv_reminder_sound=findViewById(R.id.epv_reminder_sound);
        reminder_no=findViewById(R.id.reminder_no);
        reminder_yes=findViewById(R.id.reminder_yes);
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
        epv_reminder_sound.setDataList(data);
        epv_reminder_sound.setOnScrollChangedListener(new EasyPickerView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(int curIndex) throws IllegalAccessException {
                if(mp!=null){
                    mp.pause();
                }
                location=curIndex;
                rawId=fields[curIndex].getInt(R.raw.class);
                mp=MediaPlayer.create(context,rawId);
                mp.start();
            }

            @Override
            public void onScrollFinished(int curIndex) throws IllegalAccessException {
                if(mp!=null){
                    mp.pause();
                }
                location=curIndex;
                rawId=fields[curIndex].getInt(R.raw.class);
                mp=MediaPlayer.create(context,rawId);
                mp.start();
            }
        });
    }

    //初始化界面控件事件
    private void initEvent(){
        reminder_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onNoOnclickListener!=null){
                    onNoOnclickListener.onNoClick();
                }
            }
        });
        reminder_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onYesOnclickListener!=null){
                    onYesOnclickListener.onYesClick(location);
                }
            }
        });

    }

    /**
     * 设置确定和取消按钮点击
     */
    public interface onYesOnclickListener{
        public void onYesClick(int location);
    }
    public interface onNoOnclickListener{
        public void onNoClick();
    }
}
