package com.example.dellcorei3.storemanagement.employee.Firebase;

/**
 * Created by Admin on 31/08/2017.
 */

public class Menu {

    public String thucdon_ten;
    public String thucdon_gia;
    public String nhom_id;
    public String nhom_ten;
    public String key;
    public Menu() {
    }

    public Menu(String thucdon_gia, String thucdon_ten,String nhom_id,String nhom_ten) {
        this.thucdon_ten = thucdon_ten;
        this.nhom_id=nhom_id;
        this.thucdon_gia = thucdon_gia;
        this.nhom_ten = nhom_ten;
    }

    public String getThucdon_ten() {
        return thucdon_ten;
    }
}
