package com.example.benetech.bbq.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.benetech.bbq.R;

import java.util.ArrayList;
import static com.example.benetech.bbq.wight.PickerView.TAG;


public class ListviewTimedownAdapter extends BaseAdapter {

    private ArrayList<Integer> list;
    private Context context;
    private View view;


    public ListviewTimedownAdapter(ArrayList<Integer> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void addItemTimedown(int time){
        Log.e(TAG, "getCount:8888888888888888 "+list.size() );
        list.add(time);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       ViewTimedown mViewHistorical=null;
        if(mViewHistorical==null){
            mViewHistorical=new ViewTimedown();
            view= LayoutInflater.from(context).inflate(R.layout.listview_timedown_item,null);
            mViewHistorical.tv_timedown_result=view.findViewById(R.id.tv_timedown_result);
            mViewHistorical.btn_timedown_start=view.findViewById(R.id.btn_timedown_start);
            final ViewTimedown finalMViewHistorical = mViewHistorical;
            mViewHistorical.btn_timedown_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new CountDownTimer(list.get(position), 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            finalMViewHistorical.tv_timedown_result.setText(millisUntilFinished/1000+"");
                        }

                        @Override
                        public void onFinish() {
                            finalMViewHistorical.tv_timedown_result.setText("结束了");
                        }
                    }.start();

                }
            });
            view.setTag(mViewHistorical);
        }else{
            view.getTag();
        }
        return view;
    }
}

    class ViewTimedown{
        public TextView tv_timedown_result;
        public Button btn_timedown_start;
}
