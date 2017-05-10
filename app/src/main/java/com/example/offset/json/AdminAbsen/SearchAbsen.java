package com.example.offset.json.AdminAbsen;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.offset.json.Other.Login_Activity;
import com.example.offset.json.R;

import java.util.Calendar;

public class SearchAbsen extends AppCompatActivity {

    EditText t_nik, t_tanggal_dari, t_tanggal_sampai;
    TextView t_welcome;
    Button btn_search;
    String nik ="",tgl_dari="",tgl_sampai="";
    private Calendar calendar;
    private int year, month, day;
    String crud ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_absen);

        //initialize component
        t_nik = (EditText) findViewById(R.id.text_nik_absen);
        t_tanggal_dari = (EditText) findViewById(R.id.txt_tgl_dari_absen);
        t_tanggal_sampai = (EditText) findViewById(R.id.txt_tgl_sampai_absen);
        btn_search = (Button) findViewById(R.id.button_search_absen);
        t_welcome = (TextView) findViewById(R.id.textView) ;

        crud = getIntent().getStringExtra("crud");

        if (crud.equals("d")){
            t_welcome.setText("HAPUS DATA ABSEN");
        }
        else if(crud.equals("u")){
            t_welcome.setText("UBAH DATA ABSEN");
        }
        else{
        }

        //calender requirement
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        //pilih tanggal clicked, showing datepicker dialog
        t_tanggal_dari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
            }

        });

        t_tanggal_sampai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(998);
            }

        });

    }

    //Datepicker dialog
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        else if (id == 998){
            return new DatePickerDialog(this, myDateListener2, year, month, day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3, 1);
        }
    };

    private DatePickerDialog.OnDateSetListener myDateListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3, 2);
        }
    };
    private void showDate(int year, int month, int day, int choose) {
        if(choose==1){
            t_tanggal_dari.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-").append(day));
        }
        else{
            t_tanggal_sampai.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-").append(day));
        }
    }

    public void search_absen(View view) {

        nik = t_nik.getText()+"";
        tgl_dari = t_tanggal_dari.getText()+"";
        tgl_sampai = t_tanggal_sampai.getText()+"";

        if (t_nik.getText().length()==0 && t_tanggal_dari.getText().length()==0 && t_tanggal_sampai.getText().length()==0){
            Toast.makeText(SearchAbsen.this, "Data tidak lengkap! Cek kembali", Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.text_nik_absen));
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_tgl_dari_absen));
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_tgl_sampai_absen));
        }
        else{
            if(crud.equals("d")){
                Intent intent = new Intent(SearchAbsen.this, DeleteAbsen.class);
                intent.putExtra("nik",nik);
                intent.putExtra("tgl_dari",tgl_dari);
                intent.putExtra("tgl_sampai",tgl_sampai);
                startActivity(intent);
                t_tanggal_dari.setText("");
                t_tanggal_sampai.setText("");
                t_nik.setText("");
                this.finish();
            }
            else if(crud.equals("u")){
                Intent intent = new Intent(SearchAbsen.this, SearchUpdateAbsen.class);
                intent.putExtra("nik",nik);
                intent.putExtra("tgl_dari",tgl_dari);
                intent.putExtra("tgl_sampai",tgl_sampai);
                startActivity(intent);
                t_tanggal_dari.setText("");
                t_tanggal_sampai.setText("");
                t_nik.setText("");
                this.finish();
            }
            else{
                Intent intent = new Intent(SearchAbsen.this, PageViewAbsen.class);
                intent.putExtra("nik",nik);
                intent.putExtra("tgl_dari",tgl_dari);
                intent.putExtra("tgl_sampai",tgl_sampai);
                startActivity(intent);
                t_tanggal_dari.setText("");
                t_tanggal_sampai.setText("");
                t_nik.setText("");
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(SearchAbsen.this, AdminAbsen.class);
        startActivity(i);
        finish();
    }

    public void logout(View view){
        Intent i = new Intent(SearchAbsen.this, Login_Activity.class);
        startActivity(i);
        SearchAbsen.this.finish();
    }
}
