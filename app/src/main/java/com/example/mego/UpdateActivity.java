package com.example.mego;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewKt;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class UpdateActivity extends AppCompatActivity {

    CalendarView dateEvent;
    TextView text;
    Button updateButton;
    EditText updateDesc, updateTitle;
    String title, desc;
    String imageUrl;
    FirebaseAuth auth;
    String day="",month="",year="",date="",prevtitle="",prevdes="",prevday="",prevmonth="",prevyear="";


    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        text = findViewById(R.id.text);
        updateButton = findViewById(R.id.updateButton);
        updateDesc = findViewById(R.id.updateDesc);
        updateTitle = findViewById(R.id.updateTitle);
        dateEvent = findViewById(R.id.calendarView);
        auth = FirebaseAuth.getInstance();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            date = bundle.getString("dateID");
            prevday = bundle.getString("Day");
            prevmonth = bundle.getString("Month");
            prevyear = bundle.getString("Year");
            prevtitle = bundle.getString("Title");
            prevdes = bundle.getString("Description");
        }
        updateTitle.setHint(prevtitle);
        updateDesc.setHint(prevdes);
        text.append("("+prevday+"/"+prevmonth+"/"+prevyear+")");
        databaseReference = FirebaseDatabase.getInstance().getReference("Events").child(auth.getCurrentUser().getUid()).child(date);
        dateEvent.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                year = Integer.toString(i);
                month = Integer.toString(i1);
                day = Integer.toString(i2);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
                Intent intent = new Intent(UpdateActivity.this, Calendar.class);
                startActivity(intent);
            }
        });
    }
    public void updateData(){
        title = updateTitle.getText().toString().trim();
        desc = updateDesc.getText().toString().trim();
        if(title.isEmpty()){
            title = prevtitle;
        }
        if(desc.isEmpty()){
            desc = prevdes;
        }
        if(day.isEmpty()&&month.isEmpty()&&year.isEmpty()){
            day = prevday;
            month = prevmonth;
            year = prevyear;
        }
        DataClass dataClass = new DataClass(title, desc, day,month,year,date);
        databaseReference.setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(UpdateActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}