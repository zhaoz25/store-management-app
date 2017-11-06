package com.example.dellcorei3.storemanagement.employee.LonginAndCheckin;

/**
 * Created by Admin on 26/09/2017.
 */

public class Nhanvien_checkin {
    public String ipaddress,nhanvien_id,id,email,gio,key;
    public String address,firstName,lastName,fromDate,position;
    public Long gender;
    public Nhanvien_checkin() {
    }

    public Nhanvien_checkin(String ipaddress, String nhanvien_id) {
        this.ipaddress = ipaddress;
        this.nhanvien_id = nhanvien_id;

    }


}
