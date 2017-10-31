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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.store.manager.controller.TimeFormat;
import com.example.dellcorei3.storemanagement.store.manager.models.Employee;
import com.example.dellcorei3.storemanagement.store.manager.models.Shift;

import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by dellcorei3 on 9/19/2017.
 */

public class EmployeeAdapter extends ArrayAdapter<Employee> implements Filterable{

    Context context;
    int resource;
    ArrayList<Employee> data;
    public ArrayList<Employee> orig;

    public EmployeeAdapter(Context context,int resource,ArrayList<Employee> data) {
        super(context, resource, data);

        this.resource = resource;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final ArrayList<Employee> results = new ArrayList<Employee>();
                if (orig == null) {
                    orig = data;
                }
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Employee g : orig) {
                            if (removeAccent(g.getName().toLowerCase()).contains(constraint.toString())) {
                                results.add(g);
                            }
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                data = (ArrayList<Employee>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Employee getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
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

    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }
}
