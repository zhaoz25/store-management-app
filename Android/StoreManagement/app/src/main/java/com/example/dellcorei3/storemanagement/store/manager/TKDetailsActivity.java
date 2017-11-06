package com.example.dellcorei3.storemanagement.store.manager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.store.manager.listview_adapter.TimeKeepingAdapter;
import com.example.dellcorei3.storemanagement.store.manager.models.CheckIn;

import java.util.ArrayList;

public class TKDetailsActivity extends AppCompatActivity {

    TextView tvTime;
    ListView lv;

    ArrayList<CheckIn> alCheckIn;
    ArrayList<CheckIn> alFilter;
    TimeKeepingAdapter adapterTimeKeeping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tkdetails);

        addControls();

        Bundle bundle = getIntent().getBundleExtra("data");
        alCheckIn = (ArrayList<CheckIn>) bundle.getSerializable("checkinlist");
        String time = bundle.getString("time");

        alFilter = new ArrayList<>();
        // lấy bảng checkin theo ngày
        for(int i=0;i<alCheckIn.size();i++){
            if(alCheckIn.get(i).time.equals(time) == true){
                alFilter.add(alCheckIn.get(i));
            }
        }

        adapterTimeKeeping = new TimeKeepingAdapter(TKDetailsActivity.this,R.layout.listview_timekeeping,alFilter);
        lv.setAdapter(adapterTimeKeeping);
        tvTime.setText("Chi tiết chấm công ngày "+time.substring(0,10));

    }


    private void addControls(){
        tvTime = (TextView)findViewById(R.id.tvTKTime);

        lv = (ListView)findViewById(R.id.lvTKDetails);
    }
}
