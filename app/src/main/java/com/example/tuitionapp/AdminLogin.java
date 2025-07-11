package com.example.tuitionapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminLogin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.admin_login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    //Go to Admin Dashboard
    public void DashboardAdmin (View view){
        Intent intent = new Intent(this, AdminDashboard.class);
        startActivity(intent);
    }

    //Go back to Welcome screen
    public void Welcome (View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //Go to SignUp page
    public void SignUp (View view){
        Intent intent = new Intent(this, AdminRegister.class);
        startActivity(intent);
    }
}