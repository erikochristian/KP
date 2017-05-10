package com.example.offset.json.AdminDataPegawai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.offset.json.Other.AdminMain;
import com.example.offset.json.Other.Login_Activity;
import com.example.offset.json.R;
import com.example.offset.json.User.UserMain;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    // static final String ENDPOINT = "https://kylewbanks.com/rest/posts.json";

    String nik="";
    Button btn_src, btn_barcode;
    EditText edit_nik;
    String user="";
    RadioButton r_nik,r_nama;
    String searchby = "nik";
    String crud="";
    TextView txt_welcome, txt_title;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //getting crud
        crud = getIntent().getStringExtra("crud");
        user = getIntent().getStringExtra("user");

        //initiate components
        txt_title = (TextView) findViewById(R.id.textView);
        txt_welcome = (TextView) findViewById(R.id.text_welcome);
        txt_welcome.setText("Selamat Datang");
        btn_src = (Button) findViewById(R.id.button);
        edit_nik = (EditText) findViewById(R.id.editText);
        r_nik = (RadioButton) findViewById(R.id.radio_searchnik);
        r_nama = (RadioButton) findViewById(R.id.radio_searchnama);
        r_nik.setChecked(true);

        //search/update/delete
        if (crud.equals("s")){
            txt_title.setText("CARI DATA PEGAWAI");
        }
        else if(crud.equals("d")){
            txt_title.setText("HAPUS DATA PEGAWAI");
        }
        else if(crud.equals("u")){
            txt_title.setText("UPDATE DATA PEGAWAI");
        }
    }

    public void submit(View view){
        nik = edit_nik.getText()+"";
        if(searchby.equals("nik")){
            Intent intent = new Intent(MainActivity.this, ListPegawai.class);
            intent.putExtra("cari",nik);
            intent.putExtra("searchby","nik");
            intent.putExtra("crud",crud);
            intent.putExtra("user",user);
            startActivity(intent);
            edit_nik.setText("");
            finish();
        }
        else{
            Intent intent = new Intent(MainActivity.this, ListPegawai.class);
            intent.putExtra("cari",nik);
            intent.putExtra("searchby","nama");
            intent.putExtra("crud",crud);
            intent.putExtra("user",user);
            startActivity(intent);
            edit_nik.setText("");
            finish();
        }
    }

    //radio button jenis kelamin
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio_searchnik:
                if (checked)
                    searchby = "nik";
                    edit_nik.setHint("Input NIK");
                    edit_nik.setInputType(2);
                break;

            case R.id.radio_searchnama:
                if (checked)
                    searchby = "nama";
                    edit_nik.setHint("Input Nama");
                    edit_nik.setInputType(1);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (user.equals("admin")){
            Intent i = new Intent(MainActivity.this, AdminPegawai.class);
            startActivity(i);
            finish();
        }
        else{
            Intent i = new Intent(MainActivity.this, UserMain.class);
            i.putExtra("user",user);
            startActivity(i);
            finish();
        }
    }

    public void logout(View view){
        Intent i = new Intent(MainActivity.this, Login_Activity.class);
        startActivity(i);
        MainActivity.this.finish();
    }
}
