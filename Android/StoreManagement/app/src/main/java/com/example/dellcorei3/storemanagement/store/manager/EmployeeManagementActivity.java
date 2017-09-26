package com.example.dellcorei3.storemanagement.store.manager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.store.manager.listview_adapter.EmployeeAdapter;
import com.example.dellcorei3.storemanagement.store.manager.listview_adapter.ShiftAdapter;
import com.example.dellcorei3.storemanagement.store.manager.models.Employee;
import com.example.dellcorei3.storemanagement.store.manager.models.Shift;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class EmployeeManagementActivity extends AppCompatActivity {

    private ListView lvEmployee;

    private DatabaseReference mDatabase;
    ArrayList<Employee> alEmployee;
    EmployeeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_management);

        addControls();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        alEmployee = new ArrayList<>();
        adapter = new EmployeeAdapter(this,R.layout.listview_employee,alEmployee);
        // lấy dữ liệu cho mảng
        createListViewData();
        // set adapter cho listview
        lvEmployee.setAdapter(adapter);
        // gắn sự kiện context menu cho listview
        registerForContextMenu(lvEmployee);
        // tạo sự kiện click listview
        lvEmployee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent(EmployeeManagementActivity.this,EmployeeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("employeeid",alEmployee.get(position).id);
                it.putExtra("data",bundle);

                startActivity(it);
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_employee, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;

        switch (item.getItemId()) {
            case R.id.contextEmployeeTimeKeeping:
                Intent it = new Intent(EmployeeManagementActivity.this,TimeKeepingActivity.class);
                startActivity(it);
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void addControls(){
        lvEmployee = (ListView)findViewById(R.id.lvEmployeeList);
    }

    private void createListViewData() {
        mDatabase.child("nhanvien").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Employee emp = dataSnapshot.getValue(Employee.class);
                emp.id = dataSnapshot.getKey();

                alEmployee.add(emp);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Employee emp = dataSnapshot.getValue(Employee.class);
                for(int i=0;i<alEmployee.size();i++){
                    if(alEmployee.get(i).id.equals(dataSnapshot.getKey()) == true){
                        alEmployee.set(i,emp);

                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                for(int i=0;i<alEmployee.size();i++){
                    if(alEmployee.get(i).id.equals(dataSnapshot.getKey()) == true){
                        alEmployee.remove(i);

                    }
                }
                adapter.notifyDataSetChanged();
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
