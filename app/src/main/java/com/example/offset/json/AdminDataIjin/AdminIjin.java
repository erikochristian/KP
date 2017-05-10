package com.example.offset.json.AdminDataIjin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.offset.json.Other.AdminMain;
import com.example.offset.json.Other.Login_Activity;
import com.example.offset.json.R;

public class AdminIjin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_ijin);
    }

    //button search clicked
    public void search_ijin(View view){
        Intent intent = new Intent(AdminIjin.this, AdminSearchIzin.class);
        intent.putExtra("crud", "s");
        intent.putExtra("user", "admin");
        startActivity(intent);
        this.finish();
    }

    //button delete clicked
    public void delete_ijin(View view){
        Intent intent = new Intent(AdminIjin.this, AdminSearchIzin.class);
        intent.putExtra("crud", "d");
        intent.putExtra("user", "admin");
        startActivity(intent);
        this.finish();
    }

    //button insert clicked
    public void insert_ijin(View view){
        Intent intent = new Intent(AdminIjin.this, AdminInsertIjin.class);
        startActivity(intent);
        this.finish();
    }

    //button update clicked
    public void update_ijin(View view){
        Intent intent = new Intent(AdminIjin.this, AdminSearchIzin.class);
        intent.putExtra("crud", "u");
        intent.putExtra("user", "admin");
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AdminIjin.this, AdminMain.class);
        startActivity(i);
        finish();
    }

    public void logout(View view){
        Intent i = new Intent(AdminIjin.this, Login_Activity.class);
        startActivity(i);
        AdminIjin.this.finish();
    }
}
