package com.example.benetech.bbq.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.benetech.bbq.DocumentActivity;
import com.example.benetech.bbq.R;
import com.example.benetech.bbq.bean.Document;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by guohao on 2017/9/6.
 */

public class MineRadioAdapter extends RecyclerView.Adapter<MineRadioAdapter.ViewHolder> {

    private static final int MYLIVE_MODE_CHECK = 0;
    int mEditMode = MYLIVE_MODE_CHECK;

    private int secret = 0;
    private String title = "";
    private Context context;
    private List<Document> mMyLiveList;
    private OnItemClickListener mOnItemClickListener;


    public MineRadioAdapter(Context context) {
        this.context = context;

    }


    public void notifyAdapter(List<Document> myLiveList, boolean isAdd) {
        if (!isAdd) {
            this.mMyLiveList = myLiveList;
        } else {
            this.mMyLiveList.addAll(myLiveList);
        }
        notifyDataSetChanged();
    }

    public List<Document> getMyLiveList() {
        if (mMyLiveList == null) {
            mMyLiveList = new ArrayList<>();
        }
        return mMyLiveList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_live, parent, false);
        ViewHolder holder = new ViewHolder(view);
        holder.mCheckBox = view.findViewById(R.id.check_box);
        holder.mRadioImg = view.findViewById(R.id.radio_img);
        holder.mTvTitle = view.findViewById(R.id.tv_historicalitem_title);
        holder.tv_historical_time = view.findViewById(R.id.tv_historicalitem_time);
        return holder;
    }

    @Override
    public int getItemCount() {
        return mMyLiveList.size();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Document d = mMyLiveList.get(holder.getAdapterPosition());
        holder.mTvTitle.setText(d.getFile_title() + "");

        //holder.mTvSource.setText(myLive.getSource());
        if (mEditMode == MYLIVE_MODE_CHECK) {
            holder.mCheckBox.setVisibility(View.GONE);
        } else {
            holder.mCheckBox.setVisibility(View.VISIBLE);
            if (d.isSelect()) {
                holder.mCheckBox.setImageResource(R.mipmap.ic_checked);
            } else {
                holder.mCheckBox.setImageResource(R.mipmap.ic_uncheck);
            }
        }
        //   Document d=mMyLiveList.get(position);
        holder.tv_historical_time.setText(d.getFile_time());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClickListener(holder.getAdapterPosition(), mMyLiveList);
            }
        });
    }

    public void setOnItemClickListener(DocumentActivity onItemClickListener) {
        this.mOnItemClickListener = (OnItemClickListener) onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClickListener(int pos, List<Document> document);
    }

    public void setEditMode(int editMode) {
        mEditMode = editMode;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView mRadioImg;
        TextView mTvTitle;
        ImageView mCheckBox;
        TextView tv_historical_time;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
