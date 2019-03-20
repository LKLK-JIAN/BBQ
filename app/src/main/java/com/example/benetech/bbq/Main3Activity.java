package com.example.benetech.bbq;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.benetech.bbq.base.mBaseActivity;
import com.example.benetech.bbq.bluetoothapi.APIData;
import com.example.benetech.bbq.bluetoothapi.BluetooehAttribute;
import com.example.benetech.bbq.dialog.ConnectDialog;
import com.example.benetech.bbq.dialog.CustomProgressDialog;
import com.example.benetech.bbq.wight.ShoppingCartView;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import static android.content.ContentValues.TAG;

public class Main3Activity extends mBaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    public static BluetoothGatt bluetoothGatt;
    public static BluetoothDevice device;
    public static BluetoothGattCharacteristic writeCharacteristic;
    public static BluetoothGattCharacteristic notificationCharacteristic;
    //定时器

    //连接蓝牙
    private ListView bluetooth_list;
    private ConnectDialog mConnectDialog;
    //加载框
    private CustomProgressDialog mCustomProgress;

    private Boolean isConnect = false;

    private ShoppingCartView scv_main3_channel;
    private TextView tv_main3_realtime, tv_main3_time, tv_main3_unit, tv_main3_battery;
    private EditText et_main3_settemp, et_main3_settime;
    private Button btn_main3_settemp, btn_main3_settime,btn_main3_unit;
    private RadioGroup rg_main3_unit;
    private TextView tv_main3_clear;
    private TextView tv_main3_result;
    private byte channel = 1;



    @Override
    protected int getLayoutID() {
        return R.layout.activity_main3;
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
      init();
    }

    private void init() {
        scv_main3_channel = findViewById(R.id.scv_main3_channel);
        scv_main3_channel.setNumListener(new ShoppingCartView.NumListener() {
            @Override
            public void getNum(int num) {
                channel = (byte) num;
            }
        });
        tv_main3_realtime = findViewById(R.id.tv_main3_realtime);
        tv_main3_realtime.setOnClickListener(this);
        tv_main3_time = findViewById(R.id.tv_main3_time);
        tv_main3_time.setOnClickListener(this);
        tv_main3_unit = findViewById(R.id.tv_main3_unit);
        tv_main3_unit.setOnClickListener(this);
        tv_main3_battery = findViewById(R.id.tv_main3_battery);
        tv_main3_battery.setOnClickListener(this);
        et_main3_settemp = findViewById(R.id.et_main3_settemp);
        et_main3_settime = findViewById(R.id.et_main3_settime);
        btn_main3_settemp = findViewById(R.id.btn_main3_settemp);
        btn_main3_settemp.setOnClickListener(this);
        btn_main3_settime = findViewById(R.id.btn_main3_settime);
        btn_main3_settime.setOnClickListener(this);
        btn_main3_unit=findViewById(R.id.btn_main3_unit);
        btn_main3_unit.setOnClickListener(this);
        rg_main3_unit = findViewById(R.id.rg_main3_unit);
        rg_main3_unit.setOnCheckedChangeListener(this);
        tv_main3_clear = findViewById(R.id.tv_main3_clear);
        tv_main3_clear.setOnClickListener(this);
        tv_main3_result = findViewById(R.id.tv_main3_result);
        tv_main3_result.setOnClickListener(this);
    }

    //显示连接dialog
    protected void showConnect() {
        mConnectDialog = new ConnectDialog(mContext, R.style.DialogTheme);
        bluetooth_list = mConnectDialog.getListView();
        bluetooth_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mConnectDialog.dismiss();
                mCustomProgress = CustomProgressDialog.createDialog(mContext);
                mCustomProgress.setMessage(getResources().getString(R.string.connect_dialog));
                mCustomProgress.setCanceledOnTouchOutside(false);// 设置点击屏幕progressDialog不消失
                mCustomProgress.show();
                String address = mConnectDialog.getList().get(i).getAddress();
                device = bluetoothAdapter.getRemoteDevice(address);
                bluetoothGatt = device.connectGatt(mContext, false, bluetoothGattCallback);
                Log.e(TAG, "onItemClick: "+bluetoothGatt );
            }
        });
        mConnectDialog.show();
    }

    private BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        /**
         * 返回链接状态
         */
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                //连接成功
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isConnect = true;

                        mCustomProgress.dismiss();
                        Toast.makeText(mContext,"连接成功",Toast.LENGTH_LONG).show();
                    }
                });
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isConnect = false;
                        //断开连接
                        Toast.makeText(mContext,"连接失败",Toast.LENGTH_LONG).show();
                    }
                });

            }
        }

        /**
         * 获取到链接设备的GATT服务时的回调
         */
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            if (BluetoothGatt.GATT_SUCCESS == status) {
                //gatt.getServices();
                String uuid = null;
                ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
                ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();
                for (BluetoothGattService gattService : gatt.getServices()) {
                    HashMap<String, String> currentServiceData = new HashMap<String, String>();
                    uuid = gattService.getUuid().toString();
                    Log.e(TAG, "onServicesDiscovered+gattService: " + uuid);
                    currentServiceData.put("uuid", uuid);
                    gattServiceData.add(currentServiceData);
                    ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();
                    List<BluetoothGattCharacteristic> gattCharacteristics = gattService
                            .getCharacteristics();
                    ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();
                    for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                        // 遍历每条服务里的所有Characteristic,保存所需Characteristics
                        Log.e(TAG, "characteristics uuid: " + gattCharacteristic.getUuid());
                        if (gattCharacteristic.getUuid().toString().equals(
                                BluetooehAttribute.Notification_charateristic)) {
                            //接收数据characteristic
                            notificationCharacteristic = gattCharacteristic;
                            //设置notification通知，按键回调在onCharacteristicChanged
                            //以下代码一定不能少，否则将无法接收到设备发送的数据
                            boolean isEnableNotification = bluetoothGatt.setCharacteristicNotification(
                                    notificationCharacteristic, true);
                            Log.e(TAG, "onServicesDiscovered:8888888888888 "+isEnableNotification );
                            if (isEnableNotification) {

                                BluetoothGattDescriptor descriptor = gattCharacteristic.getDescriptor(UUID
                                        .fromString(BluetooehAttribute.mluetoothGattDescriptor));
                                Log.e(TAG, "onServicesDiscovered: 8888888888"+descriptor );
                                if (descriptor != null) {
                                    Log.e(TAG, "onServicesDiscovered:8888888888888888887 " );
                                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                    bluetoothGatt.writeDescriptor(descriptor);
                                }
                            }
                        }
                        if (gattCharacteristic.getUuid().toString().equals(
                                BluetooehAttribute.Write_charateristic)) {
                            //写数据characteristic
                            writeCharacteristic = gattCharacteristic;
                        }
                    }
                }
            }
        }

        /**
         * 设备发出通知时会调用到该接口
         */
        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            Log.e(TAG, "onCharacteristicChanged:55555555555555555 " );
            final byte[] data=characteristic.getValue();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    StringBuffer stringBuffer=new StringBuffer();
                    for(byte i:data){
                        stringBuffer.append(i+"|");
                    }
                    stringBuffer.append("\n");
                    String result=tv_main3_result.getText().toString();
                    tv_main3_result.setText(result+stringBuffer.toString());
                    tv_main3_result.invalidate();
                }
            });
        }
    };

    //根据writeCharateristic写入数据
    public void writeData(byte[] bytes) {
        Log.e(TAG, "writeData: "+notificationCharacteristic);
        if (bluetoothGatt != null && writeCharacteristic != null) {
            Log.e(TAG, "writeData: ");
            writeCharacteristic.setValue(bytes);
            writeCharacteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
            bluetoothGatt.writeCharacteristic(writeCharacteristic);
        }
    }

    @Override
    public void onClick(View v) {
        if (isConnect) {
            switch (v.getId()) {
                case R.id.tv_main3_realtime:
                    writeData(APIData.getReal_time(channel));
                    break;
                case R.id.tv_main3_time:
                    writeData(APIData.getTime_data(channel));
                    break;
                case R.id.tv_main3_unit:
                    writeData(APIData.getUnit(channel));
//                    byte[] value = new byte[5];
//                    value[0] = 0x57;
//                    value[1] = 0x48;
//                    value[2] = 0x01;
//                    value[3] = 0x01;
//                    value[4] = 0x00;
//                    writeData(value);
                    break;
                case R.id.tv_main3_battery:
                    writeData(APIData.getBattry());
                    break;
                case R.id.btn_main3_settemp:
                    short temp = Short.parseShort(et_main3_settemp.getText().toString().trim());
                    writeData(APIData.setTemp_limit(temp, channel));
                    break;
                case R.id.btn_main3_settime:
                    int time = Integer.parseInt(et_main3_settime.getText().toString().trim());
                    writeData(APIData.setTiming_data(time, channel));
                    break;
                case R.id.btn_main3_unit:
                    int id = rg_main3_unit.getCheckedRadioButtonId();
                    Log.e(TAG, "onClick: 777777777"+id );
                    if (id == R.id.rb_main3_1) {
                        writeData(APIData.setUnit(channel, (byte) 1));
                    } else {
                        writeData(APIData.setUnit(channel, (byte) 0));
                    }
                    break;
                case R.id.tv_main3_clear:
                    createPDF();
                    tv_main3_result.setText(" ");
                    break;
            }
        } else {
            showConnect();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

    }

    private void createPDF() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String DATABASE_PATH = android.os.Environment
                            .getExternalStorageDirectory().getAbsolutePath() + "/NFCTemRecords";
                    String file_path = DATABASE_PATH + "/" + "香港" + ".pdf";
                    PdfDocument document = new PdfDocument();//当然，我们也可以使用PrintedPdfDocument,后者继承自前者
                    String  i="香港一天游计划书\n" +
                            "\n" +
                            "准备：通行证，身份证，中国银行卡，港币，衣服，纸巾，水， 漫游包。（八达通）\n" +
                            "\n" +
                            "（1）铜锣湾（购物）\n" +
                            "国际知名品牌：利园、希慎广场、利舞台、名店坊；\n" +
                            "中高档商品： 时代广场 、崇光百货 、wtc more世贸中心；\n" +
                            "年轻潮流服饰：金百利商场 ；\n" +
                            "廉价衣饰和生活杂货的销售热点：渣甸坊。\n" +
                            "旅游景点：铜锣湾天后庙\n" +
                            "...\n" +
                            "（2）湾仔（休闲）\n" +
                            "1.香港会议展览中心\n" +
                            "2.金紫荆广场\n" +
                            "3.旧湾仔邮政局\n" +
                            "4.洪圣庙\n" +
                            "5.湾仔电脑城\n" +
                            "6.大佛口人行天桥\n" +
                            "7.姻缘石\n" +
                            "8.太平山顶，湾仔公园\n" +
                            "...\n" +
                            "（3）尖沙咀（购物）\n" +
                            "1.海港城\n" +
                            "2.香港文化中心，香港太空馆。\n" +
                            "3.新世界中心\n" +
                            "....\n" +
                            "\n" +
                            "路线：\n" +
                            "一，深圳湾口岸——湾仔（下车）——九龙冰室——湾仔公园——时代广场——希慎广场——崇光百货。\n" +
                            "二，深圳湾口岸——湾仔（下车）——香港会议展览中心——金紫荆广场——时代广场——希慎广场——崇光百货。\n" +
                            "三，铜锣湾（下车）——崇光百货——希慎广场——世贸中心——湾仔--\n" +
                            "四，尖沙咀（下车）-购物——渡轮到湾仔——一路逛下\n" +
                            "五，尖沙咀（下车）-购物——渡轮到铜锣湾——一路逛下\n" +
                            "六，湾仔或者铜锣湾（个人选择铜锣湾）";
                    com.itextpdf.text.Document document1 = new com.itextpdf.text.Document();
                    PdfWriter.getInstance(document1, new FileOutputStream(file_path));
                    document1.open();
                    document1.add(new Paragraph(i, setChineseFont()));
                    //创建一个有三行的表格
                    document1.close();

                }catch(FileNotFoundException e){
                    e.printStackTrace();
                }catch(IOException e){
                    e.printStackTrace();
                }catch(DocumentException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static Font setChineseFont() {
        try {
            Font localFont = new Font(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false), 12.0F, 0);
            return localFont;
        } catch (Exception localException){
            localException.printStackTrace();
        }
        return null;
    }

}

