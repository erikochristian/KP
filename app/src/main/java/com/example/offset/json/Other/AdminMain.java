package com.example.offset.json.Other;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.offset.json.AdminAbsen.AdminAbsenMain;
import com.example.offset.json.AdminDataIjin.AdminIjin;
import com.example.offset.json.AdminDataPegawai.AdminPegawai;
import com.example.offset.json.AdminLiburNasional.ViewLibur;
import com.example.offset.json.R;

public class AdminMain extends AppCompatActivity {

    String id="", user="";
    TextView text_welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        text_welcome = (TextView) findViewById(R.id.text_welcome);
    }

    // if not admin, go to Search page only
    public void pegawai(View view) {
        Intent intent = new Intent(AdminMain.this, AdminPegawai.class);
        startActivity(intent);
        this.finish();
    }

    public void izin(View view) {
        Intent intent = new Intent(AdminMain.this, AdminIjin.class);
        startActivity(intent);
        this.finish();
    }

    public void libur(View view) {
        Intent intent = new Intent(AdminMain.this, ViewLibur.class);
        startActivity(intent);
        this.finish();

    }
    public void absen(View view) {
        Intent intent = new Intent(AdminMain.this, AdminAbsenMain.class);
        startActivity(intent);
        this.finish();
    }

    public void payroll(View view) {
        Intent intent = new Intent(AdminMain.this, SearchPayroll.class);
        intent.putExtra("user","admin");
        startActivity(intent);
        this.finish();
    }

    public void akun(View view) {
        Intent intent = new Intent(AdminMain.this, SettingAkun.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AdminMain.this, Login_Activity.class);
        startActivity(i);
        finish();
    }

    public void logout(View view){
        Intent i = new Intent(AdminMain.this, Login_Activity.class);
        startActivity(i);
        AdminMain.this.finish();
    }

}
