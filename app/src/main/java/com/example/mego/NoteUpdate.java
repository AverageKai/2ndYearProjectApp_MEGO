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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NoteUpdate extends AppCompatActivity {

    ImageButton updateButton;
    EditText updateDesc, updateTitle;
    String title, desc;
    String imageUrl;
    FirebaseAuth auth;
    String day="",month="",year="",date="",prevtitle="",prevdes="";


    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_update);
        updateButton = findViewById(R.id.updateButton);
        updateDesc = findViewById(R.id.updateDesc);
        updateTitle = findViewById(R.id.updateTitle);
        auth = FirebaseAuth.getInstance();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            date = bundle.getString("dateID");
            prevtitle = bundle.getString("Title");
            prevdes = bundle.getString("Description");
        }
        updateTitle.setHint(prevtitle);
        updateDesc.setHint(prevdes);
        databaseReference = FirebaseDatabase.getInstance().getReference("Notes").child(auth.getCurrentUser().getUid()).child(date);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData();
                Intent intent = new Intent(NoteUpdate.this, NotesMain.class);
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
        NotesDataClass notesDataClass = new NotesDataClass(title, desc, day,month,year,date);
        databaseReference.setValue(notesDataClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(NoteUpdate.this, "Updated", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(NoteUpdate.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}