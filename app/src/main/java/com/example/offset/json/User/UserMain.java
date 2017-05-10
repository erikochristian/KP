package com.example.offset.json.User;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.offset.json.AdminDataIjin.AdminIjin;
import com.example.offset.json.AdminDataIjin.AdminSearchIzin;
import com.example.offset.json.AdminDataPegawai.MainActivity;
import com.example.offset.json.Other.Login_Activity;
import com.example.offset.json.Other.RegisterActivity;
import com.example.offset.json.Other.SearchPayroll;
import com.example.offset.json.R;

public class UserMain extends AppCompatActivity {

    String id="", user="";
    TextView text_welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        text_welcome = (TextView) findViewById(R.id.text_welcome);
        user = getIntent().getStringExtra("user");
        text_welcome.setText("Selamat Datang "+user);

    }

    public void pegawai(View view) {
        Intent intent = new Intent(UserMain.this, MainActivity.class);
        intent.putExtra("user",user);
        intent.putExtra("crud","s");
        startActivity(intent);
        this.finish();
    }

    public void izin(View view) {
        Intent intent = new Intent(UserMain.this, AdminSearchIzin.class);
        intent.putExtra("user",user);
        intent.putExtra("crud","s");
        startActivity(intent);
        this.finish();
    }

    public void payroll(View view) {
        Intent intent = new Intent(UserMain.this, SearchPayroll.class);
        intent.putExtra("user",user);
        startActivity(intent);
        this.finish();
    }

    public void absen(View view) {
        Intent intent = new Intent(UserMain.this, UserAbsenMain.class);
        intent.putExtra("user",user);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(UserMain.this, Login_Activity.class);
        startActivity(i);
        finish();
    }


    public void logout(View view){
        Intent i = new Intent(UserMain.this, Login_Activity.class);
        startActivity(i);
        UserMain.this.finish();
    }

}
