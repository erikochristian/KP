package com.example.offset.json.User;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.offset.json.Other.Login_Activity;
import com.example.offset.json.R;

public class UserAbsenMain extends AppCompatActivity {

    String id="", user="";
    TextView text_welcome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_absen_main);

        text_welcome = (TextView) findViewById(R.id.text_welcome);
        user = getIntent().getStringExtra("user");
        text_welcome.setText("Selamat Datang "+user);
    }

    public void absen_reguler(View view) {
        Intent intent = new Intent(UserAbsenMain.this, SearchAbsenUser.class);
        intent.putExtra("user",user);
        intent.putExtra("fungsi","r");
        startActivity(intent);
        this.finish();
    }

    public void absen_lembur(View view) {
        Intent intent = new Intent(UserAbsenMain.this, SearchAbsenUser.class);
        intent.putExtra("user",user);
        intent.putExtra("fungsi","l");
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(UserAbsenMain.this, UserMain.class);
        i.putExtra("user",user);
        startActivity(i);
        finish();
    }


    public void logout(View view){
        Intent i = new Intent(UserAbsenMain.this, Login_Activity.class);
        startActivity(i);
        UserAbsenMain.this.finish();
    }
}
