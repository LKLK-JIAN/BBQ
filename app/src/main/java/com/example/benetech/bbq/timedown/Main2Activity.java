package com.example.benetech.bbq.timedown;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.benetech.bbq.R;
import com.example.benetech.bbq.adapter.ListviewTimedownAdapter;
import java.util.ArrayList;


public class Main2Activity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_activity2_additem;
    private ImageView  iv_activity2_back;
    private ListView lv_mainactivity_timedown;
    private ArrayList<Integer> list;
    private ListviewTimedownAdapter adapter;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        tv_activity2_additem=findViewById(R.id.tv_activity2_additem);
        iv_activity2_back=findViewById(R.id.iv_activity2_back);
        lv_mainactivity_timedown=findViewById(R.id.lv_mainactivity_timedown);
        tv_activity2_additem.setOnClickListener(this);
        iv_activity2_back.setOnClickListener(this);
        context=this;
        list=new ArrayList<>();
        list.add(10000);
        list.add(20000);
        adapter=new ListviewTimedownAdapter(list,context);
        lv_mainactivity_timedown.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_activity2_back:
                Log.e("TAG", "onClick: 88888888888" );
                break;
            case R.id.tv_activity2_additem:
                Log.e("TAG", "onClick: 88888888888" );
                adapter.addItemTimedown(50000);
                break;
        }
    }


}
