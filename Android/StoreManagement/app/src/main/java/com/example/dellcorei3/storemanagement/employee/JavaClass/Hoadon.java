package com.example.dellcorei3.storemanagement.employee.JavaClass;

import java.util.Map;

/**
 * Created by Admin on 08/09/2017.
 */

public class Hoadon {
    public String ban;
    public String nhanvien_id;

    public String trangthai;
    public Map<String,Object> ngay;
    public String tongtien;
    public Hoadon() {
    }

    public Hoadon( String nhanvien_id, String ban,String trangthai) {


        this.nhanvien_id = nhanvien_id;
        this.ban = ban;
        this.trangthai=trangthai;
    }

}
