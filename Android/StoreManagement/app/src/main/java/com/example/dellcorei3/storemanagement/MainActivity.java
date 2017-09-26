package com.example.dellcorei3.storemanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dellcorei3.storemanagement.store.manager.MainManagementActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    EditText etEmail,etPass;
    Button btEmployee,btManager;
    CheckBox cbSave;

    private FirebaseAuth mAuth;
    private DatabaseReference myDatabase;
    String email,password;
    int count = 0;
    ProgressDialog progressDialog;
    private ArrayList<String> managerAccountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        myDatabase = FirebaseDatabase.getInstance().getReference();
        addControls();
        // đọc dữ liệu trong shared preference
        SharedPreferences sp = getSharedPreferences("LoginData", MODE_PRIVATE);
        // đọc email,pass in lên edit text
        cbSave.setChecked(sp.getBoolean("checkbox",false));
        etEmail.setText(sp.getString("email",""));
        etPass.setText(sp.getString("pass",""));

        // đọc dữ liệu tài khoản từ firebase
        getManagerAccount();
        // đăng nhập tài khoản quản lý
        btManager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString();
                password = etPass.getText().toString();
                // kiểm tra edit text null
                if(checkIsEmpty(email) == true){
                    etEmail.setError("Hảy nhập email!");
                    return;
                }
                if(checkIsEmpty(password) == true){
                    etPass.setError("Hảy nhập mật khẩu!");
                    return;
                }
                // đăng nhập
                signInManager(email,password);
            }
        });
        // đăng nhập tài khoản nhân viên
        btEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString();
                password = etPass.getText().toString();
                // kiểm tra edit text null
                if(checkIsEmpty(email) == true){
                    etEmail.setError("Hảy nhập email!");
                    return;
                }
                if(checkIsEmpty(password) == true){
                    etPass.setError("Hảy nhập mật khẩu!");
                    return;
                }
                // đăng nhập

            }
        });

        /*
        SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String s = sfd.format(new Date(Long.parseLong("1505745885382")));*/


    }
    private void addControls(){
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPass = (EditText)findViewById(R.id.etPass);
        btEmployee = (Button)findViewById(R.id.btEmployee);
        btManager = (Button)findViewById(R.id.btManager);
        cbSave = (CheckBox)findViewById(R.id.cbSave);
    }

    private void signInManager(final String email, final String password){
        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    count = 0;
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("", "signInWithEmail:success");
                    // xác thực tài khoản quản lý
                    for (int i = 0;i < managerAccountList.size();i++){
                        if(email.equals(managerAccountList.get(i)) == true){
                            // lưu dữ liệu vào shared
                            saveData(email,password);

                            Intent it = new Intent(MainActivity.this, MainManagementActivity.class);
                            startActivity(it);
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            return;
                        }
                    }

                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    etPass.setText("");
                    // đăng xuất tài khoản
                    mAuth.signOut();
                    Toast.makeText(MainActivity.this, "Không đủ quyền hạn đăng nhập!",
                            Toast.LENGTH_SHORT).show();

                } else {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    etPass.setText("");
                    // If sign in fails, display a message to the user.
                    Toast.makeText(MainActivity.this, "Tài khoản hoặc mật khẩu không đúng",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getManagerAccount(){
        managerAccountList = new ArrayList<>();
        myDatabase.child("validation/manager").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                managerAccountList.add(dataSnapshot.getValue().toString());
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

    private void validateAccount(final String email,final String password){

        // kiem tra tai khoan trong database
        myDatabase.child("validation/manager").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                count++;

                if(email.equals(dataSnapshot.getValue().toString()) == true){


                    etPass.setText("");

                    Intent it = new Intent(MainActivity.this, MainManagementActivity.class);
                    startActivity(it);
                    // tắt dialog
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
                else if(count >= dataSnapshot.getChildrenCount() ){// nếu trong database ko có tài khoản -> failed
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    etPass.setText("");
                    // đăng xuất tài khoản
                    mAuth.signOut();
                    Toast.makeText(MainActivity.this, "Không đủ quyền hạn đăng nhập!",
                            Toast.LENGTH_SHORT).show();
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
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void signInEmployee(final String email, final String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("", "signInWithEmail:success");

                } else {
                    etPass.setText("");
                    // If sign in fails, display a message to the user.
                    Toast.makeText(MainActivity.this, "Tài khoản hoặc mật khẩu không đúng",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean checkIsEmpty(String text){
        if(TextUtils.isEmpty(text) == true){
            return true;
        }
        return false;
    }

    private void saveData(String email,String pass){
        SharedPreferences sp = getSharedPreferences("LoginData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("email",email);

        if(cbSave.isChecked() == true){
            editor.putString("pass",pass);
            editor.putBoolean("checkbox",true);
        }
        else{
            editor.remove("pass");
            editor.remove("checkbox");
        }
        editor.commit();
    }
}
