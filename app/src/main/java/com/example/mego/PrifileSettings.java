package com.example.mego;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class PrifileSettings extends AppCompatActivity {
    FirebaseAuth auth;
    Button updatebtn;
    EditText user,pass,email,newpass;
    String oldpassword,newemail,newpassword,newuser,oldpassdb,usernamedb,emaildb;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prifile_settings);
        updatebtn = findViewById(R.id.updatebtn);
        user = findViewById(R.id.username);
        pass = findViewById(R.id.password);
        email = findViewById(R.id.email);
        newpass = findViewById(R.id.repassword);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        usernamedb = String.valueOf(dataSnapshot.child("user").getValue());
                        emaildb = String.valueOf(dataSnapshot.child("email").getValue());
                        oldpassdb = String.valueOf(dataSnapshot.child("password").getValue());
                        user.setHint(usernamedb);
                        email.setHint(emaildb);
                    }else{
                        Toast.makeText(PrifileSettings.this,"Error No Data in User!!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(PrifileSettings.this,"Error User Dont Exist!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        updatebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldpassword = pass.getText().toString();
                newemail = email.getText().toString();
                newpassword = newpass.getText().toString();
                newuser = user.getText().toString();
                if(oldpassdb.equals(oldpassword)&&!oldpassword.isEmpty()&&!newpassword.isEmpty()&&newemail.isEmpty()&&newuser.isEmpty()){
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("user",usernamedb);
                    map.put("email",emaildb);
                    map.put("password",newpassword);
                    map.put("id",auth.getCurrentUser().getUid());
                    databaseReference.child(auth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(PrifileSettings.this,"Update Profile",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PrifileSettings.this,MainPage.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }else if(oldpassdb.equals(oldpassword)&&!oldpassword.isEmpty()&&!newpassword.isEmpty()&&!newemail.isEmpty()&&!newuser.isEmpty()){
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("user",newuser);
                    map.put("email",newemail);
                    map.put("password",newpassword);
                    map.put("id",auth.getCurrentUser().getUid());
                    databaseReference.child(auth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(PrifileSettings.this,"Update Profile",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PrifileSettings.this,MainPage.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }else if(newuser.isEmpty()&&oldpassword.isEmpty()&newpassword.isEmpty()){
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("user",usernamedb);
                    map.put("email",newemail);
                    map.put("password",oldpassdb);
                    map.put("id",auth.getCurrentUser().getUid());
                    databaseReference.child(auth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(PrifileSettings.this,"Update Profile",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PrifileSettings.this,MainPage.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                } else if (newemail.isEmpty()&&oldpassword.isEmpty()&newpassword.isEmpty()) {
                    HashMap<String,Object> map = new HashMap<>();
                    map.put("user",newuser);
                    map.put("email",emaildb);
                    map.put("password",oldpassdb);
                    map.put("id",auth.getCurrentUser().getUid());
                    databaseReference.child(auth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(PrifileSettings.this,"Update Profile",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PrifileSettings.this,MainPage.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }else if(!(oldpassdb.equals(oldpassword))&&!oldpassword.isEmpty()&&!newpassword.isEmpty()){
                    Toast.makeText(PrifileSettings.this,"Password Doesnt match with the Old Password",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(PrifileSettings.this,"No Input",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}