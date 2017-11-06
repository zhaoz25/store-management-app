package com.example.dellcorei3.storemanagement.store.manager.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DELL on 9/23/2017.
 */

public class CheckIn implements Serializable{
    public String nhanvien_id;
    public String ipaddress;
    public Map<String,Object> gio;
    public String time;

    public CheckIn() {
        nhanvien_id = "";
        ipaddress = "";
        gio = new HashMap<>();
    }
}
