package com.example.dellcorei3.storemanagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText etEmail;
    Button btConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        addControls();

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=etEmail.getText().toString();
                if(checkIsEmpty(email) == true){
                    etEmail.setError("Hãy nhập email!");
                    return;
                }
                if(checkEmail(email)==false){
                    Toast.makeText(ForgotPasswordActivity.this, "Email không hợp lệ",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        createDialog();
                    }

                });
            }
        });
    }

    private void addControls(){
        etEmail=(EditText)findViewById(R.id.etEmailForgot);
        btConfirm=(Button) findViewById(R.id.btConfirmForgot);
    }

    private boolean checkIsEmpty(String text){
        if(TextUtils.isEmpty(text) == true){
            return true;
        }
        return false;
    }

    private boolean checkEmail(String email){
        if(email.indexOf(".")>0 && email.indexOf("@")>0){
            return true;
        }
        return false;
    }

    private void createDialog(){
        AlertDialog.Builder b=new AlertDialog.Builder(ForgotPasswordActivity.this);
        b.setTitle("Đã gửi email phục hồi mật khẩu!");
        b.setMessage("Hãy kiểm tra email và làm theo hướng dẫn");
        b.setPositiveButton("Ok", new DialogInterface. OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                finish();
            }});

        b.create().show();
    }


}
