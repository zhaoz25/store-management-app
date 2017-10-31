package com.example.dellcorei3.storemanagement.store.manager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.TimeZoneFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dellcorei3.storemanagement.MainActivity;
import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.store.manager.controller.TimeFormat;
import com.example.dellcorei3.storemanagement.store.manager.listview_adapter.EmployeeAdapter;
import com.example.dellcorei3.storemanagement.store.manager.listview_adapter.TimeKeepingAdapter;
import com.example.dellcorei3.storemanagement.store.manager.models.CheckIn;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.TimeZone;

public class TimeKeepingActivity extends AppCompatActivity {

    EditText etFromdate,etToDate;
    ListView lv;
    TextView tvEmployeeName,tvSum;

    private DatabaseReference mDatabase;
    ArrayList<CheckIn> alCheckIn;
    ArrayList<CheckIn> alFilter;
    String employeeId;

    TimeKeepingAdapter adapterTimeKeeping;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_keeping);

        addControls();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        alCheckIn = new ArrayList<>();
        alFilter = new ArrayList<CheckIn>();
        // lấy data từ class EmployeeManagement
        Bundle bundle = getIntent().getBundleExtra("data");
        employeeId = bundle.getString("employeeid");
        tvEmployeeName.setText(bundle.getString("employeename"));

        // tạo time picker dialog cho edittext
        etFromdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTimePickerDialog(etFromdate);
            }
        });
        etToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTimePickerDialog(etToDate);
            }
        });
        // sự kiện khi người dùng chọn picker
        etFromdate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.equals("") == false && etToDate.getText().toString().equals("") == false){
                    // đổi ngày thành miliseconds
                    long from = convertDateToSeconds(etFromdate.getText().toString());
                    long to = convertDateToSeconds(etToDate.getText().toString()) + 85680000;// thay đổi tới cuối ngày

                    getCheckIn(from,to);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        // sự kiện khi người dùng chọn picker
        etToDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.equals("") == false && etFromdate.getText().toString().equals("") == false){
                    // đổi ngày thành miliseconds và trừ đi chênh lệch múi giờ
                    long from = convertDateToSeconds(etFromdate.getText().toString());
                    long to = convertDateToSeconds(s.toString()) + 85680000;

                    getCheckIn(from,to);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // sự kiện listview clicked
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(TimeKeepingActivity.this,TKDetailsActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("time",alFilter.get(position).time);
                bundle1.putSerializable("checkinlist",alCheckIn);

                it.putExtra("data",bundle1);
                startActivity(it);
            }
        });

    }

    private void addControls(){
        etFromdate = (EditText) findViewById(R.id.etTKFromDate);
        etToDate = (EditText) findViewById(R.id.etTKToDate);
        tvEmployeeName = (TextView)findViewById(R.id.tvEmployeeName);
        tvSum = (TextView)findViewById(R.id.tvSumTimeKeeping);

        lv = (ListView)findViewById(R.id.lvTimeKeeping);
    }

    private void getCheckIn(Long from,Long to){
        alCheckIn.clear();

        Query query = mDatabase.child("CheckIn").orderByChild("gio/timestamp").startAt(from).endAt(to);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Log.d("data2",issue.getValue().toString());

                        CheckIn checkIn = issue.getValue(CheckIn.class);
                        // nếu đúng nhân viên id mới thêm vào mảng
                        if(checkIn.nhanvien_id.equals(employeeId) == true) {
                            alCheckIn.add(checkIn);
                        }
                    }
                    // xóa các ngày checkin nhiều lần
                    if(alCheckIn.size() > 0) {
                        alFilter = removeDeplicate(alCheckIn);
                    }
                    else{
                        Toast.makeText(TimeKeepingActivity.this, "Không có dữ liệu!",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    adapterTimeKeeping = new TimeKeepingAdapter(TimeKeepingActivity.this,R.layout.listview_timekeeping,alFilter);
                    lv.setAdapter(adapterTimeKeeping);
                    adapterTimeKeeping.notifyDataSetChanged();
                    // hiển thị tổng công
                    tvSum.setText(alFilter.size()+"");
                }
                else{
                    Toast.makeText(TimeKeepingActivity.this, "Không có dữ liệu!",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private Long convertDateToSeconds(String s){
        String myDate = s+" 00:00";
        long millis = 0;
        try {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = sdf.parse(myDate);
            String a = sdf.format(date);
            millis = date.getTime();
            Log.d("time",millis+"");
            Log.d("date",a+"");
        } catch (ParseException e) {
            Log.d("error", e.getMessage());
        }
        return millis;
    }

    private void createTimePickerDialog(final EditText et){
        Calendar currentDate = Calendar.getInstance();


        DatePickerDialog date = new DatePickerDialog(TimeKeepingActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int yearP, int monthP, int dayOfMonth) {
                        et.setText(dayOfMonth + "/" + (monthP+1) + "/" + yearP);

                    }
                },
                currentDate.get(Calendar.YEAR),
                currentDate.get(Calendar.MONTH),
                currentDate.get(Calendar.DAY_OF_MONTH));
        date.show();
    }
    /*
    private ArrayList<Integer> removeDuplicate(ArrayList<Integer> al){
        int end = al.size();

        Set<Integer> set = new HashSet<Integer>();
        for(int i = 0; i < end; i++){
            set.add(al.get(i));
        }

        ArrayList<Integer> alFilter = new ArrayList<>(set);
        // sắp xếp mảng
        Collections.sort(alFilter);
        System.out.println(alFilter);
        return alFilter;
    }*/
    private ArrayList<CheckIn> removeDeplicate(ArrayList<CheckIn> alCheck){
        ArrayList<CheckIn> al = new ArrayList<CheckIn>(alCheck);
        ArrayList<CheckIn> alFilter = new ArrayList<>();
        //sorting the elements
        for(int i=0;i<al.size();i++)
        {
            for(int j=i;j<al.size();j++)
            {
                Long arrI = (Long)al.get(i).gio.get("timestamp");
                Long arrJ = (Long)al.get(j).gio.get("timestamp");
                if(arrI > arrJ)
                {
                    CheckIn temp=al.get(i);
                    al.set(i,al.get(j));
                    al.set(j,temp);
                }
            }
        }

        //After sorting
        for(int i=0;i<al.size();i++)
        {
            Log.d("sort",al.get(i).gio.get("timestamp").toString());
            al.get(i).time = TimeFormat.convertTimeStampToDate(al.get(i).gio);
        }

        int b = 0;
        al.set(b,al.get(0));
        // loại bỏ thành phần giống nhau theo ngày
        for(int i=0;i<al.size();i++)
        {
            String sB = al.get(b).time.substring(0,10);
            Log.d("a",sB);
            String sI = al.get(i).time.substring(0,10);
            if (sB.equals(sI) == false)
            {
                b++;
                al.set(b,al.get(i));
            }
        }
        for (int i=0;i<=b;i++ )
        {
            alFilter.add(al.get(i));
            Log.d("sort",al.get(i).time);
        }
        return alFilter;
    }
}
