package com.example.benetech.bbq;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import com.example.benetech.bbq.adapter.FileListAdapter;
import com.example.benetech.bbq.bean.Filedocument;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener {
    private Intent intent;
    private String table;


    /** Called when the activity is first created. */
    private ArrayList<String> items = null;//存放名称
    private List<String> paths = null;//存放路径

    private String DATABASE_PATH = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/BBQ";
    public  ListView lv_file;
    private ImageView iv_file_back;
    private Context context;
    private FileListAdapter adapter;
    private TextView tv_file_editor;
    private ArrayList<Filedocument> filedocuments;
    private boolean isEditState=false;
    private Button btn_filedelete;
    private TextView tv_select_filenum,select_fileall;
    private LinearLayout file_bottom_dialog;
    private boolean isSelectAll=false;
    private int index = 0;
    private boolean isEditorcancel=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 无标题栏，无状态栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getSupportActionBar().hide();
        //设置界面切换动画
        setContentView(R.layout.activity_file);
        intent=getIntent();
        table=intent.getStringExtra("table");
        filedocuments=new ArrayList<>();
        context=this;
        lv_file=findViewById(R.id.lv_file);
        tv_file_editor=findViewById(R.id.tv_file_editor);
        tv_file_editor.setOnClickListener(this);
        iv_file_back=findViewById(R.id.iv_file_back);
        iv_file_back.setOnClickListener(this);
        btn_filedelete=findViewById(R.id.btn_filedelete);
        btn_filedelete.setOnClickListener(this);
        tv_select_filenum=findViewById(R.id.tv_select_filenum);
        select_fileall=findViewById(R.id.select_fileall);
        select_fileall.setOnClickListener(this);
        file_bottom_dialog=findViewById(R.id.file_bottom_dialog);
        lv_file.setOnItemLongClickListener(this);
        this.getFileDir(DATABASE_PATH);//获取rootPath目录下的文件.
    }

    public void getFileDir(String filePath) {
        try{
            items = new ArrayList<String>();
            paths = new ArrayList<String>();
            File f = new File(filePath);
            File[] files = f.listFiles();// 列出所有文件
            // 如果不是根目录,则列出返回根目录和上一目录选项
            if (!filePath.equals(DATABASE_PATH)) {
                items.add("返回根目录");
                paths.add(DATABASE_PATH);
                items.add("返回上一层目录");
                paths.add(f.getParent());
            }
            // 将所有文件存入list中
            if(files != null){
                int count = files.length;// 文件个数
                for (int i = 0; i < count; i++) {
                    File file = files[i];
                    filedocuments.add(new Filedocument(file.getName(),file.getPath()));
                    items.add(file.getName());
                    paths.add(file.getPath());
                }
            }
            adapter=new FileListAdapter(items,context);
            adapter=new FileListAdapter(context,filedocuments);
            lv_file.setAdapter(adapter);
            lv_file.setOnItemClickListener(this);
            // lv_file.setListAdapter(adapter);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_file_back:
                finish();
                break;
            case R.id.tv_file_editor:
//                File file=new File(paths.get(0));
//                if (file.exists()) {
//                    file.delete();
//                }
//                items.remove(0);
//                paths.remove(0);
//                adapter.notifyDataSetChanged();
                // lv_file.notifyAll();
                updataEditMode();
                break;
            case R.id.btn_filedelete:
                deleteVideo();
                break;
            case R.id.select_fileall:
                selectAllMain();
                break;
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

        File file=new File(paths.get(position));
        if (file.exists()) {
            file.delete();
        }
        adapter.notifyDataSetChanged();
        adapter.notify();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if(isEditState){
            Filedocument  filedocument= filedocuments.get(position);
            boolean isSelect = filedocument.getSelected();
            if (!isSelect) {
                index++;
                filedocument.setSelected(true);
                if (index == filedocuments.size()) {
                    isSelectAll = true;
                    select_fileall.setText(getResources().getString(R.string.his_cancelallselect));
                }
            } else {
                filedocument.setSelected(false);
                index--;
                isSelectAll = false;
                select_fileall.setText(getResources().getString(R.string.his_allselect));
            }
            setBtnBackground(index);
            tv_select_filenum.setText(String.valueOf(index));
            adapter.notifyDataSetChanged();
        }else{
            String path = paths.get(position);
            File file = new File(path);
            Intent intent=getPdfFileIntent(file);
            startActivity(Intent.createChooser(intent,getResources().getString(R.string.select_app)));
        }
    }

    //android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/pdf");
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    //android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(File file) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(file);
        intent.setDataAndType(uri, "application/vnd.ms-excel");
        return intent;
    }

    private void updataEditMode(){
        if(isEditorcancel){
            tv_file_editor.setText(getResources().getString(R.string.his_cancle));
            file_bottom_dialog.setVisibility(View.VISIBLE);
            isEditState = true;
            adapter.setmEditMode(isEditState);
            isEditorcancel=false;
        }else{
            tv_file_editor.setText(getResources().getString(R.string.edit));
            file_bottom_dialog.setVisibility(View.GONE);
            isEditState = false;
            adapter.setmEditMode(isEditState);
            isEditorcancel=true;
            clearAll();
        }
    }

    private void clearAll() {
        tv_select_filenum.setText(String.valueOf(0));
        isSelectAll = false;
        select_fileall.setText(getResources().getString(R.string.invertselect));
        setBtnBackground(0);
    }

    private void setBtnBackground(int size) {
        if (size != 0) {
            btn_filedelete.setBackgroundResource(R.drawable.button_shape);
            btn_filedelete.setEnabled(true);
            btn_filedelete.setTextColor(Color.WHITE);
        } else {
            btn_filedelete.setBackgroundResource(R.drawable.button_noclickable_shape);
            btn_filedelete.setEnabled(false);
            btn_filedelete.setTextColor(ContextCompat.getColor(this, R.color.color_b7b8bd));
        }
    }

    /**
     * 全选和反选
     */
    private void selectAllMain() {
        if (adapter == null) return;
        if (!isSelectAll) {
            for (int i = 0, j = adapter.getFiledocuments().size(); i < j; i++) {
                adapter.getFiledocuments().get(i).setSelected(true);
            }
            index = adapter.getFiledocuments().size();
            btn_filedelete.setEnabled(true);
            select_fileall.setText(getResources().getString(R.string.his_cancelallselect));
            isSelectAll = true;
        } else {
            for (int i = 0, j = adapter.getFiledocuments().size(); i < j; i++) {
                adapter.getFiledocuments().get(i).setSelected(false);
            }
            index = 0;
            btn_filedelete.setEnabled(false);
            select_fileall.setText(getResources().getString(R.string.his_allselect));
            isSelectAll = false;
        }
        adapter.notifyDataSetChanged();
        setBtnBackground(index);
        tv_select_filenum.setText(String.valueOf(index));
    }

    private void deleteVideo() {
        if (index == 0) {
            btn_filedelete.setEnabled(false);
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
            msg.setText(getResources().getString(R.string.his_deleteitem));
        } else {
            msg.setText(getResources().getString(R.string.his_deleteitem));
        }
        cancle.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                builder.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                for (int i =  adapter.getFiledocuments().size(), j = 0; i > j; i--) {
                    Filedocument myLive = adapter.getFiledocuments().get(i - 1);
                    if (myLive.getSelected()) {
                        adapter.getFiledocuments().remove(myLive);
                        File file=new File(myLive.getFilePath());
                        if (file.exists()) {
                            file.delete();
                        }
                        index--;
                    }
                }
                index = 0;
                tv_select_filenum.setText(String.valueOf(0));
                setBtnBackground(index);
                if ( adapter.getFiledocuments().size() == 0){
                    file_bottom_dialog .setVisibility(View.GONE);
                }
                adapter.notifyDataSetChanged();
                builder.dismiss();
            }
        });
    }

}
