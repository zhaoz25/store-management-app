package com.example.dellcorei3.storemanagement.employee.tabadapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.dellcorei3.storemanagement.employee.TabLayout.Activity_Layout_1;
import com.example.dellcorei3.storemanagement.employee.TabLayout.Activity_Layout_2;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 12/09/2017.
 */

public class Tab_Adapter extends FragmentPagerAdapter {
    List<Fragment> fragmentList = new ArrayList<>();
    List<String> title = new ArrayList<>();
    String tab1 = "Thực Đơn",tab2="Món Ăn Đã Chọn";
    public Tab_Adapter(FragmentManager fm) {
        super(fm);
        fragmentList.add(new Activity_Layout_1());
        title.add(tab1);
        fragmentList.add(new Activity_Layout_2());
        title.add(tab2);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return super.getPageTitle(position);
    }
}
