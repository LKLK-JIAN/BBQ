package com.example.benetech.bbq.dialog;


import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import com.example.benetech.bbq.R;
import com.example.benetech.bbq.adapter.DialogListViewAdapter;
import com.example.benetech.bbq.bluetoothapi.APIBuletooth;


import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by android on 2018/4/16.
 */

public class ConnectDialog extends Dialog {
    private Context context;
    private DialogListViewAdapter adapter;
    protected List<BluetoothDevice> list = new ArrayList<>();
    private ListView dialogList;
    private ImageView dialog_search;
    private ImageView dialog_search_b;
    private Handler handler = new Handler();


    public ConnectDialog(Context context, int style) {
        super(context, style);
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        this.context = context;
        add();
    }

    public void add() {
        setContentView(R.layout.dialog_list);
        if (!APIBuletooth.getInstance(context).isOpen()) {
            APIBuletooth.getInstance(context).bleOpen();
        }
        dialogList = findViewById(R.id.dialog_listview);
        dialog_search = findViewById(R.id.dialog_search);
        dialog_search_b = findViewById(R.id.dialog_search_b);
        adapter = new DialogListViewAdapter(context, list);
        dialogList.setAdapter(adapter);
        final AnimationDrawable animationDrawable = (AnimationDrawable) dialog_search_b
                .getBackground();
        dialog_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationDrawable.start();
                dialog_search.setVisibility(View.GONE);
                dialog_search_b.setVisibility(View.VISIBLE);
                APIBuletooth.getInstance(context).scanBLE(new BluetoothAdapter.LeScanCallback() {
                    @Override
                    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                        Log.e(TAG, "onLeScan: 777777777777777777");
                        if (!list.contains(device)) {
                            adapter.addDevice(device);
                        }
                    }
                }, 5000);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animationDrawable.stop();
                        dialog_search.setVisibility(View.VISIBLE);
                        dialog_search_b.setVisibility(View.GONE);
                    }
                }, 5000);
            }
        });
    }

    public ListView getListView() {
        return dialogList;
    }

    public List<BluetoothDevice> getList() {
        return list;
    }

//    public static class Builder {

//        private DialogListViewAdapter adapter;
//        protected List<BluetoothDevice> list;
//        private View layout;
//        private ConnectDialog dialog;
//
//        public Builder(final Context context) {
//            //这里传入自定义的style，直接影响此Dialog的显示效果。style具体实现见style.xml
//            dialog = new ConnectDialog(context);
//            list = new ArrayList<>();
//            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            layout = inflater.inflate(R.layout.dialog_list, null);
//            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            layout.findViewById(R.id.dialog_search).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                   Log.e(TAG, "onClick: 搜索蓝牙");
//                  BluetoothAdapter mBluetoothAdapter = ((BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE)).getAdapter();
//                    mBluetoothAdapter.startLeScan(new BluetoothAdapter.LeScanCallback() {
//                        @Override
//                        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
//                            Log.e("TAG", "onLeScan:111 ");
//                            if (!list.contains(device)) {
//                                list.add(device);
//                           }
//                           if (list != null) {
//                               ((ListView) layout.findViewById(R.id.dialog_listview)).setAdapter(new DialogListViewAdapter(context, list));
//
//                          }
//                        }
//                    });
//                    ((ListView) layout.findViewById(R.id.dialog_listview)).setOnClickListener(View.OnClickListener hh);
//
//                    APIBuletooth.getInstance(context).scanBLE(new BluetoothAdapter.LeScanCallback() {
//                        @Override
//                        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
//                            Log.e("TAG", "onLeScan:111 ");
//                            if (!list.contains(device)) {
//                                list.add(device);
//                            }
//                            if (list != null) {
//                                ((ListView) layout.findViewById(R.id.dialog_listview)).setAdapter(new DialogListViewAdapter(context, list));
//
//                            }
//                        }
//                    },5000);
//                }
//
//            });
//        }
//
//        public ConnectDialog getDialog() {
//            dialog.setContentView(R.layout.dialog_list);
//            return dialog;
//        }
//        public View getLayout(){
//            return layout;
//        }
//        public List<BluetoothDevice> getList(){
//            return list;
//        }
//    }
}
