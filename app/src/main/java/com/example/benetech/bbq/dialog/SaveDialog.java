package com.example.benetech.bbq.dialog;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import com.example.benetech.bbq.R;
import com.example.benetech.bbq.basesqlite.TempMeterService;
import com.example.benetech.bbq.bean.Document;
import com.example.benetech.bbq.bean.TemValue;
import com.example.benetech.bbq.view.ToastUtils;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SaveDialog extends Dialog implements View.OnClickListener {
    private ArrayList<TemValue> list;
    private Context context;
    private Button save;
    private EditText et_save_title;
    private Document d;
    private CustomProgressDialog progressDialog;
    private String channeltable;
    private int channel;

    public SaveDialog(@NonNull Context context, ArrayList<TemValue> list, String channeltable,int channel) {
        super(context);
        this.context=context;
        this.list=list;
        this.channeltable=channeltable;
        d=new Document();
        this.channel=channel;
        setContentView(R.layout.save_dialog);
        save=findViewById(R.id.save);
        save.setOnClickListener(this);
        et_save_title=findViewById(R.id.et_save_title);
        progressDialog = CustomProgressDialog.createDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.saving));
        progressDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕progressDialog不消失
        progressDialog.setCancelable(false); // 设置点击返回键progressDialog不消失
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.save:
               Save();
                break;
        }
    }


    private void addInfo(){
        if(et_save_title.getText().toString().trim().equals("")){
            ToastUtils.showToast(context,context.getResources().getString(R.string.no_title));
        }
        else{
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "yyyy-MM-dd HH:MM:SS");
            Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
            String time = formatter.format(curDate);
            d.setFile_title(et_save_title.getText().toString().trim());
            d.setFile_time(time);
            d.setChannel(channel);
            TempMeterService service=new TempMeterService(context);
            service.insertDocument(d);
            Document mID=service.getTopDocument();
            service.allInsert(list,mID,channeltable);
            service.closeDatabase();
        }
    }

    //保存
    public void Save() {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.click_in);
        save.startAnimation(animation);
        if (et_save_title.getText().toString().trim().length() == 0) {
            ToastUtils.showToast(context,context.getResources().getString(R.string.no_title));
        } else {
            dismiss();
            progressDialog = CustomProgressDialog.createDialog(context);
            progressDialog.setMessage(context.getResources().getString(R.string.saving));
            progressDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕progressDialog不消失
            progressDialog.setCancelable(false); // 设置点击返回键progressDialog不消失
            progressDialog.show();
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    try {
                        sleep(300);
                        addInfo();
                        progressDialog.dismiss();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    }
}
