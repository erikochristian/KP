package com.example.offset.json.Other;

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
import com.example.offset.json.R;
import com.example.offset.json.User.UserMain;

import java.util.Calendar;

public class SearchPayroll extends AppCompatActivity {
    EditText t_nik, t_tanggal_dari, t_tanggal_sampai;
    TextView t_welcome;
    Button btn_search;
    String nik = "", tgl_dari = "", tgl_sampai = "", user="";
    private Calendar calendar;
    private int year, month, day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_payroll);

        //initialize component
        t_nik = (EditText) findViewById(R.id.text_nik_payroll);
        t_tanggal_dari = (EditText) findViewById(R.id.txt_tgl_dari_payroll);
        t_tanggal_sampai = (EditText) findViewById(R.id.txt_tgl_sampai_payroll);
        btn_search = (Button) findViewById(R.id.button_search_payroll);

        //get data from intent
        user = getIntent().getStringExtra("user");

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
        } else if (id == 998) {
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
            showDate(arg1, arg2 + 1, arg3, 1);
        }
    };

    private DatePickerDialog.OnDateSetListener myDateListener2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2 + 1, arg3, 2);
        }
    };

    private void showDate(int year, int month, int day, int choose) {
        if (choose == 1) {
            t_tanggal_dari.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-").append(day));
        } else {
            t_tanggal_sampai.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-").append(day));
        }
    }

    public void search_payroll(View view) {
        nik = t_nik.getText()+"";
        tgl_dari = t_tanggal_dari.getText()+"";
        tgl_sampai = t_tanggal_sampai.getText()+"";

        if (t_nik.getText().length() == 0|| t_tanggal_dari.getText().length() == 0 || t_tanggal_sampai.getText().length() == 0){
            Toast.makeText(SearchPayroll.this,"Data tidak lengkap !",Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.text_nik_payroll));
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_tgl_dari_payroll));
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_tgl_sampai_payroll));
        }
        else{
            Intent i = new Intent(SearchPayroll.this, RecapPayroll.class);
            i.putExtra("nik",nik);
            i.putExtra("tgl1",tgl_dari);
            i.putExtra("tgl2",tgl_sampai);
            i.putExtra("user",user);
            startActivity(i);
            t_nik.setText(""); t_tanggal_dari.setText(""); t_tanggal_sampai.setText("");
            SearchPayroll.this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (user.equals("admin")){
            Intent i = new Intent(SearchPayroll.this, AdminMain.class);
            startActivity(i);
            finish();
        }
        else{
            Intent i = new Intent(SearchPayroll.this, UserMain.class);
            i.putExtra("user",user);
            startActivity(i);
            finish();
        }
    }

    public void logout(View view){
        Intent i = new Intent(SearchPayroll.this, Login_Activity.class);
        startActivity(i);
        SearchPayroll.this.finish();
    }

}
