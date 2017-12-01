package com.example.dellcorei3.storemanagement.employee.CustomListview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.employee.JavaClass.Show_hoadon;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 25/08/2017.
 */

public class CT_hoadonAdapter extends ArrayAdapter<Show_hoadon> {
    Context context;
    int layoutResourceId;
    List<Show_hoadon> data;
    public CT_hoadonAdapter(Context context, int resource, ArrayList<Show_hoadon> data) {
        super(context, resource,data);
        this.context = context;
        this.layoutResourceId = resource;// error ở đây
        this.data = data;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        CT_BillHolder ct_billHolder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            ct_billHolder = new CT_BillHolder();

            ct_billHolder.txt_ban = (TextView)row.findViewById(R.id.txt_id_ban);
            ct_billHolder.txt_nhanvien = (TextView)row.findViewById(R.id.txt_id_nhanvien);
            ct_billHolder.txt_trangthai = (TextView)row.findViewById(R.id.txt_id_trangthai);

            row.setTag(ct_billHolder);
        }
        else
        {
            ct_billHolder = (CT_BillHolder) row.getTag();
        }
        String tt;
        Show_hoadon pf = data.get(position);
        ct_billHolder.txt_ban.setText(pf.ten.toString());
        ct_billHolder.txt_nhanvien.setText(pf.tennv.toString());
        if(pf.trangthai.equals("chothanhtoan")){
            tt  = "Chờ thanh toán";
            ct_billHolder.txt_trangthai.setText(tt);
            ct_billHolder.txt_trangthai.setBackgroundColor(Color.parseColor("#FA5858"));
        }else if(pf.trangthai.equals("chuaphucvu")){
            tt = "Chưa phục vụ";
            ct_billHolder.txt_trangthai.setText(tt);
            ct_billHolder.txt_trangthai.setBackgroundColor(Color.parseColor("#FFBF00"));
        }else if (pf.trangthai.equals("phucvu")){
            tt="Phục vụ";
            ct_billHolder.txt_trangthai.setText(tt);
            ct_billHolder.txt_trangthai.setBackgroundColor(Color.parseColor("#00FF80"));
        }


//        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.anim_listview);
//        row.startAnimation(animation);
        return row;
    }
    class CT_BillHolder
    {
        TextView txt_ban,txt_nhanvien,txt_trangthai;
    }
}
