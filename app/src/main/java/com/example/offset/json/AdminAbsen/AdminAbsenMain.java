package com.example.offset.json.AdminAbsen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.offset.json.Other.AdminMain;
import com.example.offset.json.Other.Login_Activity;
import com.example.offset.json.User.SearchAbsenUser;
import com.example.offset.json.R;

public class AdminAbsenMain extends AppCompatActivity {

    String id="", user="";
    TextView text_welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_absen_main);

        text_welcome = (TextView) findViewById(R.id.text_welcome);
        text_welcome.setText("Selamat Datang Admin");
    }

    //view absen reguler
    public void absen_reguler(View view) {
        Intent intent = new Intent(AdminAbsenMain.this, AdminAbsen.class);
        intent.putExtra("user","admin");
        startActivity(intent);
        this.finish();
    }

    //view absen reguler view absen lembur
    public void absen_lembur(View view) {
        Intent intent = new Intent(AdminAbsenMain.this, SearchAbsenUser.class);
        intent.putExtra("user","admin");
        intent.putExtra("fungsi","l");
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AdminAbsenMain.this, AdminMain.class);
        startActivity(i);
        finish();
    }

    public void logout(View view){
        Intent i = new Intent(AdminAbsenMain.this, Login_Activity.class);
        startActivity(i);
        AdminAbsenMain.this.finish();
    }
}
