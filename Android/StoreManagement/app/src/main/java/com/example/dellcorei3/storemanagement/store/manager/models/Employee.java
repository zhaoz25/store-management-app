package com.example.dellcorei3.storemanagement.store.manager.models;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dellcorei3 on 9/17/2017.
 */

public class Employee {
    public String id;
    public String email;
    public String lastName,firstName;
    public int gender;
    public String shiftId;
    public String address;
    public String position;
    public int state = 0; // 0 la ko hoat dong, 1 la dang hoat dong
    public String fromDate,toDate;

    public Employee() {

    }

    public Employee(String email,String firstName,String lastName,String position,int state) {
        this.email = email;
        this.lastName=lastName;
        this.firstName=firstName;
        gender = 0;
        shiftId = "";
        address = "";
        fromDate = "";
        toDate = "";
        this.position = position;
        this.state = state;
    }
}
