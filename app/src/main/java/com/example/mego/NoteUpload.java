package com.example.mego;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;

public class NoteUpload extends AppCompatActivity {

    ImageButton saveButton;
    EditText uploadTopic, uploadDesc;
    DatabaseReference mRootRef;
    FirebaseAuth auth;
    String day="",month="",year="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_upload);
        mRootRef = FirebaseDatabase.getInstance().getReference();
        uploadDesc = findViewById(R.id.uploadDesc);
        uploadTopic = findViewById(R.id.uploadTopic);
        saveButton = findViewById(R.id.saveButton);
        auth = FirebaseAuth.getInstance();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadData();
            }
        });
    }
    public void uploadData(){
        String title = uploadTopic.getText().toString();
        String desc = uploadDesc.getText().toString();
        String currentDate = DateFormat.getDateTimeInstance().format(java.util.Calendar.getInstance().getTime());
        NotesDataClass dataClass = new NotesDataClass(title, desc, day,month,year,currentDate);
        mRootRef.child("Notes").child(auth.getCurrentUser().getUid()).child(currentDate)
                .setValue(dataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(NoteUpload.this, "Saved", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(NoteUpload.this, NotesMain.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(NoteUpload.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}