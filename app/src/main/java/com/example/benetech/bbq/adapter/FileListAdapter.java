package com.example.benetech.bbq.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.benetech.bbq.R;
import com.example.benetech.bbq.bean.Filedocument;


import java.util.ArrayList;

public class FileListAdapter extends BaseAdapter {
    private ArrayList<String> item;
    private Context context;
    private View view;
    private ArrayList<Filedocument> filedocuments;
    private static final int MYLIVE_MODE_CHECK = 0;
    private boolean mEditMode=false;


    public FileListAdapter(ArrayList<String> item, Context context) {
        this.item = item;
        this.context = context;
    }

    public FileListAdapter( Context context, ArrayList<Filedocument> filedocuments) {

        this.context = context;
        this.filedocuments = filedocuments;
    }

    @Override
    public int getCount() {
        return filedocuments.size();
    }

    @Override
    public Object getItem(int position) {
        return filedocuments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewFile mViewHistorical=null;
        if(mViewHistorical==null){
            mViewHistorical=new ViewFile();
            view= LayoutInflater.from(context).inflate(R.layout.filelist_layout,null);
            mViewHistorical.tv_file_name=view.findViewById(R.id.tv_file_name);
            mViewHistorical.isSelect=view.findViewById(R.id.iv_check);
            view.setTag(mViewHistorical);
        }else{
            view.getTag();
        }
        Filedocument document=filedocuments.get(position);

        if (!mEditMode) {
            mViewHistorical.isSelect.setVisibility(View.GONE);
        } else {
            mViewHistorical.isSelect.setVisibility(View.VISIBLE);
            if (document.getSelected()) {
                mViewHistorical.isSelect.setImageResource(R.mipmap.ic_checked);
            } else {
                mViewHistorical.isSelect.setImageResource(R.mipmap.ic_uncheck);
            }
        }
        mViewHistorical.tv_file_name.setText(document.getFileName());
        return view;
    }

    public boolean ismEditMode() {
        return mEditMode;
    }

    public void setmEditMode(boolean mEditMode) {
        this.mEditMode = mEditMode;
        notifyDataSetChanged();
    }

    public ArrayList<Filedocument> getFiledocuments() {
        return filedocuments;
    }

    public void setFiledocuments(ArrayList<Filedocument> filedocuments) {
        this.filedocuments = filedocuments;
        notifyDataSetChanged();
    }
}


class ViewFile{
    public TextView tv_file_name;
    public ImageView isSelect;

}
