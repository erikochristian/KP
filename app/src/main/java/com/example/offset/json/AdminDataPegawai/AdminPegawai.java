package com.example.offset.json.AdminDataPegawai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.offset.json.Other.AdminMain;
import com.example.offset.json.Other.Login_Activity;
import com.example.offset.json.R;

public class AdminPegawai extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_pegawai);
    }

    //button clicked search
    public void search(View view){
        Intent intent = new Intent(AdminPegawai.this, MainActivity.class);
        intent.putExtra("crud","s");
        intent.putExtra("user","admin");
        startActivity(intent);
        this.finish();
    }

    //button clicked delete
    public void delete(View view){
        Intent intent = new Intent(AdminPegawai.this, MainActivity.class);
        intent.putExtra("crud","d");
        intent.putExtra("user","admin");
        startActivity(intent);
        this.finish();
    }

    //button clicked insert
    public void insert(View view){
        Intent intent = new Intent(AdminPegawai.this, AdminInsertPegawai.class);
        startActivity(intent);
        this.finish();
    }

    //button clicked update
    public void update(View view){
        Intent intent = new Intent(AdminPegawai.this, MainActivity.class);
        intent.putExtra("crud","u");
        intent.putExtra("user","admin");
        startActivity(intent);
        this.finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AdminPegawai.this, AdminMain.class);
        startActivity(i);
        finish();
    }

    public void logout(View view){
        Intent i = new Intent(AdminPegawai.this, Login_Activity.class);
        startActivity(i);
        AdminPegawai.this.finish();
    }

}
