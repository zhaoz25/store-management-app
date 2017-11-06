package com.example.dellcorei3.storemanagement.store.manager.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DELL on 9/28/2017.
 */

public class HoaDon implements Serializable{
    public String hoadonId;
    public String trangthai;
    public String ban;
    public Map<String,Object> ngay;
    public String nhanvien_id;
    public String ban_cu;
    public String gio_thanhtoan;
    public int soluong;
    public String tongTien;
    public String khuyenmai_id;

    public String time;

    public HoaDon() {
        tongTien = "";
        ngay = new HashMap<>();
    }
}
