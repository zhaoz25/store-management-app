package com.example.dellcorei3.storemanagement.employee;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.employee.CustomListview.CT_hoadonAdapter;
import com.example.dellcorei3.storemanagement.employee.JavaClass.Ban;
import com.example.dellcorei3.storemanagement.employee.JavaClass.Hoadon;
import com.example.dellcorei3.storemanagement.employee.JavaClass.Nhan_vien;
import com.example.dellcorei3.storemanagement.employee.JavaClass.Show_hoadon;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Main_lauout_Bill extends Activity {
    Dialog dialog;
    ListView lv;
    Button bt_taohoadon,bt_huy,bt_them;

    CT_hoadonAdapter adapter;
    DatabaseReference mdata;

    Spinner spinnerID;
    ArrayList<Show_hoadon> arrayshowhoadon;
    ArrayList<Nhan_vien> arrayListNV;
    Hoadon hoadon;
    Show_hoadon show_hoadon;
    Nhan_vien nhan_vien;

    TextView  txt_ngay,txt_gio;
    String setkey,laykey,keyresult;
    Bundle bundle;
    boolean isswap=false;

    private DatePickerDialog.OnDateSetListener dateSetListener;

    ArrayAdapter arrayAdapterSpinner;

    ArrayList<Ban> arrayBan,arrayshowban,arrayspinnerban;
    Ban banDaChon;
    String keyban;
    boolean ischoose1 = false,ischoose2 = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_lauout__bill);

        thamchieu();

        mdata = FirebaseDatabase.getInstance().getReference();




//        adapter = new CT_hoadonAdapter(Main_lauout_Bill.this,R.layout.custom_listview,arrayshowhoadon);
//        lv.setAdapter(adapter);


        firebasedata();

        dislayhoadon();


        ControButton();

        clicklistview();
        longclicklistview();


    }


    // Contro button
    private void ControButton(){
        if (isswap == false) {
            bt_taohoadon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog = new Dialog(Main_lauout_Bill.this,R.style.Dialog);
                    dialog.setTitle("Tạo Hóa Đơn");
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.customdialog);

                    Button bt_huydialog = (Button) dialog.findViewById(R.id.bt_id_dialog_huy);
                    Button bt_taodialog = (Button) dialog.findViewById(R.id.bt_id_dialog_tao);


                    //////////////////////////////-------------------------
                    spinnerID = (Spinner) dialog.findViewById(R.id.id_spinner);
                    Log.d("array",arrayspinnerban.toString());
                    arrayAdapterSpinner = new ArrayAdapter(Main_lauout_Bill.this, android.R.layout.simple_spinner_item, arrayspinnerban);
                    arrayAdapterSpinner.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                    spinnerID.setAdapter(arrayAdapterSpinner);
                    arrayAdapterSpinner.notifyDataSetChanged();
                    ////////////--------------------


                    banDaChon = new Ban();

                    spinnerID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            banDaChon = arrayspinnerban.get(position);
                            Log.d("position",arrayspinnerban.get(position).toString());


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

//                datepicket();


                    //tao hoa don
                    bt_taodialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        if (arrayspinnerban.size()==0){
                            Toast.makeText(Main_lauout_Bill.this,"Hiện không còn bàn trống để tạo hóa đơn",Toast.LENGTH_LONG).show();
                        }else {
                            String nhanvien_id = bundle.getString("nhanvien_id");

                            hoadon = new Hoadon(nhanvien_id, banDaChon.key);
                            setkey = mdata.push().getKey();
                            Map<String, Object> checkoutData = new HashMap<>();
                            checkoutData.put("timestamp", ServerValue.TIMESTAMP);
                            keyban = banDaChon.key.toString();
                            Log.d("keyban", keyban);
                            mdata.child("ban").child(keyban).child("choose").setValue("1");
                            mdata.child("Hoadon").child(setkey).setValue(hoadon);
                            mdata.child("Hoadon").child(setkey).child("ngay").setValue(checkoutData);


                            Intent layoutBill_2 = new Intent(Main_lauout_Bill.this, Main_Layout_Bill_2.class);
                            layoutBill_2.putExtra("Ban", banDaChon.key.toString());

                            layoutBill_2.putExtra("Vitri", banDaChon.vitri.toString());
                            layoutBill_2.putExtra("Chuoikeyid", nhanvien_id);
                            layoutBill_2.putExtra("Key", setkey.toString());
                            startActivityForResult(layoutBill_2, 1);
                            dialog.dismiss();

//                        dislayhoadon();
//                        Toast.makeText(Main_lauout_Bill.this,selectban.getText(),Toast.LENGTH_LONG).show();

//                        dialog.cancel();
                        }
                        }
                    });

                    bt_huydialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                    dialog.show();

                }
            });
        }
    }



    // tham chieu du lieu vao oncreate
    private void thamchieu(){
        lv =(ListView)findViewById(R.id.id_lv);
        bt_taohoadon = (Button)findViewById(R.id.bt_id_taohd);
        bt_huy = (Button)findViewById(R.id.bt_id_tab2_huy);
        bt_them = (Button)findViewById(R.id.bt_id_tab2_them);


//        data = new ArrayList<CT_hoadon>();
        arrayBan = new ArrayList();
        arrayshowban = new ArrayList<Ban>();
        arrayspinnerban = new ArrayList<>();
        arrayshowhoadon = new ArrayList<Show_hoadon>();
        arrayListNV = new ArrayList<Nhan_vien>();
        show_hoadon = new Show_hoadon();
        bundle = getIntent().getExtras();

    }



    //doc firebase
    private void firebasedata() {

        if (ischoose2 == true && ischoose1 == false) {
            mdata.child("ban").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    banDaChon = new Ban();
                    banDaChon = dataSnapshot.getValue(Ban.class);
                    banDaChon.key = dataSnapshot.getKey();
                    Log.d("choose", banDaChon.choose.toString());
                    arrayBan.add(banDaChon);
                    if (banDaChon.choose.equals("")) {
                        arrayspinnerban.add(banDaChon);
                    }

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    Ban b = dataSnapshot.getValue(Ban.class);
                    b.key = dataSnapshot.getKey();

                        for (int i = 0; i < arrayBan.size(); i++) {
                            if(arrayBan.get(i).key.equals(b.key) == true){
                                arrayBan.set(i,b);


                            }
                        }

                        arrayspinnerban.clear();
                    for(int i=0;i<arrayBan.size();i++){
                        if(arrayBan.get(i).choose.equals("")){
                            arrayspinnerban.add(arrayBan.get(i));
                        }
                    }
                    if(arrayAdapterSpinner != null) {
                        arrayAdapterSpinner.notifyDataSetChanged();
                    }



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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data !=null){
            laykey = data.getStringExtra("Keyresult");
//            dislayhoadon();
        }
    }

    private void dislayhoadon(){

        if (ischoose1 == false && ischoose2 == true) {
            //hien thi danh sach cac ban da chon mon cua nhan vien phuc vu
            mdata.child("ban").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            banDaChon = dataSnapshot1.getValue(Ban.class);
                            banDaChon.key = dataSnapshot1.getKey();
                            if (banDaChon.choose.toString() != "") {
                                arrayshowban.add(banDaChon);
                            }
                        }

                        mdata.child("nhanvien").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                                    nhan_vien = dataSnapshot2.getValue(Nhan_vien.class);
                                    nhan_vien.key = dataSnapshot2.getKey();
                                    arrayListNV.add(nhan_vien);
                                }
                                getshowhoadon();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else {

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    public void getshowhoadon(){
        mdata.child("Hoadon").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                    if(dataSnapshot.getKey().equals(laykey)) {
                show_hoadon = dataSnapshot.getValue(Show_hoadon.class);
                show_hoadon.key = dataSnapshot.getKey();

                        for (int i = 0; i < arrayshowban.size(); i++) {
                            if (show_hoadon.ban.equals(arrayshowban.get(i).key)) {
                                show_hoadon.ten = arrayshowban.get(i).tenban;
                            }
                        }
                        for (int j = 0; j < arrayListNV.size(); j++) {
                            if (show_hoadon.nhanvien_id.equals(arrayListNV.get(j).key)) {
                                show_hoadon.tennv = arrayListNV.get(j).lastName;
                            }
                        }
                    if (!show_hoadon.trangthai.equals("dathanhtoan")){
                    createdataDislayhoadon();
                    }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Show_hoadon hd = dataSnapshot.getValue(Show_hoadon.class);
                hd.key = dataSnapshot.getKey();

                        for (int i = 0; i < arrayshowhoadon.size(); i++) {

                            if (arrayshowhoadon.get(i).key.equals(dataSnapshot.getKey())) {
                                if (!hd.trangthai.equals("dathanhtoan")) {

                                    for (int j = 0; j < arrayBan.size(); j++) {
                                        if (hd.ban.equals(arrayBan.get(j).key)) {
                                            hd.ten = arrayBan.get(j).tenban;
                                        }
                                    }
                                    for (int j = 0; j < arrayListNV.size(); j++) {
                                        if (hd.nhanvien_id.equals(arrayListNV.get(j).key)) {
                                            hd.tennv = arrayListNV.get(j).lastName;
                                        }
                                    }
                                    arrayshowhoadon.set(i, hd);
                                    adapter.notifyDataSetChanged();
                                }
                                else{
                                    arrayshowhoadon.remove(i);
                                    adapter.notifyDataSetChanged();
                                }
                            }

                        }

            }
//
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

    private void clicklistview (){
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String positionkey = arrayshowhoadon.get(position).key;

                Intent i = new Intent(Main_lauout_Bill.this,Main_Layout_Bill_2.class);
                i.putExtra("positionkey",positionkey);
                String isclick = "a";
                i.putExtra("click",isclick);
                startActivity(i);
            }
        });
    }

    private void longclicklistview(){
        isswap =true;
        if (isswap == true) {
            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    final String positionkeyban = arrayshowhoadon.get(position).ban;
                    final String positionkeyhd = arrayshowhoadon.get(position).key;

                    dialog = new Dialog(Main_lauout_Bill.this,R.style.Dialog);
                    dialog.setTitle("Đổi Vị Trí");
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.customdialog);

                    Button bt_huydialog = (Button) dialog.findViewById(R.id.bt_id_dialog_huy);
                    Button bt_taodialog = (Button) dialog.findViewById(R.id.bt_id_dialog_tao);
                    bt_taodialog.setText("Đổi");
                    //////////////////////////////-------------------------
                    spinnerID = (Spinner) dialog.findViewById(R.id.id_spinner);
                    arrayAdapterSpinner = new ArrayAdapter(Main_lauout_Bill.this, android.R.layout.simple_spinner_item, arrayspinnerban);
                    arrayAdapterSpinner.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
                    spinnerID.setAdapter(arrayAdapterSpinner);
                    ////////////--------------------


                    banDaChon = new Ban();

                    spinnerID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            banDaChon = arrayspinnerban.get(position);


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    //tao hoa don
                    bt_taodialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (arrayspinnerban.size()==0){
                                Toast.makeText(Main_lauout_Bill.this,"Vui lòng chọn bàn muốn đổi",Toast.LENGTH_LONG).show();
                            }else
                            {
                                keyban = banDaChon.key.toString();
                                Log.d("keyban", keyban);
                                mdata.child("ban").child(keyban).child("choose").setValue("1");
                                mdata.child("ban").child(positionkeyban).child("choose").removeValue();
                                mdata.child("Hoadon").child(positionkeyhd).child("ban_cu").setValue(positionkeyban);
                                mdata.child("Hoadon").child(positionkeyhd).child("ban").setValue(keyban);


                                dialog.dismiss();

                            }
                        }
                    });

                    bt_huydialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                        }
                    });

                    dialog.show();
                    return true;
                }
            });
        }
    }


    //show data hoadon
    private void createdataDislayhoadon(){

        arrayshowhoadon.add(new Show_hoadon(show_hoadon.ten,show_hoadon.tennv,show_hoadon.trangthai,show_hoadon.key,show_hoadon.ban));
        adapter = new CT_hoadonAdapter(Main_lauout_Bill.this,R.layout.custom_listview,arrayshowhoadon);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }




}
