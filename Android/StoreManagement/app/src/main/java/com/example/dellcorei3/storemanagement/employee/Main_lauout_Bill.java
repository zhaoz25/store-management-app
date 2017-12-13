package com.example.dellcorei3.storemanagement.employee;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dellcorei3.storemanagement.MainActivity;
import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.employee.CustomListview.CT_hoadonAdapter;
import com.example.dellcorei3.storemanagement.employee.JavaClass.Ban;
import com.example.dellcorei3.storemanagement.employee.JavaClass.Hoadon;
import com.example.dellcorei3.storemanagement.employee.JavaClass.Nhan_vien;
import com.example.dellcorei3.storemanagement.employee.JavaClass.Show_hoadon;
import com.example.dellcorei3.storemanagement.employee.LonginAndCheckin.Nhanvien_checkin;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    Switch switchDisplay;
    TextView tvStatusConnection;

    ArrayList<Show_hoadon> arrayshowhoadon,arrHDEmp;
    ArrayList<Nhan_vien> arrayListNV;
    Hoadon hoadon;
    Show_hoadon show_hoadon;
    Nhan_vien nhan_vien;

    String setkey,laykey;
    String employeeID;
    int notificationId=0;
    Bundle bundle;
    boolean isswap=false;

    private DatePickerDialog.OnDateSetListener dateSetListener;

    ArrayAdapter arrayAdapterSpinner;

    ArrayList<Ban> arrayBan,arrayshowban,arrayspinnerban;
    Ban banDaChon;
    String keyban;
    boolean ischoose1 = false,ischoose2 = true;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_lauout__bill);

        thamchieu();
        switchDisplay.setChecked(true);

        mdata = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        // kiểm tra trạng thái kết nối internet
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if(connected) {
                    Toast.makeText(Main_lauout_Bill.this, "Đã kết nối",
                            Toast.LENGTH_SHORT).show();
                    tvStatusConnection.setVisibility(View.INVISIBLE);
                }else{
                    tvStatusConnection.setVisibility(View.VISIBLE);
                    Toast.makeText(Main_lauout_Bill.this, "Mất kết nối",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Listener was cancelled");
            }
        });


//        adapter = new CT_hoadonAdapter(Main_lauout_Bill.this,R.layout.custom_listview,arrayshowhoadon);
//        lv.setAdapter(adapter);


        firebasedata();

        dislayhoadon();


        ControButton();

        clicklistview();
        longclicklistview();

        // sự kiện thay đổi switch
        switchDisplay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // nếu chưa đọc xong dữ liệu
                if(arrHDEmp.size() == 0 || arrayshowhoadon.size() == 0){
                    return;
                }

                if(isChecked==true){
                    showData(arrayshowhoadon);
                }
                else{
                    showData(arrHDEmp);
                }
            }
        });
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

                            hoadon = new Hoadon(nhanvien_id, banDaChon.key,"");
                            setkey = mdata.push().getKey();
                            Map<String, Object> checkoutData = new HashMap<>();
                            checkoutData.put("timestamp", ServerValue.TIMESTAMP);
                            keyban = banDaChon.key.toString();
                            Log.d("keyban", keyban);

                            mdata.child("ban").child(keyban).child("choose").setValue("1");
                            hoadon.ngay = checkoutData;
                            mdata.child("Hoadon").child(setkey).setValue(hoadon);
                           // mdata.child("Hoadon").child(setkey).child("ngay").setValue(checkoutData);


                            Intent layoutBill_2 = new Intent(Main_lauout_Bill.this, Main_Layout_Bill_2.class);
                            layoutBill_2.putExtra("Ban", banDaChon.key.toString());

                            layoutBill_2.putExtra("Vitri", banDaChon.vitri.toString());
                            layoutBill_2.putExtra("Chuoikeyid", nhanvien_id);
                            Log.d("nv1",nhanvien_id);
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
        switchDisplay = (Switch)findViewById(R.id.switch1);
        tvStatusConnection = (TextView)findViewById(R.id.tvStatusConnection);
        tvStatusConnection.setVisibility(View.INVISIBLE);

//        data = new ArrayList<CT_hoadon>();
        arrayBan = new ArrayList();
        arrayshowban = new ArrayList<Ban>();
        arrayspinnerban = new ArrayList<>();
        arrayshowhoadon = new ArrayList<Show_hoadon>();
        arrHDEmp = new ArrayList<Show_hoadon>();
        arrayListNV = new ArrayList<Nhan_vien>();
        show_hoadon = new Show_hoadon();
        bundle = getIntent().getExtras();

    }

    private void pushNotifications(String ban,String nv){
        notificationId++;
        Intent intent = new Intent();
        PendingIntent pIntent = PendingIntent.getActivity(Main_lauout_Bill.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder b = new NotificationCompat.Builder(Main_lauout_Bill.this);
        b.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.icon)
                .setTicker("notification")
                .setContentTitle(ban)
                .setContentText("Đã chuẩn bị xong!")
                .setDefaults(Notification.DEFAULT_LIGHTS| Notification.DEFAULT_SOUND)
                .setContentIntent(pIntent)
                .setContentInfo(nv);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, b.build());
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

        Bundle b = getIntent().getExtras();
        final String myEmail = b.get("email").toString();
        // kiểm tra tài khoản nhân vien
        mdata.child("nhanvien").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Nhanvien_checkin nv = dataSnapshot.getValue(Nhanvien_checkin.class);

                if(nv.email.equals(myEmail)==true){
                    employeeID=dataSnapshot.getKey();
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Nhanvien_checkin nv = dataSnapshot.getValue(Nhanvien_checkin.class);

                if(nv.email.equals(myEmail)==true){
                    if(nv.state == 0){
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(Main_lauout_Bill.this,"Tài khoản của bạn đã bị khóa",Toast.LENGTH_LONG).show();
                    }
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
                if (!show_hoadon.trangthai.equals("dathanhtoan")) {
                    createdataDislayhoadon(show_hoadon.nhanvien_id);
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Show_hoadon hd = dataSnapshot.getValue(Show_hoadon.class);
                hd.key = dataSnapshot.getKey();
                // mảng chứa tất cả hóa đơn
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
                        } else {
                            arrayshowhoadon.remove(i);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                // mảng chứa hóa đơn của user
                for (int i = 0; i < arrHDEmp.size(); i++) {
                    if (arrHDEmp.get(i).key.equals(dataSnapshot.getKey())) {
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
                            // push notification khi hóa đơn đã chuẩn bị xong
                            if(hd.trangthai.equals("phucvu")){
                                pushNotifications(hd.ten,hd.tennv);
                            }
                            arrHDEmp.set(i, hd);
                            adapter.notifyDataSetChanged();
                        } else {
                            arrHDEmp.remove(i);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
//
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Show_hoadon hd = dataSnapshot.getValue(Show_hoadon.class);
                hd.key = dataSnapshot.getKey();
                // mảng chứa tất cả hóa đơn
                for (int i = 0; i < arrayshowhoadon.size(); i++) {
                    if (arrayshowhoadon.get(i).key.equals(dataSnapshot.getKey())) {
                        arrayshowhoadon.remove(i);
                        adapter.notifyDataSetChanged();
                    }
                }
                // mảng chứa hóa đơn của user
                for (int i = 0; i < arrHDEmp.size(); i++) {
                    if (arrHDEmp.get(i).key.equals(dataSnapshot.getKey())) {
                        arrHDEmp.remove(i);
                        adapter.notifyDataSetChanged();
                    }
                }
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
                String positionkey = "";
                if(switchDisplay.isChecked() == true) {
                    positionkey = arrayshowhoadon.get(position).key;
                }else{
                    positionkey = arrHDEmp.get(position).key;
                }

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
                    final String positionkeyban;
                    final String positionkeyhd;

                    if(switchDisplay.isChecked() == true){
                        positionkeyban = arrayshowhoadon.get(position).ban;
                        positionkeyhd = arrayshowhoadon.get(position).key;
                    }else{
                        positionkeyban = arrHDEmp.get(position).ban;
                        positionkeyhd = arrHDEmp.get(position).key;

                    }


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
                                String innerKeyBan=positionkeyban;
                                String innerKeyHD=positionkeyhd;

                                keyban = banDaChon.key.toString();
                                mdata.child("ban").child(keyban).child("choose").setValue("1");
                                mdata.child("ban").child(innerKeyBan).child("choose").removeValue();
                                mdata.child("Hoadon").child(innerKeyHD).child("ban_cu").setValue(positionkeyban);
                                mdata.child("Hoadon").child(innerKeyHD).child("ban").setValue(keyban);


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

    private void showData(ArrayList<Show_hoadon> al){
        adapter = new CT_hoadonAdapter(Main_lauout_Bill.this, R.layout.custom_listview, al);
        lv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //show data hoadon
    private void createdataDislayhoadon(String nhanvienID){

        if(nhanvienID.equals(employeeID)){
            arrHDEmp.add(new Show_hoadon(show_hoadon.ten,show_hoadon.tennv,show_hoadon.trangthai,show_hoadon.key,show_hoadon.ban));
        }
        arrayshowhoadon.add(new Show_hoadon(show_hoadon.ten,show_hoadon.tennv,show_hoadon.trangthai,show_hoadon.key,show_hoadon.ban));
        // nếu switch chọn hiển thị tất cả
        if(switchDisplay.isChecked() == true) {
            showData(arrayshowhoadon);
        }
        else{
            showData(arrHDEmp);
        }

    }




}
