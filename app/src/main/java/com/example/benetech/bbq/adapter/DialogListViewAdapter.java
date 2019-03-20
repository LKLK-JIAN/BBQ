package com.example.benetech.bbq.adapter;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.example.benetech.bbq.R;

import java.util.List;


/**
 * Created by android on 2018/4/16.
 */

public class DialogListViewAdapter extends BaseAdapter {

    public List<BluetoothDevice> list;
    public Context context;

    public DialogListViewAdapter(Context context, List<BluetoothDevice> list) {
        this.list = list;
        this.context=context;
    }

    public void addDevice(BluetoothDevice device){
        list.add(device);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder();
            convertView= LayoutInflater.from(context).inflate(R.layout.dialog_listview_item,null);
            viewHolder.blename=convertView.findViewById(R.id.ble_name);
            viewHolder.blemac=convertView.findViewById(R.id.ble_mac);
            if (list.get(position).getName() != null && list.get(position).getName() .length() > 0) {
                viewHolder.blename.setText(list.get(position).getName());
            } else {
                viewHolder.blename.setText("unknow-device");
            }

            viewHolder.blemac.setText(list.get(position).getAddress());
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    final class ViewHolder{
        public TextView blename;
        public TextView blemac;
    }
}
