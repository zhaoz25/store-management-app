package com.example.dellcorei3.storemanagement.employee.JavaClass;

/**
 * Created by Admin on 08/09/2017.
 */

public class Hoadon_chitiet {
    public String thucdon_id,soluong,ten;
    public String gia;
    public String key;
    public String tongtien;


    public Hoadon_chitiet() {
    }

    public Hoadon_chitiet(String ten,String thucdon_id, String soluong, String gia,String tongtien) {
        this.ten = ten;
        this.gia=gia;
        this.thucdon_id = thucdon_id;
        this.soluong = soluong;
        this.tongtien=tongtien;
    }


}
