package com.example.benetech.bbq;

import android.content.SharedPreferences;
import android.util.Log;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class Variable {

    public static int temp_unit01=0;
    public static int set_type01=0;
    public static int preset_location01=0;
    public static int preset_location201=0;
    public static int temp_reminder01=0;
    public static int time_reminder01=0;
    public static int temp_manual01=0;
    public static int sound_reminder01=0;

    public static int temp_unit02=0;
    public static int set_type02=0;
    public static int preset_location02=0;
    public static int preset_location202=0;
    public static int temp_reminder02=0;
    public static int time_reminder02=0;
    public static int temp_manual02=0;
    public static int sound_reminder02=0;

    public static int temp_unit03=0;
    public static int set_type03=0;
    public static int preset_location03=0;
    public static int preset_location203=0;
    public static int temp_reminder03=0;
    public static int time_reminder03=0;
    public static int temp_manual03=0;
    public static int sound_reminder03=0;

    public static int temp_unit04=0;
    public static int set_type04=0;
    public static int preset_location04=0;
    public static int preset_location204=0;
    public static int temp_reminder04=0;
    public static int time_reminder04=0;
    public static int temp_manual04=0;
    public static int sound_reminder04=0;

    public static int rawId=0;
    public static String rawName;
    public static Field[] fields;
    public static ArrayList<String> data=new ArrayList<>();

    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor;

    public static final int[] foodf = {145, 145, 145, 160, 170, 180, 180, 140, 160, 160, 160, 160, 160, 165, 210, 190};
    public static final int[] foodc = {63, 63, 63, 71, 77, 82, 82, 82, 60, 71, 71, 71, 71, 71, 74, 99, 88};
    public static final int[][] levelc = {{63, 71, 77}, {63, 71, 77}, {63, 71, 77}, {71}, {77}, {82}, {82}, {82}
            , {60}, {71}, {71}, {71}, {71}, {71}, {74}, {99}, {88}};
    public static int[][] levelf = {{145, 160, 170}, {145, 160, 170}, {145, 160, 170}, {160}, {170}, {180},
            {180}, {180}, {140}, {160}, {160}, {160}, {160}, {160}, {165}, {210}, {190}};

    static{
        for (int i = 0; i < fields.length; i++) {
            try {
                rawId = fields[i].getInt(R.raw.class);
                rawName = fields[i].getName();
                data.add(rawName);
                Log.e("TAG", "onClick:1111111 "+rawName);
            } catch (Exception e) {
            }}}}
//    private void createData() {
//        sp = getSharedPreferences("bbq", MODE_PRIVATE);
//        editor = sp.edit();
//        if (sp.getInt("temp_unit01", 404) == 404) {
//            editor.putInt("temp_unit01", 0);  //0代表℃，1代表℉
//        }
//        if(sp.getInt("temp_unit02",404)==404){
//            editor.putInt("temp_unit02",0);
//        }
//        if(sp.getInt("temp_unit03",404)==404){
//            editor.putInt("temp_unit03",0);
//        }
//        if(sp.getInt("temp_unit04",404)==404){
//            editor.putInt("temp_unit04",0);
//        }
//        if (sp.getInt("settemp_type01", 404) == 404) {
//            editor.putInt("settemp_type01", 0); //0代表预设，1代表手动
//        }
//        if (sp.getInt("settemp_type02", 404) == 404) {
//            editor.putInt("settemp_type02", 0); //0代表预设，1代表手动
//        }
//        if (sp.getInt("settemp_type03", 404) == 404) {
//            editor.putInt("settemp_type03", 0); //0代表预设，1代表手动
//        }
//        if (sp.getInt("settemp_type04", 404) == 404) {
//            editor.putInt("settemp_type04", 0); //0代表预设，1代表手动
//        }
//
//        if (sp.getInt("preset_location01", 404) == 404) {
//            editor.putInt("preset_location01", 0); //预设位置
//        }
//        if (sp.getInt("preset_location02", 404) == 404) {
//            editor.putInt("preset_location02", 0); //预设位置
//        }
//        if (sp.getInt("preset_location03", 404) == 404) {
//            editor.putInt("preset_location03", 0); //预设位置
//        }
//        if (sp.getInt("preset_location04", 404) == 404) {
//            editor.putInt("preset_location04", 0); //预设位置
//        }
//        if (sp.getInt("preset_location201", 404) == 404) {
//            editor.putInt("preset_location201", 0); //预设位置2
//        }
//        if (sp.getInt("preset_location202", 404) == 404) {
//            editor.putInt("preset_location202", 0); //预设位置2
//        }
//        if (sp.getInt("preset_location203", 404) == 404) {
//            editor.putInt("preset_location203", 0); //预设位置2
//        }
//        if (sp.getInt("preset_location204", 404) == 404) {
//            editor.putInt("preset_location204", 0); //预设位置2
//        }
//        if (sp.getInt("temp_reminder01", 404) == 404) {
//            editor.putInt("temp_reminder01", 88);
//        }
//        if (sp.getInt("temp_reminder02", 404) == 404) {
//            editor.putInt("temp_reminder02", 88);
//        }
//        if (sp.getInt("temp_reminder03", 404) == 404) {
//            editor.putInt("temp_reminder03", 88);
//        }
//        if (sp.getInt("temp_reminder04", 404) == 404) {
//            editor.putInt("temp_reminder04", 88);
//        }
//        if (sp.getInt("time_reminder01", 404) == 404) {
//            editor.putInt("time_reminder01", 60);
//        }
//        if (sp.getInt("time_reminder02", 404) == 404) {
//            editor.putInt("time_reminder02", 60);
//        }
//        if (sp.getInt("time_reminder03", 404) == 404) {
//            editor.putInt("time_reminder03", 60);
//        }
//        if (sp.getInt("time_reminder04", 404) == 404) {
//            editor.putInt("time_reminder04", 60);
//        }
//        if(sp.getInt("temp_manual01",404) == 404){
//            editor.putInt("temp_manual01",100);
//        }
//        if(sp.getInt("temp_manual02",404) == 404){
//            editor.putInt("temp_manual02",100);
//        }
//        if(sp.getInt("temp_manual03",404) == 404){
//            editor.putInt("temp_manual03",100);
//        }
//        if(sp.getInt("temp_manual04",404) == 404){
//            editor.putInt("temp_manual04",100);
//        }
//        if(sp.getInt("sound_reminder01",404)==404){
//            editor.putInt("sound_reminder01",0);
//        }
//        if(sp.getInt("sound_reminder02",404)==404){
//            editor.putInt("sound_remidner02",0);
//        }
//        if(sp.getInt("sound_reminder03",404)==404){
//            editor.putInt("sound_reminder03",0);
//        }
//        if(sp.getInt("sound_reminder04",404)==404){
//            editor.putInt("sond_reminder04",0);
//        }
//
//        editor.apply();
//
//        Variable.temp_unit01 = sp.getInt("temp_unit01", 404);
//        Variable.set_type01 = sp.getInt("settemp_type01", 404);
//        Variable.preset_location01 = sp.getInt("preset_location01", 404);
//        Variable.preset_location201 = sp.getInt("preset_location201", 404);
//        Variable.temp_reminder01 = sp.getInt("temp_reminder01", 404);
//        Variable.time_reminder01 = sp.getInt("time_reminder01", 404);
//        Variable.temp_manual01=sp.getInt("temp_manual01",404);
//        Variable.sound_reminder01=sp.getInt("sound_reminder01",0);
//
//        Variable.temp_unit02 = sp.getInt("temp_unit02", 404);
//        Variable.set_type02 = sp.getInt("settemp_type02", 404);
//        Variable.preset_location02 = sp.getInt("preset_location02", 404);
//        Variable.preset_location202 = sp.getInt("preset_location202", 404);
//        Variable.temp_reminder02 = sp.getInt("temp_reminder02", 404);
//        Variable.time_reminder02= sp.getInt("time_reminder02", 404);
//        Variable.temp_manual02=sp.getInt("temp_manual02",404);
//        Variable.sound_reminder02=sp.getInt("sound_reminder02",0);
//
//        Variable.temp_unit03 = sp.getInt("temp_unit03", 404);
//        Variable.set_type03 = sp.getInt("settemp_type03", 404);
//        Variable.preset_location03 = sp.getInt("preset_location03", 404);
//        Variable.preset_location203 = sp.getInt("preset_location203", 404);
//        Variable.temp_reminder03 = sp.getInt("temp_reminder03", 404);
//        Variable.time_reminder03 = sp.getInt("time_reminder03", 404);
//        Variable.temp_manual03=sp.getInt("temp_manual03",404);
//        Variable.sound_reminder03=sp.getInt("sound_reminder03",0);
//
//        Variable.temp_unit04 = sp.getInt("temp_unit04", 404);
//        Variable.set_type04 = sp.getInt("settemp_type04", 404);
//        Variable.preset_location04= sp.getInt("preset_location04", 404);
//        Variable.preset_location204 = sp.getInt("preset_location204", 404);
//        Variable.temp_reminder04 = sp.getInt("temp_reminder04", 404);
//        Variable.time_reminder04 = sp.getInt("time_reminder04", 404);
//        Variable.temp_manual04=sp.getInt("temp_manual04",404);
//        Variable.sound_reminder04=sp.getInt("sound_reminder04",0);
//    }

