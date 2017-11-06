package com.example.dellcorei3.storemanagement.store.manager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.store.manager.listview_adapter.EmployeeAdapter;
import com.example.dellcorei3.storemanagement.store.manager.models.Employee;
import com.example.dellcorei3.storemanagement.store.manager.models.Shift;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ShiftDetailActivity extends AppCompatActivity {

    TextView tvFrom,tvTo,tvShiftName;
    ListView lv;

    private DatabaseReference mDatabase;
    private String shiftId;
    ArrayList<Employee> alEmployee;
    EmployeeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_detail);

        addControls();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        alEmployee = new ArrayList<>();
        adapter = new EmployeeAdapter(this,R.layout.listview_employee,alEmployee);
        lv.setAdapter(adapter);

        // nhận dữ liệu từ ShiftActivity
        Bundle bundle = getIntent().getBundleExtra("data");
        shiftId = bundle.getString("shiftid");
        //lấy dữ liệu ca làm việc
        loadShiftData();
        // lấy dữ liệu nhân viên
        loadEmployeeData();

    }

    private void addControls(){
        tvShiftName = (TextView)findViewById(R.id.tvShiftName);
        tvFrom = (TextView)findViewById(R.id.tvShiftFrom);
        tvTo = (TextView)findViewById(R.id.tvShiftTo);

        lv = (ListView)findViewById(R.id.lvShiftDetail);
    }

    private void loadShiftData(){
        mDatabase.child("calamviec").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals(shiftId) == true){
                    Shift shift = dataSnapshot.getValue(Shift.class);

                    tvShiftName.setText(shift.shiftName);
                    tvFrom.setText("Từ "+shift.from);
                    tvTo.setText("Đến "+shift.to);
                }
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
    }

    private void loadEmployeeData(){
        mDatabase.child("nhanvien").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Employee emp = dataSnapshot.getValue(Employee.class);

                if(emp.shiftId.equals(shiftId) == true){
                    alEmployee.add(emp);
                    adapter.notifyDataSetChanged();
                }
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
    }
}
