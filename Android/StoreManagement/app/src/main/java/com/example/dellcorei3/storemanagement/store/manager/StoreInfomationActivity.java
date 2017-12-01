package com.example.dellcorei3.storemanagement.store.manager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.store.manager.models.StoreInfomation;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StoreInfomationActivity extends AppCompatActivity {

    EditText etInfoName,etInfoTitle,etInfoAddress,etInfoPhone,etInfoDuration,etInfoWebsite,etInfoEmail;
    Button btUpdate;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_infomation);

        addControls();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("store_information").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() == true){
                    StoreInfomation info = dataSnapshot.getValue(StoreInfomation.class);
                    etInfoName.setText(info.name);
                    etInfoTitle.setText(info.title);
                    etInfoAddress.setText(info.address);
                    etInfoPhone.setText(info.phone);
                    etInfoDuration.setText(info.duration);
                    etInfoWebsite.setText(info.website);
                    etInfoEmail.setText(info.email);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etInfoName.getText().toString();
                String title = etInfoTitle.getText().toString();
                String address = etInfoAddress.getText().toString();
                String phone = etInfoPhone.getText().toString();
                String duration = etInfoDuration.getText().toString();
                String website = etInfoWebsite.getText().toString();
                String email = etInfoEmail.getText().toString();

                // kiểm tra nhập dữ liệu
                if(checkIsEmpty(name) == true){
                    etInfoName.setError("Hãy nhập tên cửa hàng");
                    return;
                }
                if(checkIsEmpty(address) == true){
                    etInfoAddress.setError("Hãy nhập địa chỉ");
                    return;
                }

                StoreInfomation info = new StoreInfomation(name,address,title,email,phone,duration,website);
                mDatabase.child("store_information").setValue(info);
                Toast.makeText(StoreInfomationActivity.this, "Cập nhật thành công!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addControls(){
        etInfoName = (EditText)findViewById(R.id.etInfoName);
        etInfoTitle = (EditText)findViewById(R.id.etInfoTitle);
        etInfoAddress = (EditText)findViewById(R.id.etInfoAddress);
        etInfoPhone = (EditText)findViewById(R.id.etInfoPhone);
        etInfoDuration = (EditText)findViewById(R.id.etInfoDuration);
        etInfoWebsite = (EditText)findViewById(R.id.etInfoWebsite);
        etInfoEmail = (EditText)findViewById(R.id.etInfoEmail);

        btUpdate = (Button) findViewById(R.id.btUpdate);
    }

    private boolean checkIsEmpty(String text){
        if(TextUtils.isEmpty(text) == true){
            return true;
        }
        return false;
    }
}
