package com.example.dellcorei3.storemanagement.store.manager;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dellcorei3.storemanagement.MainActivity;
import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.store.manager.controller.ChartHandler;
import com.example.dellcorei3.storemanagement.store.manager.controller.TimeFormat;
import com.example.dellcorei3.storemanagement.store.manager.listview_adapter.HoaDonAdapter;
import com.example.dellcorei3.storemanagement.store.manager.models.EEChartData;
import com.example.dellcorei3.storemanagement.store.manager.models.Employee;
import com.example.dellcorei3.storemanagement.store.manager.models.HoaDon;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class EmployeeEfficientActivity extends AppCompatActivity {

    PieChart pieChart ;
    EditText etFrom,etTo;

    ArrayList<Entry> entries ;
    ArrayList<String> PieEntryLabels ;
    PieDataSet pieDataSet ;
    PieData pieData ;

    ArrayList<Employee> alEmployee;
    ArrayList<HoaDon> alHoaDon;
    ArrayList<EEChartData> alChart;
    private DatabaseReference mDatabase;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_efficient);

        addControls();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        alHoaDon = new ArrayList<>();
        Bundle bundle = getIntent().getBundleExtra("data");
        if(bundle != null) {
            alEmployee = (ArrayList<Employee>) bundle.getSerializable("employeelist");
        }
        else{
            alEmployee = new ArrayList<>();
        }
        // sự kiện click edit text
        etFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTimePickerDialog(etFrom);
            }
        });
        etTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTimePickerDialog(etTo);
            }
        });
        // sự kiện thay đổi edittext
        etFrom.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(etFrom.getText().toString().equals("") == false && etTo.getText().toString().equals("") == false) {
                    // đổi ngày thành miliseconds và trừ đi chênh lệch múi giờ
                    long from = TimeFormat.convertDateToSeconds(etFrom.getText().toString());
                    long to = TimeFormat.convertDateToSeconds(etTo.getText().toString()) + 85680000;

                    getHoaDon(from,to);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        etTo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.equals("") == false && etFrom.getText().toString().equals("") == false) {
                    // đổi ngày thành miliseconds và trừ đi chênh lệch múi giờ
                    long from = TimeFormat.convertDateToSeconds(etFrom.getText().toString());
                    long to = TimeFormat.convertDateToSeconds(etTo.getText().toString()) + 85680000;
                    Log.d("to",to+"");
                    getHoaDon(from,to);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });


    }

    private void addControls(){
        pieChart = (PieChart) findViewById(R.id.chart2);

        etFrom = (EditText)findViewById(R.id.etEfficientFrom);
        etTo = (EditText)findViewById(R.id.etEfficientTo);
    }

    private void displayPieChart(){
        entries = new ArrayList<>();
        PieEntryLabels = new ArrayList<String>();

        AddValuesToPIEENTRY();
        AddValuesToPieEntryLabels();

        pieDataSet = new PieDataSet(entries, "");
        pieData = new PieData(PieEntryLabels, pieDataSet);

        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextSize(14);

        pieChart.setData(pieData);
        pieChart.animateY(3000);
    }

    public void AddValuesToPIEENTRY(){

        for(int i=0;i<alChart.size();i++){
            entries.add(new BarEntry((float)alChart.get(i).value, i));
        }
    }

    public void AddValuesToPieEntryLabels(){
        for(int i=0;i<alChart.size();i++){
            PieEntryLabels.add(alChart.get(i).name);
        }

    }

    private void getHoaDon(Long from,Long to){
        alHoaDon.clear();

        progressDialog = new ProgressDialog(EmployeeEfficientActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        Query query = mDatabase.child("Hoadon").orderByChild("ngay/timestamp").startAt(from).endAt(to);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Log.d("data2",issue.getValue().toString());
                        HoaDon hd = issue.getValue(HoaDon.class);
                        hd.hoadonId = issue.getKey();

                        alHoaDon.add(hd);
                    }
                    //lấy dữ liệu cho chart
                    alChart = ChartHandler.dataForPieChart(alEmployee,alHoaDon);
                    // hiển thị chart
                    displayPieChart();

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }

                }
                else{
                    Toast.makeText(EmployeeEfficientActivity.this, "Không có dữ liệu!",
                            Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
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

    private void createTimePickerDialog(final EditText et){
        Calendar currentDate = Calendar.getInstance();
        DatePickerDialog date = new DatePickerDialog(EmployeeEfficientActivity.this,
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
}
