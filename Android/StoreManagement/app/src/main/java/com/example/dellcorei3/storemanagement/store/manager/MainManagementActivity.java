package com.example.dellcorei3.storemanagement.store.manager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.store.manager.models.Shift;
import com.google.firebase.database.ServerValue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class MainManagementActivity extends AppCompatActivity {

    ImageButton btCreateAccount,btShift,btEmployeeManagement,btRevenue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_management);

        addControls();

        btCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainManagementActivity.this,CreateAccountActivity.class);
                startActivity(it);
            }
        });
        btShift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent it = new Intent(MainManagementActivity.this,ShiftActivity.class);
                    startActivity(it);
                }
                catch(Exception e){

                }
            }
        });
        btEmployeeManagement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MainManagementActivity.this,EmployeeManagementActivity.class);
                startActivity(it);
            }
        });
        btRevenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainManagementActivity.this,RevenueActivity.class);
                startActivity(it);
            }
        });
    }

    private void addControls(){
        btCreateAccount = (ImageButton)findViewById(R.id.btCreateAccount);
        btShift = (ImageButton)findViewById(R.id.btShift);
        btEmployeeManagement = (ImageButton)findViewById(R.id.btEmployeeManagement);
        btRevenue = (ImageButton)findViewById(R.id.btRevenue);
    }
}
