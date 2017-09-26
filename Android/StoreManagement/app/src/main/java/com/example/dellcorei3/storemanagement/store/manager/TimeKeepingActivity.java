package com.example.dellcorei3.storemanagement.store.manager;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dellcorei3.storemanagement.MainActivity;
import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.store.manager.models.CheckIn;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

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

    private DatabaseReference mDatabase;
    ArrayList<CheckIn> alCheckIn;
    ArrayList<Integer> monthList,yearList;

    ArrayAdapter adapterMonth,adapterYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_keeping);

        addControls();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        alCheckIn = new ArrayList<>();
        monthList = new ArrayList<>();
        yearList = new ArrayList<>();
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.equals("") == false && etToDate.getText().toString().equals("") == false){
                    // đổi ngày thành miliseconds và trừ đi chênh lệch múi giờ
                    long from = convertDateToSeconds(etFromdate.getText().toString()) - 36000000;
                    long to = convertDateToSeconds(etToDate.getText().toString()) - 36000000;

                    Query query = mDatabase.child("CheckIn").orderByChild("gio/timestamp").startAt(from).endAt(to);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // dataSnapshot is the "issue" node with all children with id 0
                                for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                    Log.d("data2",issue.getValue().toString());
                                }
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
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // sự kiện khi người dùng chọn picker
        etToDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.equals("") == false && etFromdate.getText().toString().equals("") == false){
                    // đổi ngày thành miliseconds và trừ đi chênh lệch múi giờ
                    long from = convertDateToSeconds(etFromdate.getText().toString()) - 36000000;
                    long to = convertDateToSeconds(s.toString()) + 46620000;

                    Query query = mDatabase.child("CheckIn").orderByChild("gio/timestamp").startAt(from).endAt(to);
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                // dataSnapshot is the "issue" node with all children with id 0
                                for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                    Log.d("data2",issue.getValue().toString());
                                }
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
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDatabase.child("CheckIn").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                CheckIn c = dataSnapshot.getValue(CheckIn.class);
                alCheckIn.add(c);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mDatabase.child("CheckIn").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("We're done loading the initial "+dataSnapshot.getChildrenCount()+" items");
                /*
                for(int i=0;i<alCheckIn.size();i++){
                    int month = Integer.parseInt(alCheckIn.get(i).time.substring(3,5));
                    int year = Integer.parseInt(alCheckIn.get(i).time.substring(6,10));
                    monthList.add(month);
                    yearList.add(year);
                }
                // loại bỏ giá trị giống nhau của mảng và sắp xếp
                monthList = removeDuplicate(monthList);
                Log.d("size",monthList.size()+"");
                // loại bỏ giá trị giống nhau của mảng và sắp xếp
                yearList = removeDuplicate(yearList);

                adapterMonth = new ArrayAdapter(TimeKeepingActivity.this,android.R.layout.simple_spinner_item,monthList);
                adapterMonth.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spMonth.setAdapter(adapterMonth);

                adapterYear = new ArrayAdapter(TimeKeepingActivity.this,android.R.layout.simple_spinner_item,yearList);
                adapterYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spYear.setAdapter(adapterYear);
                */
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addControls(){
        etFromdate = (EditText) findViewById(R.id.etTKFromDate);
        etToDate = (EditText) findViewById(R.id.etTKToDate);
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
    }
}
