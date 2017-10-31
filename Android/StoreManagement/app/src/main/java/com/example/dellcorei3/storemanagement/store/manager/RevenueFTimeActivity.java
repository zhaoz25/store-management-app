package com.example.dellcorei3.storemanagement.store.manager;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.store.manager.controller.ChartHandler;
import com.example.dellcorei3.storemanagement.store.manager.controller.TimeFormat;
import com.example.dellcorei3.storemanagement.store.manager.listview_adapter.HoaDonAdapter;
import com.example.dellcorei3.storemanagement.store.manager.models.HoaDon;
import com.example.dellcorei3.storemanagement.store.manager.models.LineChartData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class RevenueFTimeActivity extends AppCompatActivity {

    EditText etFrom,etTo;
    private LineChart chart;

    private DatabaseReference mDatabase;
    ArrayList<HoaDon> alHoaDon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revenue_ftime);

        addControls();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        alHoaDon = new ArrayList<>();

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

                    getHoaDon(from,to);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void addControls(){
        etFrom = (EditText)findViewById(R.id.etFTimeFrom);
        etTo = (EditText)findViewById(R.id.etFTimeTo);

        chart = (LineChart) findViewById(R.id.linechart);
    }

    private void getHoaDon(Long from,Long to){
        alHoaDon.clear();

        Query query = mDatabase.child("Hoadon").orderByChild("ngay/timestamp").startAt(from).endAt(to);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        HoaDon hd = issue.getValue(HoaDon.class);
                        hd.hoadonId = issue.getKey();

                        alHoaDon.add(hd);
                    }
                    displayChart();


                }
                else{
                    Toast.makeText(RevenueFTimeActivity.this, "Không có dữ liệu!",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void displayChart() {
        // lấy dữ liệu cho linechart
        ArrayList<LineChartData> alChart = ChartHandler.dataForLineChart(alHoaDon);

        ArrayList<String> xVals = setXAxisValues(alChart);

        ArrayList<Entry> yVals = setYAxisValues(alChart);

        LineDataSet dataSet = new LineDataSet(yVals,"Doanh thu theo giờ");
        LineData data = new LineData(xVals,dataSet);

        int[] colors = {Color.RED};
        dataSet.setColors(colors);
        dataSet.setCircleColor(Color.BLUE);
        dataSet.setValueTextSize(12);
        dataSet.setCircleRadius(3f);
        dataSet.setDrawFilled(true);

        chart.setData(data);
        chart.notifyDataSetChanged();
        chart.setVisibleXRangeMaximum(5);
        chart.animateY(3000);

    }

    // This is used to store x-axis values
    private ArrayList<String> setXAxisValues(ArrayList<LineChartData> alChart){
        ArrayList<String> xVals = new ArrayList<String>();
        for(int i=0;i<alChart.size();i++){
            xVals.add(alChart.get(i).name+"h");
        }

        return xVals;
    }
    // This is used to store Y-axis values
    private ArrayList<Entry> setYAxisValues(ArrayList<LineChartData> alChart){
        ArrayList<Entry> yVals = new ArrayList<Entry>();
        for(int i=0;i<alChart.size();i++){
            yVals.add(new Entry((float)alChart.get(i).value,i));
        }

        return yVals;
    }

    private void createTimePickerDialog(final EditText et){
        Calendar currentDate = Calendar.getInstance();
        DatePickerDialog date = new DatePickerDialog(RevenueFTimeActivity.this,
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
