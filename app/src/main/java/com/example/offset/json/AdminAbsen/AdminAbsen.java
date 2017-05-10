package com.example.offset.json.AdminAbsen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.offset.json.Other.AdminMain;
import com.example.offset.json.Other.Login_Activity;
import com.example.offset.json.Other.RegisterActivity;
import com.example.offset.json.R;

public class AdminAbsen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_absen);
    }

    //button search clicked
    public void search_absen(View view){
        Intent intent = new Intent(AdminAbsen.this, SearchAbsen.class);
        intent.putExtra("id","Admin");
        intent.putExtra("crud", "s");
        startActivity(intent);
        this.finish();
    }

    //button delete clicked
    public void delete_absen(View view){
        Intent intent = new Intent(AdminAbsen.this, SearchAbsen.class);
        intent.putExtra("id","Admin");
        intent.putExtra("crud", "d");
        startActivity(intent);
        this.finish();
    }

    //button update clicked
    public void update_absen(View view){
        Intent intent = new Intent(AdminAbsen.this, SearchAbsen.class);
        intent.putExtra("id","Admin");
        intent.putExtra("crud", "u");
        startActivity(intent);
        this.finish();
    }

    //button delete clicked
    public void insert_absen(View view){
        Intent intent = new Intent(AdminAbsen.this, InsertAbsen.class);
        startActivity(intent);
        this.finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AdminAbsen.this, AdminAbsenMain.class);
        startActivity(i);
        finish();
    }

    public void logout(View view){
        Intent i = new Intent(AdminAbsen.this, Login_Activity.class);
        startActivity(i);
        AdminAbsen.this.finish();
    }

}
