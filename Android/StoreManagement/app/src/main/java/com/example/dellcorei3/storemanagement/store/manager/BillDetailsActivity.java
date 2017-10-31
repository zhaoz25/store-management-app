package com.example.dellcorei3.storemanagement.store.manager;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.store.manager.controller.TimeFormat;
import com.example.dellcorei3.storemanagement.store.manager.listview_adapter.BillAdapter;
import com.example.dellcorei3.storemanagement.store.manager.listview_adapter.HoaDonAdapter;
import com.example.dellcorei3.storemanagement.store.manager.listview_adapter.ProductAdapter;
import com.example.dellcorei3.storemanagement.store.manager.models.Ban;
import com.example.dellcorei3.storemanagement.store.manager.models.Employee;
import com.example.dellcorei3.storemanagement.store.manager.models.HoaDon;
import com.example.dellcorei3.storemanagement.store.manager.models.HoaDonChiTiet;
import com.example.dellcorei3.storemanagement.store.manager.models.ThucDon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class BillDetailsActivity extends AppCompatActivity {

    ExpandableListView exLv;
    TextView tvBillId,tvBillBan,tvBillNgayLap,tvBillNgayTT,tvBillNV,tvBillTrangThai,tvBillTongTien;

    private DatabaseReference mDatabase;
    HoaDon hd;
    Ban ban;
    Employee employee;
    ArrayList<HoaDonChiTiet> alHDCT;
    ArrayList<String> header;
    ArrayList<ThucDon> alMenu;
    BillAdapter adapter;
    HashMap<String,ArrayList<HoaDonChiTiet>> items;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);

        addControl();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Bundle bundle = getIntent().getBundleExtra("data");
        hd = (HoaDon) bundle.getSerializable("hoadon");

        ban = new Ban();
        employee = new Employee();
        alHDCT = new ArrayList<>();
        header = new ArrayList<>();
        alMenu = new ArrayList<>();
        loadData();
    }

    private void addControl(){
        exLv = (ExpandableListView)findViewById(R.id.lvBillDetails);

        tvBillId = (TextView)findViewById(R.id.tvBillId);
        tvBillBan = (TextView)findViewById(R.id.tvBillBan);
        tvBillNgayLap = (TextView)findViewById(R.id.tvBillNgayLap);
        tvBillNgayTT = (TextView)findViewById(R.id.tvBillNgayTT);
        tvBillNV = (TextView)findViewById(R.id.tvBillNVLap);
        tvBillTrangThai = (TextView)findViewById(R.id.tvBillTrangThai);
        tvBillTongTien = (TextView)findViewById(R.id.tvBillTongTien);
    }

    private void loadData(){
        progressDialog = new ProgressDialog(BillDetailsActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(true);
        progressDialog.show();

        mDatabase.child("ban").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        if(issue.getKey().equals(hd.ban) == true) {
                            ban = issue.getValue(Ban.class);
                        }
                    }
                    getEmployee();

                }
                else{
                    Toast.makeText(BillDetailsActivity.this, "Không Thể đọc dữ liệu bàn",
                            Toast.LENGTH_SHORT).show();

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getEmployee(){
        mDatabase.child("nhanvien").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        if(issue.getKey().equals(hd.nhanvien_id) == true) {
                            employee = issue.getValue(Employee.class);
                        }
                    }
                    getMenu();

                }
                else{
                    Toast.makeText(BillDetailsActivity.this, "Không thể đọc dử liệu nhân viên",
                            Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
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
                        alMenu.add(td);
                    }
                    getHDCT();
                }
                else{
                    Toast.makeText(BillDetailsActivity.this, "Không thể đọc dữ liêu thực đơn",
                            Toast.LENGTH_SHORT).show();
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getHDCT(){
        mDatabase.child("Hoadon_chitiet").child(hd.hoadonId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        HoaDonChiTiet hdct = issue.getValue(HoaDonChiTiet.class);
                        hdct.id = issue.getKey();
                        alHDCT.add(hdct);
                    }
                    //lấy tên món cho hoa don chi tiet
                    for(int i=0;i<alHDCT.size();i++){
                        for(int j=0;j<alMenu.size();j++){
                            String menuId = alMenu.get(j).id;
                            if(alHDCT.get(i).thucdon_id.equals(menuId) == true) {
                                alHDCT.get(i).tenMon = alMenu.get(j).thucdon_ten;
                            }
                        }
                    }
                    createView();
                }
                else{
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void createView(){
        //expandable listview
        items = new HashMap<>();
        header.add("Chi tiết món");
        items.put(header.get(0),alHDCT);
        adapter = new BillAdapter(this,header,items);
        exLv.setAdapter(adapter);

        // textview
        tvBillId.setText(hd.hoadonId);
        tvBillBan.setText(ban.tenban);
        tvBillNgayLap.setText(TimeFormat.convertTimeStampToDate(hd.ngay));
        tvBillNgayTT.setText("");
        tvBillNV.setText(employee.getName());
        if(hd.trangthai.equals("chuathanhtoan") == true) {
            tvBillTrangThai.setText("Chưa thanh toán");
        }
        else if(hd.trangthai.equals("chothanhtoan") == true){
            tvBillTrangThai.setText("Chờ thanh toán");
        }
        else{
            tvBillTrangThai.setText("Đã thanh toán");
        }

        tvBillTongTien.setText(hd.tongTien);
        // tắt dialog
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }
}
