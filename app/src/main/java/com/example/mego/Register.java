package com.example.mego;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    EditText email,password,repassword,user;
    Button register;
    DatabaseReference mRootRef;
    FirebaseAuth auth;
    TextView goLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.email);
        user = findViewById(R.id.username);
        password = findViewById(R.id.password);
        register = findViewById(R.id.registerbtn);
        repassword = findViewById(R.id.repassword);
        goLogin = findViewById(R.id.textveiwlogin);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtEmail = email.getText().toString();
                String txtPass = password.getText().toString();
                String txtRepass = repassword.getText().toString();
                String txtUser = user.getText().toString();
                if(txtPass.isEmpty()||txtEmail.isEmpty()||txtRepass.isEmpty()||txtUser.isEmpty()){
                        Toast.makeText(Register.this,"Must Fill everthing",Toast.LENGTH_SHORT).show();
                }else{
                    if(txtPass.equals(txtRepass)) {
                        if(txtPass.length()>=6) {
                            registerUser(txtUser, txtEmail, txtPass);
                        }else{
                            Toast.makeText(Register.this, "Password Must be 6 Characters or Above", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(Register.this, "Password Dont Match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void registerUser(String user ,String email, String password) {
        auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String,Object>map = new HashMap<>();
                map.put("user",user);
                map.put("email",email);
                map.put("password",password);
                map.put("id",auth.getCurrentUser().getUid());
                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                builder.setCancelable(false);
                builder.setView(R.layout.progress_layout);
                AlertDialog dialog = builder.create();
                dialog.show();
                mRootRef.child("Users").child(auth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this,"Registered Profile",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            Intent intent = new Intent(Register.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Register.this,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}