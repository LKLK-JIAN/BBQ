package com.example.benetech.bbq.basesqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.benetech.bbq.bean.Document;
import com.example.benetech.bbq.bean.TemValue;

import java.util.List;

public class TempMeterService extends DBOpenHelper implements DocumentDao,ChannelDao {

    private SQLiteDatabase readableDatabase = null;

    public TempMeterService(Context context) {
        super(context);
        readableDatabase = getReadableDatabase();
    }

    /**
     * 插入文件
     */
    @Override
    public void insertDocument(Document ci) {
        readableDatabase
                .execSQL(
                        "INSERT INTO document (channel,file_title,file_time,file_remarks) values (?,?,?,?) ",
                        new Object[]{ci.getChannel(),ci.getFile_title(), ci.getFile_time(), ci.getRemarks() });
    }


    /**
     * 删除Document表指定文件
     */
    @Override
    public void deleteDocument(Integer id) {
        readableDatabase.execSQL("DELETE FROM document WHERE _id=?",
                new Object[]{id});
    }

    /**
     * 修改文件信息
     */
    @Override
    public void modifiyDocument(Document ci) {
        readableDatabase
                .execSQL("UPDATE document SET remarks=? where _id=?",
                        new Object[]{ci.getRemarks(), ci.getId()});
    }

    /**
     * 获取单通道所有文件信息
     */
    @Override
    public Cursor getAllDocument(Integer channel) {
        return readableDatabase.rawQuery("SELECT * FROM document ORDER BY _id desc",null);
        //return readableDatabase.rawQuery("SELECT * FROM document where channel=? ORDER BY _id desc", new String[]{channel.toString()});
    }

    /**
     * 获取指定文件信息
     */
    @Override
    public Document getDocument(Integer id) {
        Cursor cursor = readableDatabase.rawQuery(
                "SELECT * FROM document WHERE _id=?",
                new String[]{id.toString()});
        if (cursor.moveToFirst()) {
            int eid = cursor.getInt(cursor.getColumnIndex("_id"));
            String file_title = cursor.getString(cursor.getColumnIndex("file_title"));
            String file_time = cursor.getString(cursor.getColumnIndex("file_time"));
           // String remarks = cursor.getString(cursor.getColumnIndex("remarks"));
            Document document = new Document( eid, file_title, file_time, "" );
            return document;
        }
        return null;
    }

    /**
     * 获取指定文件信息
     */
    @Override
    public Document getTopDocument() {
        Cursor cursor = readableDatabase.rawQuery(
                "select * from document t1 where not exists (select 1 from document where _id > t1._id)", null);
        if (cursor.moveToFirst()) {
            int eid = cursor.getInt(cursor.getColumnIndex("_id"));

            String file_title = cursor.getString(cursor.getColumnIndex("file_title"));
            String file_time = cursor.getString(cursor.getColumnIndex("file_time"));
            String remarks = cursor.getString(cursor.getColumnIndex("file_remarks"));
            Document document = new Document(eid, file_title, file_time, remarks);
            return document;
        }
        return null;
    }

    /*插入MeterData数据*/
    @Override
    public void insertMeterdata(TemValue temvalue, String channeltable) {
        readableDatabase
                .execSQL(
                        "INSERT INTO"+channeltable+"(tem, did) values (?,?) ",
                        new Object[]{temvalue.getTemp(), temvalue.getDid()});
    }

    /*删除指定数据*/
    @Override
    public void deleteMeterdata(Integer id, String channeltable) {
        readableDatabase.execSQL("DELETE FROM channeltable where did = ? ",
                new Object[]{id.toString()});
    }

    /*获取指定did的Meterdata*/
    @Override
    public Cursor getAllMeterdata(Integer id, String channeltable) {
        //return readableDatabase.rawQuery("SELECT * FROM "+channeltable+" where did=?", new String[]{id.toString()});
        return readableDatabase.rawQuery("SELECT * FROM channeltable where did=?", new String[]{id.toString()});
    }

    @Override
    public float getMaxFs(Integer id) {
        Cursor cursor = readableDatabase.rawQuery(
                "select max(lux) from meterdata where did=?",
                new String[]{id.toString()});
        if (cursor.moveToFirst()) {
            return cursor.getFloat(cursor.getColumnIndex("max(lux)"));
        }
        return 0;
    }

    @Override
    public float getMinFs(Integer id) {
        Cursor cursor = readableDatabase.rawQuery(
                "select min(lux) from meterdata where did=?",
                new String[]{id.toString()});
        if (cursor.moveToFirst()) {
            return cursor.getFloat(cursor.getColumnIndex("min(lux)"));
        }
        return 0;
    }

    @Override
    public float getAvgFs(Integer id) {
        Cursor cursor = readableDatabase.rawQuery(
                "select avg(lux) from meterdata where did=?",
                new String[]{id.toString()});
        if (cursor.moveToFirst()) {
            return cursor.getFloat(cursor.getColumnIndex("avg(lux)"));
        }
        return 0;
    }

    public boolean allInsert(List<TemValue> relDates, Document document,String channel) {
        if(null==readableDatabase||null==relDates||relDates.size()<0){
            return false;
        }
        try{
            String sql="INSERT  INTO  channeltable (tem,did) values (?,?)" ;
            SQLiteStatement state=readableDatabase.compileStatement(sql);
            readableDatabase.beginTransaction();
            for(TemValue outputData:relDates){
                state.bindDouble(1,outputData.getTemp());
                state.bindLong(2,document.getId());
                long result=state.executeInsert();
                if(result<0){
                    return false;
                }
            }
            readableDatabase.setTransactionSuccessful();
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        finally{
            try {
                if (null != readableDatabase) {
                    readableDatabase.endTransaction();
                    readableDatabase.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    /**
     * 关闭数据库
     */
    public void closeDatabase() {
        if (readableDatabase != null) {
            readableDatabase.close();
        }
    }

}
