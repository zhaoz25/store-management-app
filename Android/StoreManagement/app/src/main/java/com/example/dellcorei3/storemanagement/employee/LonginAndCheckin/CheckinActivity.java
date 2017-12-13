package com.example.dellcorei3.storemanagement.employee.LonginAndCheckin;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.employee.Main_lauout_Bill;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CheckinActivity extends AppCompatActivity {
    TextView txt_show_nv;
    DatabaseReference mdata;
    Nhanvien_checkin nhanvien_checkin;
    String getkey;
    Bundle bundle;
    String nhanvien_id;
    String getemail;
    String ipAddress;
    boolean isNam=false;
    String nv_phucvu = "Phục Vụ",nv_thungan = "Thu Ngân";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin);
        mdata = FirebaseDatabase.getInstance().getReference();
        thamchieu();
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.dimAmount=0.0f;
        this.getWindow().setAttributes(lp);
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mymenu_checkin,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_checkin:
                checkin();
                getCurrentDateTime();
                NetwordDetect();
                break;
            case R.id.action_order:
                createorder();
                break;
            case R.id.action_logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void thamchieu(){
        txt_show_nv = (TextView)findViewById(R.id.txt_id_show_nv);


        checkinEmail();



    }


    //check email
    public void checkinEmail(){
        mdata.child("nhanvien").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                bundle = getIntent().getExtras();
                getemail = bundle.getString("Email");
                nhanvien_checkin = new Nhanvien_checkin();
                nhanvien_checkin = dataSnapshot.getValue(Nhanvien_checkin.class);

                if (getemail.equals( nhanvien_checkin.email)){

                    if (nhanvien_checkin.position.equals("phucvu")){
                        nhanvien_checkin.position = nv_phucvu;

                    }else {
                        nhanvien_checkin.position = nv_thungan;

                    }

                    if (nhanvien_checkin.gender == 0 ){
                        isNam=true;
                        String Genger = "Nam";
                        txt_show_nv.setText(nhanvien_checkin.firstName+" "+nhanvien_checkin.lastName
                                +"\n"+nhanvien_checkin.address+"\n"+Genger+"\n"+nhanvien_checkin.fromDate+"\n"+nhanvien_checkin.position
                        );
                    }else {
                        isNam=false;
                        String Genger = "Nữ";
                        txt_show_nv.setText(nhanvien_checkin.firstName+" "+nhanvien_checkin.lastName
                                +"\n"+nhanvien_checkin.address+"\n"+Genger+"\n"+nhanvien_checkin.fromDate+"\n"+nhanvien_checkin.position
                        );
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


    //button checkin
    private void checkin(){
        mdata.child("nhanvien").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                bundle = getIntent().getExtras();
                getemail = bundle.getString("Email");
                nhanvien_checkin = new Nhanvien_checkin();
                nhanvien_checkin = dataSnapshot.getValue(Nhanvien_checkin.class);
                nhanvien_checkin.key = dataSnapshot.getKey();

                if (getemail.equals( nhanvien_checkin.email)) {
                    nhanvien_id=nhanvien_checkin.key;
                    Map<String,Object> checkoutData=new HashMap<>();
                    checkoutData.put("timestamp", ServerValue.TIMESTAMP);

                    String  setkey= mdata.push().getKey();
                    getkey = setkey;
                    mdata.child("CheckIn").child(setkey).setValue(new Nhanvien_checkin(ipAddress,nhanvien_id.toString()));
                    mdata.child("CheckIn").child(setkey).child("gio").setValue(checkoutData);


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

    //order
    private void createorder(){
        mdata.child("nhanvien").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                bundle = getIntent().getExtras();
                getemail = bundle.getString("Email");
                nhanvien_checkin = new Nhanvien_checkin();
                nhanvien_checkin = dataSnapshot.getValue(Nhanvien_checkin.class);
                nhanvien_checkin.key = dataSnapshot.getKey();

                if (getemail.equals( nhanvien_checkin.email)){
                    String tennv = nhanvien_checkin.lastName;
                    Intent i = new Intent(CheckinActivity.this, Main_lauout_Bill.class);
                    i.putExtra("nhanvien_id",nhanvien_checkin.key);
                    i.putExtra("lastname",tennv);
                    i.putExtra("email",nhanvien_checkin.email);
                    startActivity(i);

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

   private void NetwordDetect(){
       WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
       WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
       int ip = wifiInfo.getIpAddress();
       ipAddress = Formatter.formatIpAddress(ip);
       Log.d("IPaddress",ipAddress);
   }

   private void getCurrentDateTime(){
       DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"+" - HH:mm");
       Date date = new Date();
       Toast.makeText(CheckinActivity.this,"Điểm danh thành công!\n"+dateFormat.format(date),Toast.LENGTH_LONG).show();

   }


}
