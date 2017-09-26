package com.example.dellcorei3.storemanagement.store.manager.listview_adapter;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.store.manager.controller.TimeFormat;
import com.example.dellcorei3.storemanagement.store.manager.models.Employee;
import com.example.dellcorei3.storemanagement.store.manager.models.Shift;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by dellcorei3 on 9/19/2017.
 */

public class EmployeeAdapter extends ArrayAdapter<Employee> {

    Context context;
    int resource;
    List<Employee> data;

    public EmployeeAdapter(Context context,int resource,List<Employee> data) {
        super(context, resource, data);

        this.resource = resource;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        EmployeeHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new EmployeeHolder();
            holder.img = (ImageView) row.findViewById(R.id.imgEmployee);
            holder.tvName = (TextView)row.findViewById(R.id.tvEmployeeName);
            holder.tvFromDate = (TextView)row.findViewById(R.id.tvEmployeeFromDate);
            holder.tvPosition = (TextView)row.findViewById(R.id.tvEmployeePosition);

            row.setTag(holder);
        }
        else
        {
            holder = (EmployeeHolder) row.getTag();
        }

        Employee emp = data.get(position);

        // hiển thị ảnh đại diện
        if(emp.gender == 0) {
            holder.img.setImageResource(R.drawable.male);
        }
        else{
            holder.img.setImageResource(R.drawable.female);
        }
        // hien thi mau chu
        if(emp.state == 0){
            holder.tvName.setTextColor(ContextCompat.getColor(context,R.color.colorEmployee0));
        }
        else{
            holder.tvName.setTextColor(ContextCompat.getColor(context,R.color.colorEmployee1));
        }
        holder.tvName.setText(emp.firstName+" "+emp.lastName);
        // hiển thị ngày vào làm
        holder.tvFromDate.setText(emp.fromDate);
        // hiển thị chức vụ
        if(emp.position.equals("phucvu")==true){
            holder.tvPosition.setText("Phục vụ");
        }
        else{
            holder.tvPosition.setText("Thu ngân");
        }

        return row;
    }

    class EmployeeHolder{
        ImageView img;
        TextView tvName;
        TextView tvFromDate;
        TextView tvPosition;
    }
}
