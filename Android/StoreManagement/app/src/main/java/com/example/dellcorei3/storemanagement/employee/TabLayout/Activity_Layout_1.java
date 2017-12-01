package com.example.dellcorei3.storemanagement.employee.TabLayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dellcorei3.storemanagement.R;

/**
 * Created by Admin on 30/08/2017.
 */

public class Activity_Layout_1 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.tab_layout_1,container,false);
        return view;
    }
}
