package com.example.dellcorei3.storemanagement.store.manager;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.dellcorei3.storemanagement.MainActivity;
import com.example.dellcorei3.storemanagement.R;
import com.example.dellcorei3.storemanagement.store.manager.models.Employee;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreateAccountActivity extends AppCompatActivity {

    EditText etEmail,etPass,etConfirmPass;
    EditText etFirstName,etLastName;
    Button btCreate;
    RadioButton rdPhucVu,rdThuNgan;

    private FirebaseAuth mAuth;
    private DatabaseReference myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        mAuth = FirebaseAuth.getInstance();
        myDatabase = FirebaseDatabase.getInstance().getReference();
        addControls();
        rdPhucVu.setChecked(true);

        btCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkField() == false){
                    return;
                }

                String email = etEmail.getText().toString();
                String pass = etPass.getText().toString();
                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.getText().toString();
                // kiểm tra radio button
                String nv = "";
                if(rdPhucVu.isChecked() == true){
                    nv= "phucvu";
                }
                else{
                    nv="thungan";
                }

                createAccount(email,pass,nv,firstName,lastName);
            }
        });
    }

    private void addControls(){
        etEmail = (EditText)findViewById(R.id.etCreateEmail);
        etPass = (EditText)findViewById(R.id.etCreatePass);
        etConfirmPass = (EditText)findViewById(R.id.etCreateConfirmPass);
        etFirstName = (EditText)findViewById(R.id.etCreateFirstName);
        etLastName = (EditText)findViewById(R.id.etCreateLastName);
        btCreate = (Button)findViewById(R.id.btCreate);

        rdPhucVu = (RadioButton)findViewById(R.id.rdPhucVu);
        rdThuNgan = (RadioButton)findViewById(R.id.rdThuNgan);
    }

    private void createAccount(final String email, String password, final String nv, final String firstName, final String lastName){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(CreateAccountActivity.this,
                new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // tạo lớp  employee lưu thông tin nhân viên
                    Employee emp = new Employee(email,firstName,lastName,nv,1);
                    // lấy thời gian hiện tại
                    SimpleDateFormat sfd = new SimpleDateFormat("dd/MM/yyyy");
                    emp.fromDate = sfd.format(Calendar.getInstance().getTime());
                    // them thong tin nhan vien vao database
                    myDatabase.child("nhanvien").push().setValue(emp);
                    // thông báo thành công
                    Toast.makeText(CreateAccountActivity.this, "Tạo tài khoản thành công!",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Log.d("Error create user:",task.getException().getMessage());
                    Toast.makeText(CreateAccountActivity.this, "Không thể tạo tài khoản ("+task.getException().getMessage()+")",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private boolean checkField(){
        if(TextUtils.isEmpty(etEmail.getText().toString()) == true){
            etEmail.setError("Hãy nhập email!");
            return false;
        }
        if(TextUtils.isEmpty(etPass.getText().toString()) == true){
            etPass.setError("Hãy nhập mật khẩu!");
            return false;
        }
        if(TextUtils.isEmpty(etConfirmPass.getText().toString()) == true){
            etConfirmPass.setError("Hãy nhập mật khẩu!");
            return false;
        }
        if(TextUtils.isEmpty(etFirstName.getText().toString()) == true){
            etFirstName.setError("Hãy nhập tên nhân viên");
            return false;
        }
        if(TextUtils.isEmpty(etLastName.getText().toString()) == true){
            etLastName.setError("Hãy nhập tên nhân viên");
            return false;
        }

        String pass = etPass.getText().toString();
        String confirmPass = etConfirmPass.getText().toString();
        if(pass.equals(confirmPass) == false){
            etConfirmPass.setError("Mật khẩu không giống nhau!");
            return false;
        }

        return true;
    }
}
