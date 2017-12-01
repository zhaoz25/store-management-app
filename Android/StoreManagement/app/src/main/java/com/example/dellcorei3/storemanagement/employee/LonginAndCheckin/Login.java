package com.example.dellcorei3.storemanagement.employee.LonginAndCheckin;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dellcorei3.storemanagement.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends Activity {
    EditText edt_name,edt_pass;
    Button bt_login;
    FirebaseAuth mAuth;
    String email,pass;
    CheckBox checkBox;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        thamchieu();


    }
    public void thamchieu(){
        edt_name = (EditText)findViewById(R.id.txt_id_login_name);
        edt_pass = (EditText)findViewById(R.id.txt_id_login_pass);
        bt_login = (Button)findViewById(R.id.bt_id_login);
        checkBox = (CheckBox)findViewById(R.id.id_checkbox_remember);


        mAuth = FirebaseAuth.getInstance();



        //onclick button
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();

                Log.d("login","a");
            }
        });
    }



    public void signup(){
        email = edt_name.getText().toString();
        pass = edt_pass.getText().toString();
        Log.d("Test",email);
        if (!email.isEmpty() && !pass.isEmpty()) {
        mAuth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                           if (task.isSuccessful()) {
                               Intent i = new Intent(Login.this, CheckinActivity.class);
                               i.putExtra("Email", email.toString());
                               startActivity(i);
                               if (!checkBox.isChecked()) {
                                   edt_name.setText("");
                                   edt_pass.setText("");

                               }

                           } else {
                               edt_name.setText("");
                               edt_pass.setText("");
                               checkBox.setChecked(false);
                               Toast.makeText(Login.this, "Tài khoản hoăc mật khẩu không đúng", Toast.LENGTH_LONG).show();
                           }

                    }
                });
        }else {
            Toast.makeText(Login.this, "Vui long nhap day du", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        //gọi hàm lưu trạng thái ở đây
        savedata();
    }
    //Phục hồi khi mở app lại
    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        //gọi hàm đọc trạng thái ở đây
        readdata();
    }

    private void savedata(){
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sp.edit();

        String nameR = edt_name.getText().toString();
        String passR = edt_pass.getText().toString();
        boolean isclick = checkBox.isChecked();
        if (!isclick){
            editor.clear();
        }else {
            editor.putString("name", nameR);
            editor.putString("pass", passR);
            editor.putBoolean("checked", isclick);

        }
        editor.commit();
    }


    private void readdata(){
        sp = PreferenceManager.getDefaultSharedPreferences(this);
       boolean bchk = sp.getBoolean("checked",false);
        if (bchk){
            String user = sp.getString("name","");
            String pass = sp.getString("pass","");
            edt_name.setText(user);
            edt_pass.setText(pass);
        }
        checkBox.setChecked(bchk);
    }
}
