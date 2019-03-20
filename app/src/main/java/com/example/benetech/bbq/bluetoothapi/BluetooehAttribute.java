package com.example.benetech.bbq.bluetoothapi;

import java.util.HashMap;

/**
 * Created by android on 2018/4/16.
 */

public class BluetooehAttribute {
    private static HashMap<String, String> attributes = new HashMap();
    public static String Notification_charateristic = "6e400003-b5a3-f393-e0a9-e50e24dcca9e";
    public static String Write_charateristic = "6e400002-b5a3-f393-e0a9-e50e24dcca9e";
    public static String mluetoothGattDescriptor="00002902-0000-1000-8000-00805f9b34fb";
    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
