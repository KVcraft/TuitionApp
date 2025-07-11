package com.example.tuitionapp;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;


import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class AdminDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.admin_nav_dash), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();
            navigationView.setCheckedItem(R.id.admin_dash);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item){
        int id = item.getItemId();

        if (id == R.id.admin_nav_dash) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();
        } else if (id == R.id.admin_nav_students) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StudentsFragment()).commit();
        } else if (id == R.id.admin_nav_teachers) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TeachersFragment()).commit();
        } else if (id == R.id.admin_nav_attendance) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AttendanceFragment()).commit();
        } else if (id == R.id.admin_nav_results) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ResultsFragment()).commit();
        } else if (id == R.id.admin_nav_reports) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReportsFragment()).commit();
        } else if (id == R.id.admin_nav_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AdminProfileFragment()).commit();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }
}