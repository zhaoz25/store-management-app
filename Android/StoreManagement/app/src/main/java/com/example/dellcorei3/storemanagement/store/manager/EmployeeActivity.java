package com.example.dellcorei3.storemanagement.store.manager;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.dellcorei3.storemanagement.MainActivity;
import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.store.manager.controller.TimeFormat;
import com.example.dellcorei3.storemanagement.store.manager.models.Employee;
import com.example.dellcorei3.storemanagement.store.manager.models.Shift;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class EmployeeActivity extends AppCompatActivity {

    EditText etEmployeeName,etEmployeeFirstName,etEmployeeEmail,etEmployeeFrom,etEmployeeTo;
    EditText etEmployeeAddress;
    RadioButton rdMale,rdFemale,rdPhucVu,rdThuNgan;
    Spinner spEmployeeShift;
    Switch tgButtonState;
    Button btEmployeeUpdate;

    private DatabaseReference mDatabase;
    ArrayAdapter adapter;
    ArrayList<Shift> alShift;
    String empId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        addControls();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        alShift = new ArrayList<>();
        // nhận dữ liệu từ EmployeeManagement
        Bundle bundle = getIntent().getBundleExtra("data");
        empId = bundle.getString("employeeid");
        //// lấy dữ liệu ca làm việc từ firebase
        adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,alShift);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        loadShiftData();
        spEmployeeShift.setAdapter(adapter);


        // su kien click button
        btEmployeeUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmployeeEmail.getText().toString();
                String firstName = etEmployeeFirstName.getText().toString();
                String lastName = etEmployeeName.getText().toString();
                String address = etEmployeeAddress.getText().toString();
                int gender = 0;
                if(rdFemale.isChecked()==true){
                    gender=1;
                }
                String employeePosition="";
                if(rdPhucVu.isChecked()==true){
                    employeePosition="phucvu";
                }
                else{
                    employeePosition="thungan";
                }

                int state=0;
                if(tgButtonState.isChecked()==true){
                    state=1;
                }
                // lấy id của ca làm việc trong spinner
                String shiftId = alShift.get(spEmployeeShift.getSelectedItemPosition()).shiftId;
                // update thông tin nhân viên
                Employee emp = new Employee(email,firstName,lastName,employeePosition,state);
                emp.id = empId;
                emp.fromDate = etEmployeeFrom.getText().toString();
                emp.toDate = etEmployeeTo.getText().toString();
                emp.gender = gender;
                emp.address=address;
                emp.shiftId = shiftId;
                updateData(emp);
            }
        });
        // su kien click edit text
        etEmployeeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentDate = Calendar.getInstance();
                DatePickerDialog date = new DatePickerDialog(EmployeeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                etEmployeeFrom.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                            }
                        },
                        currentDate.get(Calendar.YEAR),
                        currentDate.get(Calendar.MONTH),
                        currentDate.get(Calendar.DAY_OF_MONTH));
                date.show();
            }
        });
        etEmployeeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentDate = Calendar.getInstance();
                DatePickerDialog date = new DatePickerDialog(EmployeeActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                etEmployeeTo.setText(dayOfMonth + "/" + (month+1) + "/" + year);
                            }
                        },
                        currentDate.get(Calendar.YEAR),
                        currentDate.get(Calendar.MONTH),
                        currentDate.get(Calendar.DAY_OF_MONTH));
                date.show();
            }
        });
    }



    private void addControls(){
        etEmployeeName = (EditText)findViewById(R.id.etEmployeeName);
        etEmployeeFirstName = (EditText)findViewById(R.id.etEmployeeFirsrtName);
        etEmployeeEmail = (EditText)findViewById(R.id.etEmployeeEmail);
        etEmployeeFrom = (EditText)findViewById(R.id.etEmployeeFrom);
        etEmployeeTo = (EditText)findViewById(R.id.etEmployeeTo);
        etEmployeeAddress = (EditText) findViewById(R.id.etEmployeeAddress);

        rdMale = (RadioButton)findViewById(R.id.rdMale);
        rdFemale = (RadioButton)findViewById(R.id.rdFemale);
        rdPhucVu = (RadioButton)findViewById(R.id.rdEmployeePhucVu);
        rdThuNgan = (RadioButton)findViewById(R.id.rdEmployeeThuNgan);

        spEmployeeShift = (Spinner) findViewById(R.id.spEmployeeShift);

        tgButtonState = (Switch) findViewById(R.id.tgButtonState);
        btEmployeeUpdate = (Button) findViewById(R.id.btEmployeeUpdate);

    }

    private void loadData(final String empId){
        mDatabase.child("nhanvien").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals(empId) == true) {
                    Employee emp = dataSnapshot.getValue(Employee.class);

                    etEmployeeEmail.setText(emp.email);
                    etEmployeeName.setText(emp.lastName);
                    etEmployeeFirstName.setText(emp.firstName);
                    // hiển thị ngày vào làm
                    etEmployeeFrom.setText(emp.fromDate);
                    etEmployeeTo.setText(emp.toDate);

                    etEmployeeAddress.setText(emp.address);
                    //chọn radio button
                    if (emp.gender == 0) {
                        rdMale.setChecked(true);
                    } else {
                        rdFemale.setChecked(true);
                    }

                    if(emp.position.equals("phucvu") == true){
                        rdPhucVu.setChecked(true);
                    }
                    else{
                        rdThuNgan.setChecked(true);
                    }
                    // chọn toggle button
                    if (emp.state == 0) {
                        tgButtonState.setText("Vô hiệu hóa");
                        tgButtonState.setChecked(false);
                    } else {
                        tgButtonState.setText("Đang hoạt động");
                        tgButtonState.setChecked(true);
                    }
                    // chọn spinner
                    for(int i=0;i<alShift.size();i++){
                        if(alShift.get(i).shiftId.equals(emp.shiftId)==true){
                            spEmployeeShift.setSelection(i);
                        }
                    }

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

    private void loadShiftData(){
        mDatabase.child("calamviec").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Shift shift = new Shift();
                shift = dataSnapshot.getValue(Shift.class);
                shift.shiftId = dataSnapshot.getKey();

                alShift.add(shift);
                adapter.notifyDataSetChanged();
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
        // kiểm tra hoàn thành load data
        mDatabase.child("calamviec").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("We're done loading the initial "+dataSnapshot.getChildrenCount()+" items");
                // lấy dữ liệu nhân viên từ firebase
                loadData(empId);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateData(Employee emp){
        mDatabase.child("nhanvien").child(empId).setValue(emp);
        Toast.makeText(EmployeeActivity.this, "Cập nhật thành công!",
                Toast.LENGTH_SHORT).show();
    }
}
