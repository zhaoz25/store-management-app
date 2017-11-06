package com.example.dellcorei3.storemanagement.employee.CustomListview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dellcorei3.storemanagement.R;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by Admin on 12/09/2017.
 */

public class CT_hoadon_chitietAdapter extends ArrayAdapter<CT_Hoadon_chitiet_layout_2> {
    Context context;
    int layoutResourceId;
    List<CT_Hoadon_chitiet_layout_2> data;
    DecimalFormat decimalFormat;
    public CT_hoadon_chitietAdapter(Context context, int resource, List<CT_Hoadon_chitiet_layout_2> data) {
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
            ct_billHolder.txt_ten = (TextView)row.findViewById(R.id.txt_id_tenmon_ct);
            ct_billHolder.txt_gia = (TextView)row.findViewById(R.id.txt_id_gia_ct);
            ct_billHolder.txt_soluong = (TextView)row.findViewById(R.id.txt_soluong_ct);




            row.setTag(ct_billHolder);
        }
        else
        {
            ct_billHolder = (CT_BillHolder) row.getTag();
        }

        CT_Hoadon_chitiet_layout_2 pf = data.get(position);
        decimalFormat = new DecimalFormat("###,###,###");
        ct_billHolder.txt_ten.setText(String.valueOf(pf.ten));
        float gia = Float.parseFloat(pf.gia);
        ct_billHolder.txt_gia.setText(decimalFormat.format(gia));
        ct_billHolder.txt_soluong.setText(String.valueOf(pf.soluong));

        //Animation cua listview
//        Animation animation = AnimationUtils.loadAnimation(getContext(),R.anim.anim_listview);
//        row.startAnimation(animation);


        return row;
    }
    class CT_BillHolder
    {
        TextView txt_ten,txt_soluong,txt_gia;
    }
}
