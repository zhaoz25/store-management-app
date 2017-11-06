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
import com.example.dellcorei3.storemanagement.store.manager.models.Shift;

import java.util.List;

/**
 * Created by dellcorei3 on 9/14/2017.
 */

public class ShiftAdapter extends ArrayAdapter<Shift> {

    Context context;
    int resource;
    List<Shift> data;

    public ShiftAdapter(Context context,int resource,List<Shift> data) {
        super(context, resource, data);

        this.resource = resource;
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View row = convertView;
        ShiftHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);

            holder = new ShiftHolder();
            holder.lvETShiftName = (TextView) row.findViewById(R.id.lvETShiftName);
            holder.lvETShiftFrom = (TextView)row.findViewById(R.id.lvETShiftFrom);
            holder.lvETShiftTo = (TextView)row.findViewById(R.id.lvETShiftTo);

            row.setTag(holder);
        }
        else
        {
            holder = (ShiftHolder) row.getTag();
        }

        Shift shift = data.get(position);
        holder.lvETShiftName.setText(shift.shiftName);
        holder.lvETShiftFrom.setText(shift.from);
        holder.lvETShiftTo.setText(shift.to);

        return row;
    }

    class ShiftHolder{
        TextView lvETShiftName;
        TextView lvETShiftFrom;
        TextView lvETShiftTo;
    }
}
