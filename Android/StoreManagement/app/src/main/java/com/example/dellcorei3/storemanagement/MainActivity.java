package com.example.dellcorei3.storemanagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dellcorei3.storemanagement.employee.LonginAndCheckin.CheckinActivity;
import com.example.dellcorei3.storemanagement.store.manager.MainManagementActivity;
import com.example.dellcorei3.storemanagement.store.manager.models.CheckIn;
import com.example.dellcorei3.storemanagement.store.manager.models.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity {

    EditText etEmail,etPass;
    Button btLogIn;
    CheckBox cbSave;
    RadioButton rdEmployee,rdManger;
    TextView tvForgotPassword;

    private FirebaseAuth mAuth;
    private DatabaseReference myDatabase;
    String email,password;
    int count = 0;
    ProgressDialog progressDialog;
    private ArrayList<String> managerAccountList;
    private ArrayList<Employee> employeeAccountList;

    private static final int EMPLOYEE_CHECKED = 1;
    private static final int MANAGER_CHECKED = 2;

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
        // đọc radio
        int checked = sp.getInt("radio",EMPLOYEE_CHECKED);
        if(checked == EMPLOYEE_CHECKED){
            rdEmployee.setChecked(true);
        }
        else{
            rdManger.setChecked(true);
        }

        // đăng nhập tài khoản
        btLogIn.setOnClickListener(new View.OnClickListener() {
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
                signIn(email,password);
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it=new Intent(MainActivity.this,ForgotPasswordActivity.class);
                startActivity(it);
            }
        });
        /*
        SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String s = sfd.format(new Date(Long.parseLong("1505745885382")));*/
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        Log.d("ip",ip);

    }
    private void addControls(){
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPass = (EditText)findViewById(R.id.etPass);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        btLogIn = (Button)findViewById(R.id.btLogIn);

        cbSave = (CheckBox)findViewById(R.id.cbSave);
        rdEmployee = (RadioButton)findViewById(R.id.rdLogInEmployee);
        rdManger = (RadioButton)findViewById(R.id.rdLogInManager);
    }

    private void signIn(final String email, final String password){
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
                    if(rdEmployee.isChecked() == true) {
                        // xác thực tài khoản nhân viên
                        getEmployeeAccount();
                    }
                    else{
                        // xác thực tài khoản quản lý
                        getManagerAccount();
                    }

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

        myDatabase.child("validation/manager").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        managerAccountList.add(issue.getValue().toString());
                    }

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
                    // tắt dialog
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    etPass.setText("");
                    // đăng xuất tài khoản
                    mAuth.signOut();
                    Toast.makeText(MainActivity.this, "Không đủ quyền hạn đăng nhập!",
                            Toast.LENGTH_SHORT).show();

                }
                else{
                    // tắt dialog
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
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void getEmployeeAccount(){
        employeeAccountList = new ArrayList<>();

        Query query = myDatabase.child("nhanvien").orderByChild("position").equalTo("phucvu");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        Employee emp = issue.getValue(Employee.class);
                        if(emp.email.equals(email) == true && emp.state == 1 && emp.position.equals("phucvu") == true){
                            Toast.makeText(MainActivity.this, "Đăng nhập thành công",
                                    Toast.LENGTH_SHORT).show();
                            // lưu dữ liệu vào shared
                            saveData(email,password);

                            Intent it = new Intent(MainActivity.this, CheckinActivity.class);
                            it.putExtra("Email", email);
                            startActivity(it);
                            // tắt dialog
                            if (progressDialog.isShowing()) {
                                progressDialog.dismiss();
                            }
                            return;
                        }
                    }
                    // tắt dialog
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    Toast.makeText(MainActivity.this, "Tài khoản đã bị khóa hoặc không phù hợp",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // tắt dialog
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
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

        if(rdEmployee.isChecked() == true) {
            editor.putInt("radio",EMPLOYEE_CHECKED);
        }
        else{
            editor.putInt("radio",MANAGER_CHECKED);
        }

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
