package com.example.dellcorei3.storemanagement.employee.CustomListview;

/**
 * Created by Admin on 31/08/2017.
 */

public class CT_hoadon_layout_2 {
    public String thucdon_ten;
    public String thucdon_gia;

    public String nhom_ten="";
    public String key;




    public CT_hoadon_layout_2(String thucdon_gia, String thucdon_ten,String nhom_ten,String key) {
        this.thucdon_ten = thucdon_ten;
        this.nhom_ten = nhom_ten;
        this.key = key;
        this.thucdon_gia = thucdon_gia;



    }
    public String getThucdon_ten(){
        return thucdon_ten;
    }

}
