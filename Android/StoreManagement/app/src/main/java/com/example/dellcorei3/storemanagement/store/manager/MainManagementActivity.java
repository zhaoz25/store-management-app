package com.example.dellcorei3.storemanagement.store.manager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.dellcorei3.storemanagement.R;
import com.google.firebase.auth.FirebaseAuth;

public class MainManagementActivity extends AppCompatActivity {

    ImageButton btCreateAccount,btShift,btEmployeeManagement,btRevenue,btProduct,btRevenueFTime;


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
        btProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainManagementActivity.this,ProductStatisticActivity.class);
                startActivity(it);
            }
        });
        btRevenueFTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainManagementActivity.this,RevenueFTimeActivity.class);
                startActivity(it);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionMainInfo:
                Intent it = new Intent(MainManagementActivity.this,StoreInfomationActivity.class);
                startActivity(it);

                break;
            case R.id.optionMainLogout:
                FirebaseAuth.getInstance().signOut();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addControls(){
        btCreateAccount = (ImageButton)findViewById(R.id.btCreateAccount);
        btShift = (ImageButton)findViewById(R.id.btShift);
        btEmployeeManagement = (ImageButton)findViewById(R.id.btEmployeeManagement);
        btRevenue = (ImageButton)findViewById(R.id.btRevenue);
        btProduct = (ImageButton)findViewById(R.id.btStatisticProduct);
        btRevenueFTime = (ImageButton)findViewById(R.id.btRevenueFTime);
    }
}
