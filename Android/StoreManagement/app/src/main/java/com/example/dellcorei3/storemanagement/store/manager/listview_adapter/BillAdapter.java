package com.example.dellcorei3.storemanagement.store.manager.listview_adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.store.manager.models.HoaDonChiTiet;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by DELL on 10/7/2017.
 */

public class BillAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<HoaDonChiTiet>> listDataChild;

    public BillAdapter(Context context, ArrayList<String> listDataHeader,
                                     HashMap<String, ArrayList<HoaDonChiTiet>> listChildData) {
        this.context = context;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return listDataChild.get(listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String data = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.listview_header_bill, null);
        }

        TextView tvHeader = (TextView) convertView.findViewById(R.id.tvBillHeaderLV);
        tvHeader.setText(data);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final HoaDonChiTiet data = (HoaDonChiTiet) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.listview_bill_item, null);
        }

        TextView tvTen = (TextView) convertView.findViewById(R.id.tvBillDTen);
        TextView tvSoLuong = (TextView) convertView.findViewById(R.id.tvBillDSoluong);
        TextView tvTong = (TextView) convertView.findViewById(R.id.tvBillDTongTien);

        tvTen.setText(data.tenMon);
        tvSoLuong.setText(data.soluong);
        tvTong.setText(data.tongtien);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
