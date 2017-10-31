package com.example.dellcorei3.storemanagement.store.manager;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.store.manager.controller.ChartHandler;
import com.example.dellcorei3.storemanagement.store.manager.controller.TimeFormat;
import com.example.dellcorei3.storemanagement.store.manager.listview_adapter.HoaDonAdapter;
import com.example.dellcorei3.storemanagement.store.manager.listview_adapter.ProductAdapter;
import com.example.dellcorei3.storemanagement.store.manager.models.HoaDon;
import com.example.dellcorei3.storemanagement.store.manager.models.HoaDonChiTiet;
import com.example.dellcorei3.storemanagement.store.manager.models.StatisticProduct;
import com.example.dellcorei3.storemanagement.store.manager.models.ThucDon;
import com.github.mikephil.charting.charts.BarChart;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class ProductStatisticActivity extends AppCompatActivity {

    EditText etFrom,etTo;
    RadioGroup rdGroup;
    RadioButton rdMax,rdMin;
    ListView lv;

    private DatabaseReference mDatabase;
    ArrayList<HoaDon> alHoaDon;
    ArrayList<HoaDonChiTiet> alHoaDonChiTiet;
    ArrayList<ThucDon> alThucDon;
    ArrayList<StatisticProduct> alProduct;
    ProductAdapter productAdapter;

    ProgressDialog progressDialog;

    private static final int MAX = 2;
    private static final int MIN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_statistic);

        addControls();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        alHoaDon = new ArrayList<>();
        alHoaDonChiTiet = new ArrayList<>();
        alThucDon = new ArrayList<>();
        alProduct = new ArrayList<>();
        rdMax.setChecked(true);

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
                    // lấy hóa đơn và hiển thị dữ liệu
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
                    // lấy hóa đơn và hiển thị dữ liệu
                    getHoaDon(from,to);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        // sự kiện thay đổi radio button
        rdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(rdMax.isChecked() == true){
                    if(etFrom.getText().toString().equals("") == false && etTo.getText().toString().equals("") == false){
                        Collections.reverse(alProduct);
                        productAdapter.notifyDataSetChanged();
                    }
                }
                else{
                    if(etFrom.getText().toString().equals("") == false && etTo.getText().toString().equals("") == false) {
                        Collections.sort(alProduct, new Comparator<StatisticProduct>() {
                            @Override
                            public int compare(StatisticProduct o1, StatisticProduct o2) {
                                return Integer.valueOf(o1.soluong).compareTo(o2.soluong);
                            }
                        });
                        productAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void addControls(){
        lv = (ListView) findViewById(R.id.lvProduct);
        etFrom = (EditText)findViewById(R.id.etProductFrom);
        etTo = (EditText)findViewById(R.id.etProductTo);

        rdGroup = (RadioGroup)findViewById(R.id.rdGroup1);
        rdMax = (RadioButton) findViewById(R.id.rdMax);
        rdMin = (RadioButton) findViewById(R.id.rdMin);
    }

    private void getHoaDon(Long from,Long to){
        alHoaDon.clear();

        progressDialog = new ProgressDialog(ProductStatisticActivity.this);
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
                        getHoaDonChiTiet(hd.hoadonId);
                    }
                    // lấy menu
                    if(alThucDon.size() == 0) {
                        getMenu();
                    }
                    else{
                        handlerData();
                        productAdapter = new ProductAdapter(ProductStatisticActivity.this,R.layout.listviewe_product,alProduct);
                        lv.setAdapter(productAdapter);
                        productAdapter.notifyDataSetChanged();

                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    }

                }
                else{
                    Toast.makeText(ProductStatisticActivity.this, "Không có dữ liệu!",
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

    private void getHoaDonChiTiet(String hoadonId){
        alHoaDonChiTiet.clear();

        mDatabase.child("Hoadon_chitiet").child(hoadonId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        HoaDonChiTiet hdct = issue.getValue(HoaDonChiTiet.class);
                        hdct.id = issue.getKey();
                        alHoaDonChiTiet.add(hdct);

                        Log.d("hoadonct",hdct.ten+"-"+hdct.gia+" "+issue.getKey());
                    }

                }
                else{

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getMenu(){

        mDatabase.child("thucdon").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        ThucDon td = issue.getValue(ThucDon.class);
                        td.id = issue.getKey();
                        alThucDon.add(td);
                    }
                    Log.d("size",alThucDon.size()+"");
                    // lấy dữ liệu cho listview
                    handlerData();
                    productAdapter = new ProductAdapter(ProductStatisticActivity.this,R.layout.listviewe_product,alProduct);
                    lv.setAdapter(productAdapter);
                    productAdapter.notifyDataSetChanged();

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
                else{
                    Log.d("error","Không thể đọc thucdon");
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

    private void handlerData(){
        for(int i=0;i<alThucDon.size();i++){
            ThucDon td = alThucDon.get(i);
            StatisticProduct product = new StatisticProduct();
            product.ten = td.thucdon_ten;
            for(int j=0;j<alHoaDonChiTiet.size();j++){
                HoaDonChiTiet hd = alHoaDonChiTiet.get(j);
                if(hd.thucdon_id.equals(td.id) == true){
                    product.soluong += 1;
                    product.tongtien += Integer.parseInt(td.thucdon_gia);
                }
            }

            alProduct.add(product);
        }
        // sắp zếp dữ liệu
            Collections.sort(alProduct, new Comparator<StatisticProduct>() {
                @Override
                public int compare(StatisticProduct o1, StatisticProduct o2) {
                    return Integer.valueOf(o1.soluong).compareTo(o2.soluong);
                }
            });

        if(rdMax.isChecked() == true){
            Collections.reverse(alProduct);
        }

    }


    private void createTimePickerDialog(final EditText et){
        Calendar currentDate = Calendar.getInstance();
        DatePickerDialog date = new DatePickerDialog(ProductStatisticActivity.this,
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
