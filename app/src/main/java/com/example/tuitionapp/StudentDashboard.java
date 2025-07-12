package com.example.tuitionapp;

import android.content.Intent;
import android.os.Bundle;
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

public class StudentDashboard extends AppCompatActivity
implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.student_navigation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.stud_nav), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.stud_nav);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StudentDashboardFragment()).commit();
            navigationView.setCheckedItem(R.id.stud_dashboard);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.stud_nav_dashboard) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StudentDashboardFragment()).commit();
        } else if (id == R.id.stud_nav_materials) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StudentMaterialFragment()).commit();
        } else if (id == R.id.stud_nav_attendance) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StudentAttendanceFragment()).commit();
        } else if (id == R.id.stud_nav_results) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StudentResultsFragment()).commit();
        } else if (id == R.id.stud_nav_assignments) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StudentAssignmentsFragment()).commit();
        } else if (id == R.id.stud_nav_qr) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StudentQRFragment()).commit();
        } else if (id == R.id.stud_nav_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new StudentProfileFragment()).commit();
        } else if (id == R.id.stud_nav_logout){
            Intent intent = new Intent(this, AdminLogin.class);
            startActivity(intent);
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