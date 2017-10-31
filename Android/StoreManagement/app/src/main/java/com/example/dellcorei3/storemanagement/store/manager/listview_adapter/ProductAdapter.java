package com.example.dellcorei3.storemanagement.store.manager.listview_adapter;

import android.app.Activity;
import android.content.Context;
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
import com.example.dellcorei3.storemanagement.store.manager.models.HoaDon;
import com.example.dellcorei3.storemanagement.store.manager.models.StatisticProduct;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by DELL on 10/4/2017.
 */

public class ProductAdapter extends ArrayAdapter<StatisticProduct> {
    Context context;
    int resource;
    List<StatisticProduct> data;

    public ProductAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<StatisticProduct> data) {
        super(context, resource, data);

        this.resource = resource;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ProductHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new ProductHolder();
            holder.tvTen = (TextView) row.findViewById(R.id.tvPTen);
            holder.tvSoluong = (TextView)row.findViewById(R.id.tvPSoluong);
            holder.tvTongtien = (TextView)row.findViewById(R.id.tvPTongTien);

            row.setTag(holder);
        }
        else
        {
            holder = (ProductHolder) row.getTag();
        }

        StatisticProduct product = data.get(position);

        holder.tvTen.setText(product.ten);
        holder.tvSoluong.setText(product.soluong+"");
        // currency format
        NumberFormat formatter = new DecimalFormat("#,###");
        String formatNumber = formatter.format(product.tongtien);
        holder.tvTongtien.setText(formatNumber);

        return row;
    }

    class ProductHolder{
        TextView tvTen;
        TextView tvSoluong;
        TextView tvTongtien;
    }
}
