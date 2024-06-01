package com.example.mego.expensemanagerapp;

import static android.text.TextUtils.replace;

import android.content.Intent;
import android.icu.text.Replaceable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mego.Calendar;
import com.example.mego.MainActivity;
import com.example.mego.MainPage;
import com.example.mego.NotesMain;
import com.example.mego.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.DrawerLayoutUtils;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;
    private FirebaseAuth auth;
    //Fragment
    private DashboardFragment dashboardFragment;
    private IncomeFragment incomeFragment;
    private ExpenseFragment expenseFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("MEGO");
        setSupportActionBar(toolbar);

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        frameLayout = findViewById(R.id.main_frame);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        menu.findItem(R.id.nav_home).setVisible(true);
        menu.findItem(R.id.nav_calendar).setVisible(true);
        menu.findItem(R.id.nav_list).setVisible(true);
        menu.findItem(R.id.nav_savings).setVisible(false);
        menu.findItem(R.id.nav_exp).setVisible(false);

        dashboardFragment = new DashboardFragment();
        incomeFragment = new IncomeFragment();
        expenseFragment = new ExpenseFragment();

        setFragment((dashboardFragment));
        //Bottom navigation do not fiddle or il diddle
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.dashboard) {
                    setFragment(dashboardFragment);
                    bottomNavigationView.setItemBackgroundResource(R.color.Fragment);
                    return true;
                } else if (itemId == R.id.income) {
                    setFragment(incomeFragment);
                    bottomNavigationView.setItemBackgroundResource(R.color.FragmentIncome);
                    return true;
                } else if (itemId == R.id.expense) {
                    setFragment(expenseFragment);
                    bottomNavigationView.setItemBackgroundResource(R.color.FragmentExpense);
                    return true;
                } else {
                    return false;
                }
            }
        });

    }

    private void setFragment(Fragment fragment) {

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);

        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }


    public void displaySelectedListener(int itemId) {
        Fragment fragment = null;

        if (itemId == R.id.dashboard) {
            fragment = new DashboardFragment();
        } else if (itemId == R.id.income) {
            fragment = new IncomeFragment();
        } else if (itemId == R.id.expense) {
            fragment = new ExpenseFragment();
        }

        if (fragment != null) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_frame, fragment);
            ft.commit();

        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_logout){
            auth.signOut();
            SignOut();
        } else if (item.getItemId() == R.id.nav_home) {
            Intent intent = new Intent(Home.this, MainPage.class);
            startActivity(intent);
            finish();
        } else if (item.getItemId() == R.id.nav_list) {
            Intent intent = new Intent(Home.this, NotesMain.class);
            startActivity(intent);
            finish();
        }else if (item.getItemId() == R.id.nav_share) {
            Toast.makeText(Home.this,"Thank you for Sharing!",Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.nav_rate) {
            Toast.makeText(Home.this,"Thank you for Rating Us!",Toast.LENGTH_SHORT).show();
        }else if (item.getItemId() == R.id.nav_list) {
            Intent intent = new Intent(Home.this, NotesMain.class);
            startActivity(intent);
            finish();
        }else if (item.getItemId() == R.id.nav_calendar) {
            Intent intent = new Intent(Home.this, Calendar.class);
            startActivity(intent);
            finish();
        }
        return true;
    }
    private void SignOut() {
        Intent intent = new Intent(Home.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}