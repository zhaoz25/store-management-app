package com.example.dellcorei3.storemanagement.store.manager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.store.manager.listview_adapter.EmployeeAdapter;
import com.example.dellcorei3.storemanagement.store.manager.listview_adapter.ShiftAdapter;
import com.example.dellcorei3.storemanagement.store.manager.models.Employee;
import com.example.dellcorei3.storemanagement.store.manager.models.HoaDon;
import com.example.dellcorei3.storemanagement.store.manager.models.Shift;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EmployeeManagementActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{

    private ListView lvEmployee;
    SearchView searchView;
    Button btEfficient;

    private DatabaseReference mDatabase;
    ArrayList<Employee> alEmployee;
    EmployeeAdapter adapter;
    ProgressDialog progressDialog;

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

        lvEmployee.setTextFilterEnabled(true);
        setupSearchView();
        // tạo sự kiện click listview
        lvEmployee.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Employee emp = adapter.getItem(position);
                Intent it = new Intent(EmployeeManagementActivity.this,EmployeeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("employeeid",emp.id);
                it.putExtra("data",bundle);

                startActivity(it);
            }
        });

        btEfficient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(EmployeeManagementActivity.this,EmployeeEfficientActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("employeelist",alEmployee);

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
        Employee emp = adapter.getItem(position);

        switch (item.getItemId()) {
            case R.id.contextEmployeeTimeKeeping:
                Intent it = new Intent(EmployeeManagementActivity.this,TimeKeepingActivity.class);

                Bundle bundle = new Bundle();
                bundle.putString("employeeid",emp.id);
                bundle.putString("employeename",emp.firstName+" "+emp.lastName);
                it.putExtra("data",bundle);

                startActivity(it);
                break;
        }

        return super.onContextItemSelected(item);
    }

    private void addControls(){
        lvEmployee = (ListView)findViewById(R.id.lvEmployeeList);
        btEfficient = (Button)findViewById(R.id.btEfficient);

        searchView = (SearchView)findViewById(R.id.svEmployee);
    }

    private void setupSearchView()
    {
        //searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Here");

    }

    private void createListViewData() {
        progressDialog = new ProgressDialog(EmployeeManagementActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);
        progressDialog.show();

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
        mDatabase.child("nhanvien").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // sắp xếp mảng nhanvien
                for(int i=0;i<alEmployee.size();i++){
                    for(int j=i;j<alEmployee.size();j++){
                        int nv1 = alEmployee.get(i).state;
                        int nv2 = alEmployee.get(j).state;
                        if(nv1 < nv2){
                            Employee temp = alEmployee.get(i);
                            alEmployee.set(i,alEmployee.get(j));
                            alEmployee.set(j,temp);
                        }
                    }
                }
                adapter.notifyDataSetChanged();

                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            lvEmployee.clearTextFilter();
        } else {
            lvEmployee.setFilterText(newText);
        }
        return true;
    }
}
