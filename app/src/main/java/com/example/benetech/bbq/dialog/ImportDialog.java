package com.example.benetech.bbq.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.example.benetech.bbq.R;
import com.example.benetech.bbq.bean.Document;
import com.example.benetech.bbq.bean.TemValue;
import com.example.benetech.bbq.wight.ToastUtils;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;


public class ImportDialog extends Dialog implements View.OnClickListener {
    private Document d;
    private ArrayList<TemValue> list;
    private Context context;
    private Button btn_import_cancel, btn_import_confirm;
    private RadioGroup rg_import_radiogroup;
    private DecimalFormat decimalFormat;
    private Handler handler;
    private int unitStyle;
    private CustomProgressDialog customProgressDialog;


    public ImportDialog(@NonNull final Context context, final Document d, ArrayList<TemValue> list, int unitStyle) {
        super(context, R.style.DialogTheme);
        this.context = context;
        this.d = d;
        this.unitStyle = unitStyle;
        setContentView(R.layout.import_dialog);
        btn_import_cancel = findViewById(R.id.btn_import_cancel);
        btn_import_confirm = findViewById(R.id.btn_import_confirm);
        rg_import_radiogroup = findViewById(R.id.rg_import_radiogroup);
        this.list = list;
        decimalFormat = new DecimalFormat("0.0");
        btn_import_cancel.setOnClickListener(this);
        btn_import_confirm.setOnClickListener(this);
        customProgressDialog = CustomProgressDialog.createDialog(context);
        customProgressDialog.setMessage(context.getResources().getString(R.string.data_importing));
        customProgressDialog.setCanceledOnTouchOutside(false);// 设置点击屏幕progressDialog不消失

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case 0x123:
                        customProgressDialog.dismiss();
                        String DATABASE_PATH = Environment
                                .getExternalStorageDirectory().getAbsolutePath() + "/BBQ";
                        String file_path = DATABASE_PATH + "/" + d.getFile_title() + ".pdf";
                        getPdfFileIntent(file_path);
                        ToastUtils.showToast(context, d.getFile_title() + ".pdf" + context.getString(R.string.haven_save));
                        break;
                    case 0x456:
                        customProgressDialog.setMessage(message.arg1 + " %");
                        break;
                }
                return true;
            }
        });
    }

    //导出表格
    private void createExcel() {
        customProgressDialog.show();
        try {
            String DATABASE_PATH = android.os.Environment
                    .getExternalStorageDirectory().getAbsolutePath() + "/BBQ";
            File files = new File(DATABASE_PATH);
            if (!files.exists()) {
                files.mkdirs();
            }
            String file_path = DATABASE_PATH + "/" + d.getFile_title() + ".xls";
            File file = new File(file_path);
            WritableWorkbook book = Workbook.createWorkbook(file);
            WritableSheet sheet1 = book.createSheet("Data", 0);
            sheet1.addCell(new Label(0, 0, context.getResources().getString(R.string.title)));
            sheet1.addCell(new Label(1, 0, d.getFile_title()));
            sheet1.addCell(new Label(0, 1, context.getResources().getString(R.string.time)));
            sheet1.addCell(new Label(1, 1, d.getFile_time()));
            sheet1.addCell(new Label(0, 2, context.getResources().getString(R.string.serial)));
            sheet1.addCell(new Label(1, 2, context.getResources().getString(R.string.temp)));
//          sheet1.addCell(new Label(0, 2, context.getResources().getString(R.string.max)));
//          sheet1.addCell(new Label(1, 2, decimalFormat.format(d.getmMaxValue() * 0.1) + "dB"));
//          sheet1.addCell(new Label(2, 2, context.getResources().getString(R.string.min)));
//          sheet1.addCell(new Label(3, 2, decimalFormat.format(d.getmMinValue() * 0.1) + "dB"));
//          sheet1.addCell(new Label(4, 2, context.getResources().getString(R.string.avg)));
//          sheet1.addCell(new Label(5, 2, decimalFormat.format(d.getmAvgValue() * 0.1) + "dB"));
//          sheet1.addCell(new Label(0, 4, context.getResources().getString(R.string.import_dialog_serialnumber)));
//          sheet1.addCell(new Label(1, 4, context.getResources().getString(R.string.DB)));
//          sheet1.addCell(new Label(2, 4, context.getResources().getString(R.string.minormax)));
//          sheet1.addCell(new Label(3, 4, context.getResources().getString(R.string.fastorslow)));
//          sheet1.addCell(new Label(4, 4, context.getResources().getString(R.string.ac)));
//          sheet1.addCell(new Label(3, 4, context.getResources().getString(R.string.hold)));
            if (unitStyle == 0) {
                for (int i = 0; i < list.size(); i++) {
                    sheet1.addCell(new Label(0, i + 3, i + 1 + ""));
                    sheet1.addCell(new Label(1, i + 3, String.format("%.1f",list.get(i).getTemp()) + "℃"));
                }
            } else {
                for (int i = 0; i < list.size(); i++) {
                    sheet1.addCell(new Label(0, i + 3, i + 1 + ""));
                    sheet1.addCell(new Label(1, i + 3, String.format("%.1f",list.get(i).getTemp()*1.8+32) + "℉"));
                }
            }
            book.write();
            book.close();
            getExcelFileIntent(files);
            customProgressDialog.dismiss();
            ToastUtils.showToast(context, d.getFile_title() + ".xls" + context.getString(R.string.haven_save));
        } catch (WriteException e) {
            ToastUtils.showToast(context, "123");
            e.printStackTrace();
        } catch (IOException e) {
            ToastUtils.showToast(context, "456");
            e.printStackTrace();
        }

    }

    // 导出PDF
    private void createPDF() {
        customProgressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String DATABASE_PATH = Environment
                            .getExternalStorageDirectory().getAbsolutePath() + "/BBQ";
                    String file_path = DATABASE_PATH + "/" + d.getFile_title() + ".pdf";
                    com.itextpdf.text.Document document1 = new com.itextpdf.text.Document();
                    PdfWriter.getInstance(document1, new FileOutputStream(file_path));
                    document1.open();
                    String title = context.getResources().getString(R.string.title) +":" +d.getFile_title();
                    String timeDate = context.getResources().getString(R.string.time) +":"+ d.getFile_time();
//                    String mostValue = String.format(context.getResources().getString(R.string.max) + decimalFormat.format(d.getmMaxValue() * 0.1) + "dB" +
//                            "   " + context.getResources().getString(R.string.min) + decimalFormat.format(d.getmMinValue() * 0.1) + "dB" + "   "
//                            + context.getResources().getString(R.string.avg) + decimalFormat.format(d.getmAvgValue() * 0.1)) + "dB";
                    document1.add(new Paragraph(title, setChineseFont()));
                    document1.add(new Paragraph(timeDate, setChineseFont()));
                    //document1.add(new Paragraph(mostValue, setChineseFont()));
                    document1.add(new Paragraph("   ", setChineseFont()));


                    //创建一个有三行的表格
                    PdfPTable table = new PdfPTable(2);
                    table.setTotalWidth(350);//设置表格的总宽度
                    table.setTotalWidth(new float[]{50, 50});//设置表格的各列宽度
                    table.setLockedWidth(true);
                    String serialnumber = context.getResources().getString(R.string.serial);
                    String temp = context.getResources().getString(R.string.temp);
//                    String minormax = context.getResources().getString(R.string.minormax);
//                    String fastormax = context.getResources().getString(R.string.fastorslow);
//                    String ac = context.getResources().getString(R.string.ac);
//                    String hold = context.getResources().getString(R.string.hold);
                    table.addCell(new Paragraph(serialnumber, setChineseFont()));
                    table.addCell(new Paragraph(temp, setChineseFont()));
                    //table.addCell(new Paragraph(minormax, setChineseFont()));
                    //table.addCell(new Paragraph(fastormax, setChineseFont()));
                    //table.addCell(new Paragraph(ac, setChineseFont()));
                    //table.addCell(new Paragraph(hold, setChineseFont()));
                    if(unitStyle==0){
                        for (int i = 0; i < list.size(); i++) {
                            int persent = i * 100 / list.size();
                            Message message = new Message();
                            message.what = 0x456;
                            message.arg1 = persent;
                            handler.sendMessage(message);
//                        if (d.getSavetype() == 1) {
                            float db1 = list.get(i).getTemp();
//                            int maxormin1 = list.get(i).getMaxormin();
//                            int hold1 = list.get(i).getHold();
//                            int fastorslow1 = list.get(i).getFastorslow();
//                            int ac1 = list.get(i).getAc();
                            table.addCell(
                                    new Paragraph((i + 1) + "", setChineseFont()));
                            table.addCell(
                                    new Paragraph(decimalFormat.format(db1) + "℃", setChineseFont()));
//                            if (maxormin1 == 0x30) {
//                                table.addCell(
//                                        new Paragraph("REAL", setChineseFont()));
//                            } else if (maxormin1 == 0x31) {
//                                table.addCell(
//                                        new Paragraph("MIN", setChineseFont()));
//                            } else {
//                                table.addCell(
//                                        new Paragraph("MAX", setChineseFont()));
//                            }

//                            if (fastorslow1 == 0x30) {
//                                table.addCell(
//                                        new Paragraph("SLOW", setChineseFont()));
//                            } else {
//                                table.addCell(
//                                        new Paragraph("FAST", setChineseFont()));
//                            }
//                            if (ac1 == 0x30) {
//                                table.addCell(
//                                        new Paragraph("C", setChineseFont()));
//                            } else {
//                                table.addCell(
//                                        new Paragraph ("A", setChineseFont()));
//                            }
//                            if (hold1 == 0x30) {
//                                table.addCell(
//                                        new Paragraph("", setChineseFont()));
//                            } else {
//                                table.addCell(
//                                        new Paragraph("HOLD", setChineseFont()));
//                            }
//                        } else {
//                            int db0 = list.get(i).getValue();
//                            int maxormin0 = list.get(i).getMaxormin();
//                            int hold0 = list.get(i).getHold();
//                            int fastorslow0 = list.get(i).getFastorslow();
//                            int ac0 = list.get(i).getAc();
//                            table.addCell(
//                                    new Paragraph((i + 1) + "", setChineseFont()));
//                            table.addCell(
//                                    new Paragraph(decimalFormat.format(db0 * 0.1) + "dB", setChineseFont()));
//                            if (maxormin0 == 0) {
//                                table.addCell(
//                                        new Paragraph("REAL", setChineseFont()));
//                            } else if (maxormin0 == 1) {
//                                table.addCell(
//                                        new Paragraph("MIN", setChineseFont()));
//                            } else {
//                                table.addCell(
//                                        new Paragraph("MAX", setChineseFont()));
//                            }

//                            if (fastorslow0 == 0) {
//                                table.addCell(
//                                        new Paragraph("SLOW", setChineseFont()));
//                            } else {
//                                table.addCell(
//                                        new Paragraph("FAST", setChineseFont()));
//                            }
//                            if (ac0 == 0) {
//                                table.addCell(
//                                        new Paragraph("C", setChineseFont()));
//                            } else {
//                                table.addCell(new Paragraph(  "A", setChineseFont()));
//                            }
//                            if (hold0 == 0) {
//                                table.addCell(
//                                        new Paragraph("", setChineseFont()));
//                            } else {
//                                table.addCell(
//                                        new Paragraph("HOLD", setChineseFont()));
//                            }
//                        }
                        }
                    }else{
                        for (int i = 0; i < list.size(); i++) {
                            int persent = i * 100 / list.size();
                            Message message = new Message();
                            message.what = 0x456;
                            message.arg1 = persent;
                            float db1 = list.get(i).getTemp();
                            table.addCell(
                                    new Paragraph((i + 1) + "", setChineseFont()));
                            table.addCell(
                                    new Paragraph(decimalFormat.format(db1*1.8+32) + "℃", setChineseFont()));
                        }
                    }
                    document1.add(table);
                    document1.close();
                    handler.sendEmptyMessage(0x123);

                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } catch (DocumentException e1) {
                    e1.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_import_cancel:
                dismiss();
                break;
            case R.id.btn_import_confirm:
                switch (rg_import_radiogroup.getCheckedRadioButtonId()) {
                    case R.id.rb_import_excel:
                        createExcel();
                        dismiss();
                        break;
                    case R.id.rb_import_pdf:
                        createPDF();
                        dismiss();
                        break;
                }
                break;
        }
    }

    public static Font setChineseFont() {
        try {
            Font localFont = new Font(BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", false), 12.0F, 0);
            return localFont;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return null;
    }

//    protected void creatTable(int i, ArrayList<TemValue> arrayList, WritableSheet sheet) {
//        if (d.getSavetype() == 1) {
//            int db = arrayList.get(i).getValue();
//            int maxormin = arrayList.get(i).getMaxormin();
//            int hold = arrayList.get(i).getHold();
//            int fastorslow = arrayList.get(i).getFastorslow();
//            int ac = arrayList.get(i).getAc();
//            try {
//                sheet.addCell(new Label(0, i + 5, i + 1 + ""));
//                sheet.addCell(new Label(1, i + 5, decimalFormat.format(db * 0.1) + "dB"));
//                if (maxormin == 0x30) {
//                    sheet.addCell(new Label(2, i + 5, "REAL"));
//                } else if (maxormin == 0x31) {
//                    sheet.addCell(new Label(2, i + 5, "MIN"));
//                } else {
//                    sheet.addCell(new Label(2, i + 5, "MAX"));
//                }
//                if (hold == 0x30) {
//
//                } else {
//                    sheet.addCell(new Label(5, i + 5, "HOLD"));
//                }
////                if (fastorslow == 0x30) {
////                    sheet.addCell(new Label(3, i + 5, "SLOW"));
////                } else {
////                    sheet.addCell(new Label(3, i + 5, "FAST"));
////                }
////                if (ac == 0x30) {
////                    sheet.addCell(new Label(4, i + 5, "C"));
////                } else {
////                    sheet.addCell(new Label(4, i + 5, "A"));
////                }
//            } catch (WriteException e) {
//                e.printStackTrace();
//            }
//        } else {
//            int db = arrayList.get(i).getValue();
//            int maxormin = arrayList.get(i).getMaxormin();
//            int hold = arrayList.get(i).getHold();
//            int fastorslow = arrayList.get(i).getFastorslow();
//            int ac = arrayList.get(i).getAc();
//
//            try {
//                sheet.addCell(new Label(0, i + 5, i + 1 + ""));
//                sheet.addCell(new Label(1, i + 5, decimalFormat.format(db * 0.1) + "dB"));
//
//                if (maxormin == 0) {
//                    sheet.addCell(new Label(2, i + 5, "REAL"));
//                } else if (maxormin == 1) {
//                    sheet.addCell(new Label(2, i + 5, "MIN"));
//                } else {
//                    sheet.addCell(new Label(2, i + 5, "MAX"));
//                }
//                if (hold == 0) {
//                } else {
//                    sheet.addCell(new Label(5, i + 5, "HOLD"));
//                }
////                if (fastorslow == 0) {
////                    sheet.addCell(new Label(3, i + 5, "SLOW"));
////                } else {
////                    sheet.addCell(new Label(3, i + 5, "FAST"));
////                }
////                if (ac == 0) {
////                    sheet.addCell(new Label(4, i + 5, "C"));
////                } else {
////                    sheet.addCell(new Label(4, i + 5, "A"));
////                }
//            } catch (WriteException e) {
//                e.printStackTrace();
//            }
//        }
//
//
//    }

//    protected void creatPDFTable(int i, ArrayList<DBValue> arrayList, PdfPTable table) {
//        if (d.getSavetype() == 1) {
//            int db = arrayList.get(i).getValue();
//            int maxormin = arrayList.get(i).getMaxormin();
//            int hold = arrayList.get(i).getHold();
//            int fastorslow = arrayList.get(i).getFastorslow();
//            int ac = arrayList.get(i).getAc();
//            table.addCell(
//                    new Paragraph(i + "", setChineseFont()));
//            table.addCell(
//                    new Paragraph(decimalFormat.format(db * 0.1), setChineseFont()));
//
//            if (maxormin == 0x30) {
//                table.addCell(
//                        new Paragraph("REAL", setChineseFont()));
//
//            } else if (maxormin == 0x31) {
//                table.addCell(
//                        new Paragraph("MIN", setChineseFont()));
//
//            } else {
//                table.addCell(
//                        new Paragraph("MAX", setChineseFont()));
//
//            }
//            if (hold == 0x30) {
//                table.addCell(
//                        new Paragraph("", setChineseFont()));
//            } else {
//                table.addCell(
//                        new Paragraph("HOLD", setChineseFont()));
//
//            }
////            if (fastorslow == 0x30) {
////                table.addCell(
////                        new Paragraph("SLOW", setChineseFont()));
////            } else {
////                table.addCell(
////                        new Paragraph("FAST", setChineseFont()));
////            }
////            if (ac == 0x30) {
////                table.addCell(
////                        new Paragraph("C", setChineseFont()));
////            } else {
////                table.addCell(
////                        new Paragraph(i + "A", setChineseFont()));
////            }
//
//        } else {
//            int db = arrayList.get(i).getValue();
//            int maxormin = arrayList.get(i).getMaxormin();
//            int hold = arrayList.get(i).getHold();
//            int fastorslow = arrayList.get(i).getFastorslow();
//            int ac = arrayList.get(i).getAc();
//
//            table.addCell(
//                    new Paragraph(i + "", setChineseFont()));
//            table.addCell(
//                    new Paragraph(decimalFormat.format(db * 0.1), setChineseFont()));
//            if (maxormin == 0) {
//                table.addCell(
//                        new Paragraph("REAL", setChineseFont()));
//
//            } else if (maxormin == 1) {
//                table.addCell(
//                        new Paragraph("MIN", setChineseFont()));
//
//            } else {
//                table.addCell(
//                        new Paragraph("MAX", setChineseFont()));
//
//            }
//            if (hold == 0) {
//                table.addCell(
//                        new Paragraph("", setChineseFont()));
//            } else {
//                table.addCell(
//                        new Paragraph("HOLD", setChineseFont()));
//
//            }
////            if (fastorslow == 0) {
////                table.addCell(
////                        new Paragraph("SLOW", setChineseFont()));
////
////            } else {
////                table.addCell(
////                        new Paragraph("FAST", setChineseFont()));
////
////            }
////            if (ac == 0) {
////                table.addCell(
////                        new Paragraph("C", setChineseFont()));
////
////            } else {
////                table.addCell(
////                        new Paragraph(i + "A", setChineseFont()));
////            }
//        }
//    }

    //android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(String filePath) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(filePath));
        intent.setDataAndType(uri, "application/pdf");
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
}


