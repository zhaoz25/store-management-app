package com.example.dellcorei3.storemanagement.store.manager.interface_controller;

import com.example.dellcorei3.storemanagement.store.manager.models.HoaDon;

import java.util.ArrayList;

/**
 * Created by DELL on 9/30/2017.
 */

public interface FragmentSendActivity {
    void onSuccessData(ArrayList<HoaDon> al);
    ArrayList<HoaDon> getData();
}
