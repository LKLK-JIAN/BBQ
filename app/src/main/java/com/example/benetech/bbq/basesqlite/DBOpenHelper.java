package com.example.benetech.bbq.basesqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * SQLite版本控制类，用于创建数据表及版本升级
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    // 得到SD卡路径
    private static final String DATABASE_PATH = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/BBQThermometer";
    private static final String DATABASE_NAME = "BBQThermometer.db";
    private static final int VERSION = 1;
    private static final String Channel="channel";
    private static final String file_title="file_title";
    private static final String file_time="file_time";
    private static final String file_remarks="file_remarks";
    private static final String temp_unit="temp_unit";

    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    // 用来创建数据库表及初始化操作
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE document( _id INTEGER PRIMARY KEY autoincrement,"+Channel+" INTEGER,"+file_title+" TEXT, "+file_time +
                " TEXT,"+file_remarks+" TEXT)");
        db.execSQL("CREATE TABLE channeltable(_id INTEGER PRIMARY KEY autoincrement,tem float,did INTEGER)");
//        db.execSQL("CREATE TABLE channeltwo(_id INTEGER PRIMARY KEY autoincrement,tem float,did INTEGER)");
//        db.execSQL("CREATE TABLE channelthree(_id INTEGER PRIMARY KEY autoincrement,tem float,did INTEGER)");
//        db.execSQL("CREATE TABLE channelfour(_id INTEGER PRIMARY KEY autoincrement,tem float,did INTEGER)");
        Log.e("sqlite", "创建表ok!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 软件版本升级时来修改表结构
    }
}
