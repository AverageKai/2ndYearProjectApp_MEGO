package com.example.mego;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.mego.expensemanagerapp.Home;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class NotesMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    FloatingActionButton fab;
    NavigationView navigationView;
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    DrawerLayout drawerLayout;
    NoteAdapter adapter;
    Toolbar toolbar;
    List<NotesDataClass> dataList;
    SearchView searchView;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //menu
        setContentView(R.layout.activity_notes_main);
        drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar2);
        navigationView = findViewById(R.id.nav_veiw);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_home).setVisible(true);
        menu.findItem(R.id.nav_calendar).setVisible(true);
        menu.findItem(R.id.nav_list).setVisible(false);
        menu.findItem(R.id.nav_savings).setVisible(true);
        menu.findItem(R.id.nav_exp).setVisible(true);
        navigationView.setNavigationItemSelectedListener(this);
        toolbar.setTitle("Notes");
        recyclerView = findViewById(R.id.recyclerView);
        auth = FirebaseAuth.getInstance();
        fab = findViewById(R.id.fab);
        searchView = findViewById(R.id.search);
        searchView.clearFocus();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(NotesMain.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        AlertDialog.Builder builder = new AlertDialog.Builder(NotesMain.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();
        dataList = new ArrayList<>();
        adapter = new NoteAdapter(NotesMain.this, dataList);
        recyclerView.setAdapter(adapter);
        databaseReference = FirebaseDatabase.getInstance().getReference("Notes").child(auth.getUid());
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    NotesDataClass dataClass = itemSnapshot.getValue(NotesDataClass.class);
                    dataClass.setKey(auth.getCurrentUser().getUid());
                    dataList.add(dataClass);
                }
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return true;
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotesMain.this, NoteUpload.class);
                startActivity(intent);
            }
        });
    }
    public void searchList(String text){
        ArrayList<NotesDataClass> searchList = new ArrayList<>();
        for (NotesDataClass notedataClass: dataList){
            if (notedataClass.getDataTitle().toLowerCase().contains(text.toLowerCase())){
                searchList.add(notedataClass);
            }
        }
        adapter.searchDataList(searchList);
    }

    private void SignOut() {
        Intent intent = new Intent(NotesMain.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_logout){
            auth.signOut();
            SignOut();
        } else if (item.getItemId() == R.id.nav_home) {
            Intent intent = new Intent(NotesMain.this, MainPage.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.nav_calendar) {
            Intent intent = new Intent(NotesMain.this, Calendar.class);
            startActivity(intent);
            finish();
        }else if (item.getItemId() == R.id.nav_share) {
            Toast.makeText(NotesMain.this,"Thank you for Sharing!",Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.nav_rate) {
            Toast.makeText(NotesMain.this,"Thank you for Rating Us!",Toast.LENGTH_SHORT).show();
        }else if (item.getItemId() == R.id.nav_savings) {
            Intent intent = new Intent(NotesMain.this, Home.class);
            startActivity(intent);
        } else if (item.getItemId() == R.id.nav_exp) {
            Intent intent = new Intent(NotesMain.this, Home.class);
            startActivity(intent);
        }else if (item.getItemId() == R.id.nav_settings) {
            Intent intent = new Intent(NotesMain.this, PrifileSettings.class);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}