package com.example.mego;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import com.example.mego.expensemanagerapp.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainPage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    FirebaseAuth auth;
    TextView user;
    DatabaseReference ref;
    CardView calendar, list, saving, exp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_veiw);
        toolbar = findViewById(R.id.toolbar2);
        auth = FirebaseAuth.getInstance();
        calendar = findViewById(R.id.calendarCard);
        list = findViewById(R.id.listCard);
        saving = findViewById(R.id.savCard);
        exp = findViewById(R.id.expCard);
        setSupportActionBar(toolbar);
        user = findViewById(R.id.username);
        setUsername(user);
        navigationView.bringToFront();
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_home).setVisible(false);
        menu.findItem(R.id.nav_calendar).setVisible(true);
        menu.findItem(R.id.nav_list).setVisible(true);
        menu.findItem(R.id.nav_savings).setVisible(true);
        menu.findItem(R.id.nav_exp).setVisible(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, Calendar.class);
                startActivity(intent);
            }
        });
        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, NotesMain.class);
                startActivity(intent);
            }
        });
        saving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, Home.class);
                startActivity(intent);
            }
        });
        exp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainPage.this, Home.class);
                startActivity(intent);
            }
        });
    }

    private void setUsername(TextView user) {
        user.setText("");
        ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot dataSnapshot = task.getResult();
                        String username = String.valueOf(dataSnapshot.child("user").getValue());
                        user.setText(username);
                    }else{
                        Toast.makeText(MainPage.this,"Error No Data in User!!",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainPage.this,"Error User Dont Exist!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_logout){
            auth.signOut();
            SignOut();
        } else if (item.getItemId() == R.id.nav_calendar) {
            Intent intent = new Intent(MainPage.this, Calendar.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.nav_list) {
            Intent intent = new Intent(MainPage.this, NotesMain.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.nav_share) {
            Toast.makeText(MainPage.this,"Thank you for Sharing!",Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.nav_rate) {
            Toast.makeText(MainPage.this,"Thank you for Rating Us!",Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.nav_savings) {
            Intent intent = new Intent(MainPage.this, Home.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.nav_exp) {
            Intent intent = new Intent(MainPage.this, Home.class);
            startActivity(intent);
        }else if (item.getItemId() == R.id.nav_settings) {
            Intent intent = new Intent(MainPage.this, PrifileSettings.class);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void SignOut() {
        Intent intent = new Intent(MainPage.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}