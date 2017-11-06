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
import android.widget.ListView;
import android.widget.TextView;

import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.store.manager.controller.TimeFormat;
import com.example.dellcorei3.storemanagement.store.manager.models.CheckIn;
import com.example.dellcorei3.storemanagement.store.manager.models.Shift;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by DELL on 9/26/2017.
 */

public class TimeKeepingAdapter extends ArrayAdapter<CheckIn> {

    Context context;
    int resource;
    List<CheckIn> data;

    public TimeKeepingAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<CheckIn> data) {
        super(context, resource, data);

        this.resource = resource;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row = convertView;
        TimeKeepingHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new TimeKeepingHolder();
            holder.tvTime = (TextView) row.findViewById(R.id.tvTKTime);
            holder.tvIp = (TextView)row.findViewById(R.id.tvTKIp);

            row.setTag(holder);
        }
        else
        {
            holder = (TimeKeepingHolder) row.getTag();
        }

        CheckIn checkIn = data.get(position);
        String date = TimeFormat.convertTimeStampToDate(checkIn.gio);
        holder.tvTime.setText(date);

        holder.tvIp.setText(checkIn.ipaddress);

        return row;
    }

    class TimeKeepingHolder{
        TextView tvTime;
        TextView tvIp;
    }
}
