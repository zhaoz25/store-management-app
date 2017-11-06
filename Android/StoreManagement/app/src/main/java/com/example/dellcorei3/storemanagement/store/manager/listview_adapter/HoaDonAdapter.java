package com.example.dellcorei3.storemanagement.store.manager.listview_adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.store.manager.controller.TimeFormat;
import com.example.dellcorei3.storemanagement.store.manager.models.CheckIn;
import com.example.dellcorei3.storemanagement.store.manager.models.HoaDon;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by DELL on 9/28/2017.
 */

public class HoaDonAdapter extends ArrayAdapter<HoaDon> {

    Context context;
    int resource;
    List<HoaDon> data;

    public HoaDonAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<HoaDon> data) {
        super(context, resource, data);

        this.resource = resource;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        HoaDonHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new HoaDonHolder();
            holder.tvNgay = (TextView) row.findViewById(R.id.tvHDNgay);
            holder.tvTien = (TextView)row.findViewById(R.id.tvHDTien);
            holder.tvtrangThai = (TextView)row.findViewById(R.id.tvHDTrangThai);

            row.setTag(holder);
        }
        else
        {
            holder = (HoaDonHolder) row.getTag();
        }

        HoaDon hd = data.get(position);
        String date = TimeFormat.convertTimeStampToDate(hd.ngay);
        holder.tvNgay.setText(date);

        // currency format
        NumberFormat formatter = new DecimalFormat("#,###");
        String formatNumber = formatter.format(Long.parseLong(hd.tongTien));
        holder.tvTien.setText(formatNumber);

        holder.tvtrangThai.setTextColor(Color.BLACK);
        if(hd.trangthai.equals("chuaphucvu")){
            holder.tvtrangThai.setText("Chưa phục vụ");
        }else if(hd.trangthai.equals("phucvu")){
            holder.tvtrangThai.setText("Đã phục vụ");
        }else if(hd.trangthai.equals("chothanhtoan")){
            holder.tvtrangThai.setText("Chờ thanh toán");
        }
        else{
            holder.tvtrangThai.setTextColor(Color.BLUE);
            holder.tvtrangThai.setText("Đã thanh toán");
        }

        return row;
    }

    class HoaDonHolder{
        TextView tvNgay;
        TextView tvTien;
        TextView tvtrangThai;
    }
}
