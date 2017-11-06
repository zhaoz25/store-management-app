package com.example.dellcorei3.storemanagement.store.manager.models;

/**
 * Created by dellcorei3 on 9/8/2017.
 */

public class Shift {
    public String shiftId;
    public String shiftName;
    public String from;
    public String to;

    public Shift(String shiftName, String from, String to) {
        this.shiftName = shiftName;
        this.from = from;
        this.to = to;
    }
    public Shift() {
    }

    @Override
    public String toString() {
        return shiftName;
    }
}
