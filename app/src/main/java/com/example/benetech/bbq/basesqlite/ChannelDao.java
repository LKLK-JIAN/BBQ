package com.example.benetech.bbq.basesqlite;

import android.database.Cursor;

import com.example.benetech.bbq.bean.TemValue;

public interface ChannelDao {
    void insertMeterdata(TemValue temvalu,String channneltable);
    void deleteMeterdata(Integer id,String channeltable);
    Cursor getAllMeterdata(Integer id,String channeltable);
    float getMaxFs(Integer id);
    float getMinFs(Integer id);
    float getAvgFs(Integer id);
}
