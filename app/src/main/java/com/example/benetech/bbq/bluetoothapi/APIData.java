package com.example.benetech.bbq.bluetoothapi;


import com.example.benetech.bbq.ChannelActivity;

import java.nio.channels.Channel;

/**
 * Created by android on 2018/4/16.
 */

public class APIData {

    public static byte[] value;
//    public  byte[] read_record_data;
//    public  byte[] start_to_record;
//    public  byte[] clear_data;
//    public  byte[] query_datanumber;
//    public  byte[] query_record_time;
//    public  byte[] query_area;
//    public  byte[] set_unit;
//    public  byte[] set_record_time;
//    public  byte[] set_area;

    //读取实时温度值  1~4通道
    public static byte[] getReal_time(byte i){
        ChannelActivity.mBackDataType="realtime";
        value = new byte[5];
        value[0] = 0x57;
        value[1] = 0x48;
        value[2] = 0x01;
        value[3] = i;
        value[4] = 0x00;
        return value;
    }

    //获取时间数据  1~4通道
    public static byte[] getTime_data(byte i){
        ChannelActivity.mBackDataType="timedata";
        value = new byte[5];
        value[0] = 0x57;
        value[1] = 0x48;
        value[2] = 0x02;
        value[3] = i;
        value[4] = 0x00;
        return value;
    }

    //读取单位信息
    public static byte[] getUnit(byte i){
        ChannelActivity.mBackDataType="getunit";
        value = new byte[5];
        value[0] = 0x57;
        value[1] = 0x48;
        value[2] = 0x03;
        value[3] = i;
        value[4] = 0x00;
        return value;
    }

    //读取电量信息
    public static byte[] getBattry(){
        ChannelActivity.mBackDataType="battery";
        value = new byte[4];
        value[0] = 0x57;
        value[1] = 0x48;
        value[2] = 0x04;
        value[3] = 0x00;
        return value;
    }

    //设置温度限制
    public static byte[] setTemp_limit(short temp, byte i){
        ChannelActivity.mBackDataType="templimit";
        value = new byte[7];
        value[0] = 0x57;
        value[1] = 0x48;
        value[2] = 0x11;
        value[3] = i;
        value[4] = 0x02;
        value[5] = (byte) (temp & 0xff);
        value[6] = (byte) (temp >> 8 & 0xff);
        return value;
    }

    //设置定时数据
    public static byte[] setTiming_data(int time, byte i){
        ChannelActivity.mBackDataType="timingdata";
        value = new byte[9];
        value[0] = 0x57;
        value[1] = 0x48;
        value[2] = 0x12;
        value[3] = i;
        value[4] = 0x04;
        value[5] = (byte) (time & 0xff);
        value[6] = (byte) (time >> 8 & 0xff);
        value[7] = (byte) (time >> 16 & 0xff);
        value[8] = (byte) (time >> 24 & 0xff);
        return value;
    }

    //设置单位信息   0/1
    public static byte[] setUnit(byte i, byte unit){
        ChannelActivity.mBackDataType="setunit";
        value = new byte[6];
        value[0] = 0x57;
        value[1] = 0x48;
        value[2] = 0x13;
        value[3] = i;
        value[4] = 0x01;
        value[5] = unit;
        return value;
    }
    //获取四个通道的温度值
    public static byte[] getAllChannelTemp(){
        ChannelActivity.mBackDataType="allchanneltemp";
        value=new byte[5];
        value[0]=0x57;
        value[1]=0x48;
        value[2]=0x01;
        value[3]=0x05;
        value[4]=0x00;
        return value;
    }
    //获取四个通道的时间数据
    public static byte[] getAllChannelTime(){
        ChannelActivity.mBackDataType="allchanneltime";
        value=new byte[5];
        value[0]=0x57;
        value[1]=0x48;
        value[2]=0x02;
        value[3]=0x05;
        value[4]=0x00;
        return value;
    }

    public static byte[] getChannelquantity(){
        ChannelActivity.mBackDataType="allchanneltime";
        value=new byte[5];
        value[0]=0x57;
        value[1]=0x48;
        value[2]=0x06;
        value[3]=0x00;
        value[4]=0x00;
        return value;
    }
}
