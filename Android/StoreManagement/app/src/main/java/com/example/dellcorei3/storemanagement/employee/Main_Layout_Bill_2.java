package com.example.dellcorei3.storemanagement.employee;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dellcorei3.storemanagement.MainActivity;
import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.employee.CustomListview.CT_Hoadon_chitiet_layout_2;
import com.example.dellcorei3.storemanagement.employee.CustomListview.CT_hoadonAdapter_LayoutBill_2;
import com.example.dellcorei3.storemanagement.employee.CustomListview.CT_hoadon_chitietAdapter;
import com.example.dellcorei3.storemanagement.employee.CustomListview.CT_hoadon_layout_2;
import com.example.dellcorei3.storemanagement.employee.Firebase.Menu;
import com.example.dellcorei3.storemanagement.employee.Firebase.Nhom_thucdon;
import com.example.dellcorei3.storemanagement.employee.JavaClass.Hoadon;
import com.example.dellcorei3.storemanagement.employee.JavaClass.Hoadon_chitiet;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;

//import com.example.dellcorei3.storemanagement.employee.JavaClass.test;

public class Main_Layout_Bill_2 extends Activity {
    EditText txt_soluong;
    TextView txt_tong;
    String getkey,getkeyban,keyshowhoadon;
    CT_hoadonAdapter_LayoutBill_2 ct_hoadonAdapter_layoutBill_2;
    CT_hoadon_chitietAdapter ct_hoadon_chitietAdapter;

    ArrayList<CT_hoadon_layout_2> data;
    ArrayList<CT_Hoadon_chitiet_layout_2> data_chitiet;
    ArrayList<Nhom_thucdon> alNhom;

    Button bt_tab2_huy,bt_tab2_them;
    Menu menu;
    Hoadon_chitiet hoadon_chitiet;
    DatabaseReference mdata;
    ListView lv_tab1,lv_tab2;
    int soluong = 1;
    String xoa,tongtien;
    String  tenmonan;
    boolean isRemove=true;

    Bundle bundle;
    TabHost tab;
    TabHost.TabSpec spec1,spec2;
    long tong,tongconlai,tongsl,tong_ct;
    AlertDialog.Builder alertDialogBuilder;

    SearchView sv;
    String tab1 = "Thực Đơn",tab2="Món Ăn Đã Chọn",Namehoadonchitiet="Hoadon_chitiet";
    DecimalFormat decimalFormat;
    //
    long STRtong_ct;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main__layout__bill_2);
        thamchieu();


        mdata = FirebaseDatabase.getInstance().getReference();

        firebasedata();




        //////--------------------------
        decimalFormat = new DecimalFormat("###,###,###");
        //////------------------------
//        setadapter_tab1();
        setadapter_tab2();
        clicklistview();
        longclicklistview();
        searchname();
        getStateHD();
//        mdata.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.d("done","11");
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


    }
    //setadapter tab 1
    private void setadapter_tab1(){
        ct_hoadonAdapter_layoutBill_2 = new CT_hoadonAdapter_LayoutBill_2(Main_Layout_Bill_2.this
                ,R.layout.custom_listview_layout_2,data
        );
        lv_tab1.setAdapter(ct_hoadonAdapter_layoutBill_2);

    }
    //setadapter tab 2
    private void setadapter_tab2(){
        ct_hoadon_chitietAdapter = new CT_hoadon_chitietAdapter(Main_Layout_Bill_2.this,
                R.layout.custom_hodon_chitiet_listview_layout_2,data_chitiet);
        lv_tab2.setAdapter(ct_hoadon_chitietAdapter);

    }

    // tham chieu cac method vao oncreate
    private void thamchieu(){
        txt_tong = (TextView)findViewById(R.id.txt_id_tong);
        bt_tab2_huy = (Button)findViewById(R.id.bt_id_tab2_huy);
        bt_tab2_them = (Button)findViewById(R.id.bt_id_tab2_them);
        tab = (TabHost)findViewById(R.id.tabhost);
        lv_tab1 = (ListView)findViewById(R.id.id_listview_tab_1);
        lv_tab2 = (ListView)findViewById(R.id.id_listview_tab_2);

        tab.setup();

        bundle = getIntent().getExtras();


//         Thuc don mon an Tab1
        spec1 = tab.newTabSpec(tab1);
        spec1.setIndicator(tab1);
        spec1.setContent(R.id.id_layout1);
        tab.addTab(spec1);

//         món ăn đã chọn Tab2
        spec2 = tab.newTabSpec(tab2);
        spec2.setIndicator(tab2);
        spec2.setContent(R.id.id_layout2);
        tab.addTab(spec2);
        tab.setCurrentTab(0);


        data = new ArrayList<CT_hoadon_layout_2>();
        data_chitiet = new ArrayList<CT_Hoadon_chitiet_layout_2>();

        menu=new Menu();
        hoadon_chitiet = new Hoadon_chitiet();


        sv = (SearchView)findViewById(R.id.id_timkiem);

        contronbutton();

    }

    //xu kien cac button
    private void contronbutton(){
        //huy tat ca cac mon an da chon o thuc don
        bt_tab2_huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogBuilder = new AlertDialog.Builder(Main_Layout_Bill_2.this);
                alertDialogBuilder.setMessage("Bạn có muốn xóa tất cả món đã chọn");
                alertDialogBuilder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getkey = bundle.getString("Key");
                        getkeyban = bundle.getString("Ban");
                        keyshowhoadon = bundle.getString("positionkey");
                        if (keyshowhoadon == null) {
                            mdata.child(Namehoadonchitiet).child(getkey).removeValue();
                            mdata.child("Hoadon").child(getkey).removeValue();
                            mdata.child("ban").child(getkeyban).child("choose").removeValue();
                        }else {
                            mdata.child(Namehoadonchitiet).child(keyshowhoadon).removeValue();
                            mdata.child("Hoadon").child(keyshowhoadon).removeValue();
                        }
                        dialog.dismiss();
                        finish();

                    }
                }).setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog= alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        //them cac mon an da chon o thuc don vao hoa don chinh


        bt_tab2_them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                alertDialogBuilder = new AlertDialog.Builder(Main_Layout_Bill_2.this);
                if(keyshowhoadon==null){
                    alertDialogBuilder.setMessage("Thêm hoá đơn");
                }else {
                    alertDialogBuilder.setMessage("Cập nhật hoá đơn");
                }

                alertDialogBuilder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        keyshowhoadon = bundle.getString("positionkey");
                        getkey = bundle.getString("Key");
                        if (keyshowhoadon == null) {
//                        String keyResult = getkey;
                            if (data_chitiet.size() != 0) {
                                mdata.child("Hoadon").child(getkey).child("trangthai").setValue("chuaphucvu");
                                mdata.child("Hoadon").child(getkey).child("tongTien").setValue(tongtien);
                                mdata.child("Hoadon").child(getkey).child("soluong").setValue(tongsl);
                                mdata.child("Hoadon").child(getkey).child("ban_cu").setValue("");
                                mdata.child("Hoadon").child(getkey).child("gio_thanhtoan").setValue("");
                                mdata.child("Hoadon").child(getkey).child("khuyenmai_id").setValue("");

                                Toast.makeText(Main_Layout_Bill_2.this, "Thêm hóa đơn thành công", Toast.LENGTH_SHORT).show();
//                            Intent data = new Intent();
//                            data.putExtra("Keyresult",keyResult);
//                            setResult(Main_lauout_Bill.RESULT_OK,data);
                                finish();
                            } else {
                                Toast.makeText(Main_Layout_Bill_2.this, "Vui lòng chọn một món ăn", Toast.LENGTH_LONG).show();
                            }
                        }else {
                            if (data_chitiet.size() != 0) {
                                String isclick = bundle.getString("click");
                                String a = "a";
                                if (isclick.equals(a)) {
                                    mdata.child("Hoadon").child(keyshowhoadon).child("trangthai").setValue("chothanhtoan");
                                    mdata.child("Hoadon").child(keyshowhoadon).child("tongTien").setValue(tongtien);
                                    mdata.child("Hoadon").child(keyshowhoadon).child("soluong").setValue(tongsl);
                                    mdata.child("Hoadon").child(keyshowhoadon).child("gio_thanhtoan").setValue("");
                                    mdata.child("Hoadon").child(keyshowhoadon).child("khuyenmai_id").setValue("");
                                    Toast.makeText(Main_Layout_Bill_2.this, "Cap nhat thành công", Toast.LENGTH_SHORT).show();
                                }



//                            Intent data = new Intent();
//                            data.putExtra("Keyresult",keyResult);
//                            setResult(Main_lauout_Bill.RESULT_OK,data);
                                finish();
                            } else {
                                Toast.makeText(Main_Layout_Bill_2.this, "Vui lòng chọn một món ăn", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                }).setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog= alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    private void getStateHD(){
        String key = bundle.getString("positionkey");
        if(key == null){
            return;
        }

        mdata.child("Hoadon").child(key).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals("trangthai")) {
                    if (dataSnapshot.getValue().equals("chuaphucvu") == true || dataSnapshot.getValue().equals("") == true) {
                        isRemove = true;
                    } else {
                        isRemove = false;
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals("trangthai")) {
                    if (dataSnapshot.getValue().equals("chuaphucvu") == true || dataSnapshot.getValue().equals("") == true) {
                        isRemove = true;
                    } else {
                        isRemove = false;
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



    //doc du lieu tu firebase
    private void firebasedata() {
        alNhom = new ArrayList<>();
        mdata.child("nhom_thucdon").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot issue : dataSnapshot.getChildren()){
                        Nhom_thucdon n = issue.getValue(Nhom_thucdon.class);
                        n.nhomkey = issue.getKey();
                        alNhom.add(n);

                    }
                    getHD();

                }else{

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        // Hoadon chitiet
        keyshowhoadon = bundle.getString("positionkey");
        getkey = bundle.getString("Key");
        if (keyshowhoadon == null ) {
            mdata.child(Namehoadonchitiet).child(getkey).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    hoadon_chitiet = dataSnapshot.getValue(Hoadon_chitiet.class);
                    hoadon_chitiet.key = dataSnapshot.getKey();

                    int soluong = Integer.parseInt(hoadon_chitiet.soluong.toString());

                    String giatien = hoadon_chitiet.gia.toString();
                    tong += soluong * Float.parseFloat(giatien);
                    tongsl += soluong;

                    tongtien = String.valueOf(tong);
                    txt_tong.setText(decimalFormat.format(tong) + " VND");
                    tenmonan = hoadon_chitiet.ten.toString();

                    createdata_Tab2();
                    //xoa 1 mon khi long click


                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                    for (int i = 0; i < data_chitiet.size(); i++) {
                        if (data_chitiet.get(i).id.equals(dataSnapshot.getKey())) {
                            data_chitiet.remove(i);
                            ct_hoadon_chitietAdapter.notifyDataSetChanged();
                            break;

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
        }else {
            mdata.child(Namehoadonchitiet).child(keyshowhoadon).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    hoadon_chitiet = dataSnapshot.getValue(Hoadon_chitiet.class);
                    hoadon_chitiet.key = dataSnapshot.getKey();

                    int soluong = Integer.parseInt(hoadon_chitiet.soluong.toString());

                    String giatien = hoadon_chitiet.gia.toString();
                    tong += soluong * Float.parseFloat(giatien);
                    tongsl += soluong;

                    tongtien = String.valueOf(tong);
                    txt_tong.setText(decimalFormat.format(tong) + " VND");
                    String isclick = bundle.getString("click");
                    String a = "a";
                    if (isclick.equals(a)) {
                        bt_tab2_huy.setVisibility(View.GONE);
                        bt_tab2_them.setText("Thanh toan");
                    }

                    createdata_Tab2();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                    for (int i = 0; i < data_chitiet.size(); i++) {
                        if (data_chitiet.get(i).id.equals(dataSnapshot.getKey())) {
                            data_chitiet.remove(i);
                            ct_hoadon_chitietAdapter.notifyDataSetChanged();
                            break;

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
    }

    private void getHD(){
        mdata.child("thucdon").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                menu = dataSnapshot.getValue(Menu.class);
                menu.key = dataSnapshot.getKey();
                for(int i=0;i<alNhom.size();i++){
                    if(menu.nhom_id.equals(alNhom.get(i).nhomkey)){
                        menu.nhom_ten = alNhom.get(i).nhom_ten;
                    }
                }
                createdata_Tab1();

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

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {


        return true;
    }



    //search
    private void searchname(){
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
//
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                ct_hoadonAdapter_layoutBill_2.filter(s.toLowerCase().trim());

                return true;
            }
        });
    }


    // tao data cho tab1
    private void createdata_Tab1 (){


        data.add(new CT_hoadon_layout_2(menu.thucdon_gia,menu.thucdon_ten,menu.nhom_ten,menu.key));

        setadapter_tab1();

    }

    //tao data cho tab2
    private void createdata_Tab2(){
        if(hoadon_chitiet.thucdon_id.equals("")== false) {
                CT_Hoadon_chitiet_layout_2 ct = new CT_Hoadon_chitiet_layout_2(hoadon_chitiet.ten, hoadon_chitiet.thucdon_id, hoadon_chitiet.soluong, hoadon_chitiet.gia, hoadon_chitiet.tongtien);
                ct.id = hoadon_chitiet.key;
                data_chitiet.add(ct);
                ct_hoadon_chitietAdapter.notifyDataSetChanged();

        }
    }


    // on click de chon mon (hoadon)
    private void clicklistview(){
        //listview tab 1
        lv_tab1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final Dialog dialog = new Dialog(Main_Layout_Bill_2.this,R.style.Dialog);
                dialog.setContentView(R.layout.custom_dialog_layoutbill_2);
                dialog.setTitle("Chọn Món");

                dialog.setCanceledOnTouchOutside(false);

               String positionTen = data.get(position).thucdon_ten;

                final String positionKey = data.get(position).key;

                final String positionGia=data.get(position).thucdon_gia;


                txt_soluong = (EditText) dialog.findViewById(R.id.txt_id_number);
               final TextView txt_tenmonan = (TextView)dialog.findViewById(R.id.txt_id_tenmonandachon);
                txt_tenmonan.setText(positionTen);


                TextView bt_giam = (TextView) dialog.findViewById(R.id.bt_id_giamsoluong);
                TextView bt_tang = (TextView) dialog.findViewById(R.id.bt_id_tangsoluong);
                Button bt_huy = (Button)dialog.findViewById(R.id.bt_id_huymonandachon);
                Button bt_xacnhan = (Button)dialog.findViewById(R.id.bt_id_xacnhanmonandachon);
                String test = txt_soluong.getText().toString();
                if (test.equals("")||test.equals(0)){
                    txt_soluong.setText("1");
                }

                //Button tang giam so luong
                bt_tang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String edt_soluong = txt_soluong.getText().toString();
                        if (!edt_soluong.equals("")){
                        soluong = Integer.parseInt(edt_soluong);
                       soluong += 1;
                        txt_soluong.setText(String.valueOf(soluong));}
                        else {
                            txt_soluong.setText("1");
                        }

                    }
                });

                bt_giam.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String edt_soluong = txt_soluong.getText().toString();
                        if (!edt_soluong.equals("")){
                        soluong = Integer.parseInt(edt_soluong);
                        if (soluong <= 1){

                            Toast.makeText(Main_Layout_Bill_2.this,"Tối thiểu là 1",Toast.LENGTH_SHORT).show();
                        }else {

                            soluong -=1;
                            txt_soluong.setText(String.valueOf(soluong));
                        }}else {
                            txt_soluong.setText("1");
                        }
                    }
                });

                //Button xac nhan va huy

                bt_huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        soluong = 1;
                        dialog.cancel();
                    }
                });


                bt_xacnhan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String soluong_ct = txt_soluong.getText().toString();
                        if (soluong_ct.toString().trim().equals("") || Integer.parseInt(soluong_ct) == 0 ){
                            Toast.makeText(Main_Layout_Bill_2.this,"Số lượng không đúng",Toast.LENGTH_LONG).show();
                        }else {
                            STRtong_ct = Integer.parseInt(soluong_ct) * Integer.parseInt(positionGia);


                            keyshowhoadon = bundle.getString("positionkey");
                            getkey = bundle.getString("Key");

                            Hoadon_chitiet hoadon_CT = new Hoadon_chitiet(txt_tenmonan.getText().toString(), positionKey, txt_soluong.getText().toString(),
                                    positionGia.toString(), String.valueOf(STRtong_ct)
                            );


                            if (keyshowhoadon == null) {
                                mdata.child(Namehoadonchitiet).child(getkey).push().setValue(hoadon_CT);
                            } else {
                                mdata.child(Namehoadonchitiet).child(keyshowhoadon).push().setValue(hoadon_CT);
                            }

                            dialog.cancel();
                            soluong = 1;
                        }
                    }
                });

                dialog.show();
            }
        });


    }

    // long click xoa mon an
    private void longclicklistview(){
        getkey = bundle.getString("Key");
        keyshowhoadon = bundle.getString("positionkey");
        lv_tab2.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                 xoa = data_chitiet.get(position).id;


                AlertDialog.Builder builder = new AlertDialog.Builder(Main_Layout_Bill_2.this);
                builder.setMessage("Bạn có muốn xóa món đã chọn").setCancelable(false).setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // nếu hóa đơn chưa phục vụ mới đc xóa
                        if(isRemove==true) {
                            if (keyshowhoadon == null) {
                                mdata.child(Namehoadonchitiet).child(getkey).child(xoa).removeValue();

                                tongconlai = tong;
                                tongconlai -= Float.parseFloat(data_chitiet.get(position).gia) * Float.parseFloat(data_chitiet.get(position).soluong);
                                tong = tongconlai;
                                tongtien = String.valueOf(tong);
                                tongsl -= Integer.parseInt(data_chitiet.get(position).soluong);
                                txt_tong.setText(decimalFormat.format(tong) + " VND");
                            } else {
                                mdata.child(Namehoadonchitiet).child(keyshowhoadon).child(xoa).removeValue();

                                tongconlai = tong;
                                tongconlai -= Float.parseFloat(data_chitiet.get(position).gia) * Float.parseFloat(data_chitiet.get(position).soluong);
                                tong = tongconlai;
                                tongtien = String.valueOf(tong);
                                tongsl -= Integer.parseInt(data_chitiet.get(position).soluong);
                                txt_tong.setText(decimalFormat.format(tong) + " VND");
                            }
                        }
                        else{
                            Toast.makeText(Main_Layout_Bill_2.this, "Thực đơn đã phục vụ không thể xóa.",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                }).setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

                return true;

            }
        });

    }
    // button huy mon an


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (keyshowhoadon==null) {
                alertDialogBuilder = new AlertDialog.Builder(Main_Layout_Bill_2.this);
                alertDialogBuilder.setMessage("Bạn có muốn hủy hóa đơn!");
                alertDialogBuilder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getkey = bundle.getString("Key");
                        getkeyban = bundle.getString("Ban");

                        mdata.child(Namehoadonchitiet).child(getkey).removeValue();
                        mdata.child("Hoadon").child(getkey).removeValue();
                        mdata.child("ban").child(getkeyban).child("choose").removeValue();
                        dialog.dismiss();
                        finish();

                    }
                }).setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
        return super.onKeyDown(keyCode, event);
    }


}
