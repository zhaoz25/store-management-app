package com.example.dellcorei3.storemanagement.employee.JavaClass;

/**
 * Created by Admin on 27/09/2017.
 */

public class Show_hoadon {
    public String ban,nhanvien_id,tennv="",trangthai="",key,ten="";

    public Show_hoadon() {
        nhanvien_id="";

    }


    public Show_hoadon(String ten, String tennv, String trangthai,String key,String ban) {
        this.ten = ten;
        this.tennv = tennv;
        this.trangthai = trangthai;
        this.key=key;
        this.ban=ban;
    }

}
