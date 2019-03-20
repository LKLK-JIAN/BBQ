    package com.example.benetech.bbq;


    import android.app.AlertDialog;
    import android.content.Context;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.database.Cursor;
    import android.graphics.Color;
    import android.os.Bundle;
    import android.support.v4.content.ContextCompat;
    import android.support.v7.app.AppCompatActivity;
    import android.support.v7.widget.DividerItemDecoration;
    import android.support.v7.widget.LinearLayoutManager;
    import android.support.v7.widget.RecyclerView;
    import android.util.Log;
    import android.view.View;
    import android.view.Window;
    import android.widget.Button;
    import android.widget.ImageView;
    import android.widget.LinearLayout;
    import android.widget.TextView;
    import com.example.benetech.bbq.adapter.MineRadioAdapter;
    import com.example.benetech.bbq.basesqlite.TempMeterService;
    import com.example.benetech.bbq.bean.Document;

    import java.util.ArrayList;
    import java.util.List;

    public class DocumentActivity extends AppCompatActivity implements MineRadioAdapter.OnItemClickListener,View.OnClickListener {

        private TempMeterService service;
        private Cursor cursor;
        private ArrayList<Document> list;
        private TextView tv_action_title, tv_action_right;
        private ImageView tv_action_left;


        private static final int MYLIVE_MODE_CHECK = 0;
        private static final int MYLIVE_MODE_EDIT = 1;

        private RecyclerView mRecyclerview;
        TextView mTvSelectNum;
        Button mBtnDelete;
        TextView mSelectAll;
        LinearLayout mLlMycollectionBottomDialog;
        TextView mBtnEditor;
        private MineRadioAdapter mRadioAdapter = null;
        private LinearLayoutManager mLinearLayoutManager;
        private List<Document> mList = new ArrayList<>();
        private int mEditMode = MYLIVE_MODE_CHECK;
        private boolean isSelectAll = false;
        private boolean editorStatus = false;
        private int index = 0;
        private SharedPreferences sharedPreferences;
        private SharedPreferences.Editor editor;
        private String tempunit;

        private Context mContext;
        private int channel;
        private Intent intent;
        private String channeltable;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // 无标题栏，无状态栏
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            //取消状态栏
            getSupportActionBar().hide();
            setContentView(R.layout.activity_document);
            intent=getIntent();
            mContext=this;
            channel=intent.getIntExtra("channel",0);
            switch (channel){
                case 1:
                    channeltable="channelone";
                    break;
                case 2:
                    channeltable="channeltwo";
                    break;
                case 3:
                    channeltable="channelthree";
                    break;
                case 4:
                    channeltable="channelfour";
                    break;
            }
            list = new ArrayList<>();
            service = new TempMeterService (mContext);
            cursor = service.getAllDocument(channel);
            Log.e("TAG", "onCreate: "+cursor );
            mRecyclerview = findViewById(R.id.record_his_contenttitlt);
            mTvSelectNum = findViewById(R.id.tv_select_num);
            mBtnDelete = findViewById(R.id.btn_delete);
            mSelectAll = findViewById(R.id.select_all);
            mLlMycollectionBottomDialog = findViewById(R.id.ll_mycollection_bottom_dialog);
            tv_action_left = findViewById(R.id.record_actionbar_left);
            tv_action_title =findViewById(R.id.record_actionbar_center);
            mBtnEditor = findViewById(R.id.record_actionbar_right);
            //tv_action_title.setText(getResources().getString(R.string.his_title));
            mBtnEditor.setText(mContext.getResources().getString(R.string.history_edit));
            tv_action_title.setText(mContext.getResources().getString(R.string.history_record));
            tv_action_left.setOnClickListener(this);
            Log.e("TAG", "onCreate:99999999 "+cursor );
            while (cursor.moveToNext()) {
                int mID=cursor.getInt(cursor.getColumnIndex("_id"));
                String mTitle=cursor.getString(cursor.getColumnIndex("file_title"));
                String mDate=cursor.getString(cursor.getColumnIndex("file_time"));
                Document d = new Document(mID,mTitle,mDate);
                Log.e("TAG", "onCreate:99999999999 "+mID );
    //            d.setmID(cursor.getInt(cursor.getColumnIndex("mID")));
    //            d.setmTitle(cursor.getString(cursor.getColumnIndex("mTitle")));
    //            d.setmDate(cursor.getString(cursor.getColumnIndex("mDate")));
    //            d.setmTime(cursor.getInt(cursor.getColumnIndex("mTime")));
    //            d.setmMaxValue(cursor.getInt(cursor.getColumnIndex("mMaxValue")));
    //            d.setmMinValue(cursor.getInt(cursor.getColumnIndex("mMinValue")));
    //            d.setmAvgValue(cursor.getInt(cursor.getColumnIndex("mAvgValue")));
                list.add(d);
        }
            mRadioAdapter = new MineRadioAdapter(this);
            mLinearLayoutManager = new LinearLayoutManager(this);
            mRecyclerview.setLayoutManager(mLinearLayoutManager);
            DividerItemDecoration itemDecorationHeader = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
            //itemDecorationHeader.setDividerDrawable(ContextCompat.getDrawable(this, R.drawable.divider_main_bg_height_1));
            mRecyclerview.addItemDecoration(itemDecorationHeader);
            mRecyclerview.setAdapter(mRadioAdapter);
            mRadioAdapter.notifyAdapter(list, false);

            initListener();

            /**
             * 根据选择的数量是否为0来判断按钮的是否可点击.
             *
             * @param size
             */

            //adapter = new HistoricalAdapter(mContext, list);
            //lv_historicalre_recard.setAdapter(adapter);
    //        lv_historicalre_recard.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    //            @Override
    //            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
    //                int mID = list.get(i).getmID();
    //                ArrayList<Integer> valueList = new ArrayList<>();
    //                Cursor cursor = service.getAllValue(mID);
    //                while (cursor.moveToNext()) {
    //                    valueList.add(cursor.getInt(cursor.getColumnIndex("mTemValue")));
    //                }
    //                Document document = service.getDocument(mID);
    //                Intent intent = new Intent(mContext, ContentActivity.class);
    //                Bundle bundle = new Bundle();
    //                bundle.putSerializable("document", document);
    //                bundle.putIntegerArrayList("arraylist", valueList);
    //                bundle.putInt("type",1);  //0:代表实时 1：代表记录进去
    //                intent.putExtras(bundle);
    //                startActivity(intent);
    //            }
    //        });
        }

        private void setBtnBackground(int size) {
            if (size != 0) {
                mBtnDelete.setBackgroundResource(R.drawable.button_shape);
                mBtnDelete.setEnabled(true);
                mBtnDelete.setTextColor(Color.WHITE);
            } else {
                mBtnDelete.setBackgroundResource(R.drawable.button_noclickable_shape);
                mBtnDelete.setEnabled(false);
                mBtnDelete.setTextColor(ContextCompat.getColor(this, R.color.color_b7b8bd));
            }
        }


        /**
         * 全选和反选
         */
        private void selectAllMain() {
            if (mRadioAdapter == null) return;
            if (!isSelectAll) {
                for (int i = 0, j = mRadioAdapter.getMyLiveList().size(); i < j; i++) {
                    mRadioAdapter.getMyLiveList().get(i).setSelect(true);
                }
                index = mRadioAdapter.getMyLiveList().size();
                mBtnDelete.setEnabled(true);
                mSelectAll.setText(mContext.getResources().getString(R.string.his_cancelallselect));
                isSelectAll = true;
            } else {
                for (int i = 0, j = mRadioAdapter.getMyLiveList().size(); i < j; i++) {
                    mRadioAdapter.getMyLiveList().get(i).setSelect(false);
                }
                index = 0;
                mBtnDelete.setEnabled(false);
                mSelectAll.setText(mContext.getResources().getString(R.string.his_allselect));
                isSelectAll = false;
            }
            mRadioAdapter.notifyDataSetChanged();
            setBtnBackground(index);
            mTvSelectNum.setText(String.valueOf(index));
        }

        /**
         * 删除逻辑
         */
        private void deleteVideo() {
            if (index == 0) {
                mBtnDelete.setEnabled(false);
                return;
            }
            final AlertDialog builder = new AlertDialog.Builder(this)
                    .create();
            builder.show();
            if (builder.getWindow() == null) return;
            builder.getWindow().setContentView(R.layout.pop_user);//设置弹出框加载的布局
            TextView msg = (TextView) builder.findViewById(R.id.tv_msg);
            Button cancle = (Button) builder.findViewById(R.id.btn_cancel);
            Button sure = (Button) builder.findViewById(R.id.btn_sure);
            if (msg == null || cancle == null || sure == null) return;

            if (index == 1) {
                msg.setText(mContext.getResources().getString(R.string.his_deleteitem));
            } else {
                msg.setText(mContext.getResources().getString(R.string.his_deleteitem));
            }
            cancle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder.dismiss();
                }
            });
            sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = mRadioAdapter.getMyLiveList().size(), j = 0; i > j; i--) {
                        Document myLive = mRadioAdapter.getMyLiveList().get(i - 1);
                        if (myLive.isSelect()) {
                            mRadioAdapter.getMyLiveList().remove(myLive);
                            service.deleteDocument(myLive.getId());
                            service.deleteMeterdata(myLive.getId(),channeltable);
                            index--;

                        }
                    }
                    index = 0;
                    mTvSelectNum.setText(String.valueOf(0));
                    setBtnBackground(index);
                    if (mRadioAdapter.getMyLiveList().size() == 0) {
                        mLlMycollectionBottomDialog.setVisibility(View.GONE);
                    }
                    mRadioAdapter.notifyDataSetChanged();
                    builder.dismiss();
                }
            });
        }

        private void updataEditMode() {
            mEditMode = mEditMode == MYLIVE_MODE_CHECK ? MYLIVE_MODE_EDIT : MYLIVE_MODE_CHECK;
            if (mEditMode == MYLIVE_MODE_EDIT) {
                mBtnEditor.setText(mContext.getResources().getString(R.string.his_cancle));
                mLlMycollectionBottomDialog.setVisibility(View.VISIBLE);
                editorStatus = true;
            } else {
                mBtnEditor.setText(mContext.getResources().getString(R.string.history_edit));
                mLlMycollectionBottomDialog.setVisibility(View.GONE);
                editorStatus = false;
                clearAll();
            }
            mRadioAdapter.setEditMode(mEditMode);
        }


        private void clearAll() {
            mTvSelectNum.setText(String.valueOf(0));
            isSelectAll = false;
            mSelectAll.setText("全选");
            setBtnBackground(0);
        }


        private void initListener() {
            mRadioAdapter.setOnItemClickListener(this);
            mBtnDelete.setOnClickListener(this);
            mSelectAll.setOnClickListener(this);
            mBtnEditor.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.record_actionbar_left:
                    finish();
                    break;
                case R.id.btn_delete:
                    deleteVideo();
                    break;
                case R.id.select_all:
                    selectAllMain();
                    break;
                case R.id.record_actionbar_right:
                    updataEditMode();
                    break;
            }
        }

        @Override
        public void onItemClickListener(int pos, List<Document> myLiveList) {
            if (editorStatus) {
                Document myLive = myLiveList.get(pos);
                boolean isSelect = myLive.isSelect();
                if (!isSelect) {
                    index++;
                    myLive.setSelect(true);
                    if (index == myLiveList.size()) {
                        isSelectAll = true;
                        mSelectAll.setText(mContext.getResources().getString(R.string.his_cancelallselect));
                    }
                } else {
                    myLive.setSelect(false);
                    index--;
                    isSelectAll = false;
                    mSelectAll.setText(mContext.getResources().getString(R.string.his_allselect));
                }
                setBtnBackground(index);
                mTvSelectNum.setText(String.valueOf(index));
                mRadioAdapter.notifyDataSetChanged();
            } else {
                int mID = list.get(pos).getId();
                //  ArrayList<DBValue> valueList = new ArrayList<>();
                // Cursor cursor = service.getAllValue(mID);
    //            while (cursor.moveToNext()) {
    //                int dbvalue=cursor.getInt(cursor.getColumnIndex("mNoiseValue"));
    //                int maxormin=cursor.getInt(cursor.getColumnIndex(""));
    //                int hold=cursor.getInt(cursor.getColumnIndex(""));
    //                int fastorslow=cursor.getInt(cursor.getColumnIndex(""));
    //                int ac=cursor.getInt(cursor.getColumnIndex(""));
    //                valueList.add(new DBValue());
    //            }
    //            int max = service.getMaxValue(mID);
    //            int min = service.getMinValue(mID);
    //            int avg = service.getAvgValue(mID);
                Document document = service.getDocument(mID);
                Intent intent1 = new Intent(mContext, ContentActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("document", document);
                // bundle.putParcelableArray("arraylist", valueList);
                //  bundle.putInt("type", 1);  //0:代表实时 1：代表记录进去
    //            bundle.putInt("max", max);
    //            bundle.putInt("min", min);
    //            bundle.putInt("avg", avg);
                intent1.putExtras(bundle);
                startActivity(intent1);
            }
        }

    }
