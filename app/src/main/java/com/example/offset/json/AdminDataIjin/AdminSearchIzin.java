package com.example.offset.json.AdminDataIjin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.example.offset.json.Other.AdminMain;
import com.example.offset.json.Other.Login_Activity;
import com.example.offset.json.R;
import com.example.offset.json.User.UserMain;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Calendar;

public class AdminSearchIzin extends AppCompatActivity {
    Button btn_src, btn_barcode;
    EditText edit_nik;
    String nik="";
    String crud = "";
    String tgl_dari = "";
    String tgl_sampai = "";
    String user="";
    TextView text_tanggal_dari, text_tanggal_sampai;
    TextView t_welcome, t_title;
    private Calendar calendar;
    private int year, month, day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_search_izin);

        //initialize component
        btn_src = (Button) findViewById(R.id.button);
        edit_nik = (EditText) findViewById(R.id.editText);
        t_welcome = (TextView) findViewById(R.id.text_welcome);
        t_title = (TextView) findViewById(R.id.textView);
        crud = getIntent().getStringExtra("crud");
        user = getIntent().getStringExtra("user");
        t_welcome.setText("Selamat Datang "+user);
        text_tanggal_dari = (TextView) findViewById(R.id.txt_tgl_dari);
        text_tanggal_sampai = (TextView) findViewById(R.id.txt_tgl_sampai);


        //Tittle based on action (s/d/u)
        if (crud.equals("s")){
            t_title.setText("CARI IZIN");
        }
        else if (crud.equals("d")){
            t_title.setText("HAPUS IZIN");
        }
        else if (crud.equals("u")){
            t_title.setText("UBAH IZIN");
        }

        //calender requirement
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        //pilih tanggal clicked, showing datepicker dialog
        text_tanggal_dari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
            }

        });

        text_tanggal_sampai.setOnClickListener(new View.OnClickListener() {
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
            text_tanggal_dari.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-").append(day));
        }
        else{
            text_tanggal_sampai.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-").append(day));
        }
    }

    //button submit clicked
    public void submit(View view){
        //get nik from edittext
        nik = edit_nik.getText()+"";
        tgl_dari = text_tanggal_dari.getText()+"";
        tgl_sampai = text_tanggal_sampai.getText()+"";


        if(edit_nik.getText().length()==0){
            Toast.makeText(AdminSearchIzin.this, "NIK tidak boleh kosong", Toast.LENGTH_LONG).show();
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.editText));
        }
        else if((tgl_dari.equals("") && !tgl_sampai.equals("")) || (!tgl_dari.equals("") && tgl_sampai.equals(""))){
            Toast.makeText(AdminSearchIzin.this, "Tanggal harus diisi", Toast.LENGTH_LONG).show();
        }
        else if (tgl_dari.equals("") && tgl_sampai.equals("")){

            //go to search ijin page
            if (crud.equals("s")){
                Intent intent = new Intent(AdminSearchIzin.this, PageViewIzin.class);
                intent.putExtra("nik",nik);
                intent.putExtra("tgl","no");
                intent.putExtra("user",user);
                startActivity(intent);
                edit_nik.setText("");
                text_tanggal_sampai.setText("");
                text_tanggal_dari.setText("");
                this.finish();
            }
            //go to delete ijin page
            else if(crud.equals("d")){
                Intent intent = new Intent(AdminSearchIzin.this, AdminDeleteIjin.class);
                intent.putExtra("nik",nik);
                intent.putExtra("tgl","no");
                intent.putExtra("user",user);
                startActivity(intent);
                edit_nik.setText("");
                text_tanggal_sampai.setText("");
                text_tanggal_dari.setText("");
                this.finish();
            }
            //go to update ijin page
            else if(crud.equals("u")){
                Intent intent = new Intent(AdminSearchIzin.this, AdminSearchUpdateIjin.class);
                intent.putExtra("nik",nik);
                intent.putExtra("tgl","no");
                intent.putExtra("user",user);
                startActivity(intent);
                edit_nik.setText("");
                text_tanggal_sampai.setText("");
                text_tanggal_dari.setText("");
                this.finish();
            }
        }
        else {
            //go to search ijin page
            if (crud.equals("s")){
                Intent intent = new Intent(AdminSearchIzin.this, PageViewIzin.class);
                intent.putExtra("nik",nik);
                intent.putExtra("user",user);
                intent.putExtra("tgl","yes");
                intent.putExtra("tgl_dari",tgl_dari);
                intent.putExtra("tgl_sampai",tgl_sampai);
                startActivity(intent);
                edit_nik.setText("");
                text_tanggal_sampai.setText("");
                text_tanggal_dari.setText("");
                this.finish();
            }
            //go to delete ijin page
            else if(crud.equals("d")){
                Intent intent = new Intent(AdminSearchIzin.this, AdminDeleteIjin.class);
                intent.putExtra("nik",nik);
                intent.putExtra("tgl","yes");
                intent.putExtra("tgl_dari",tgl_dari);
                intent.putExtra("tgl_sampai",tgl_sampai);
                intent.putExtra("user",user);
                startActivity(intent);
                edit_nik.setText("");
                text_tanggal_sampai.setText("");
                text_tanggal_dari.setText("");
                this.finish();
            }
            //go to update ijin page
            else if(crud.equals("u")){
                Intent intent = new Intent(AdminSearchIzin.this, AdminSearchUpdateIjin.class);
                intent.putExtra("nik",nik);
                intent.putExtra("tgl","yes");
                intent.putExtra("tgl_dari",tgl_dari);
                intent.putExtra("tgl_sampai",tgl_sampai);
                intent.putExtra("user",user);
                startActivity(intent);
                edit_nik.setText("");
                text_tanggal_sampai.setText("");
                text_tanggal_dari.setText("");
                this.finish();
            }
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (user.equals("admin")){
            Intent i = new Intent(AdminSearchIzin.this, AdminIjin.class);
            startActivity(i);
            finish();
        }
        else{
            Intent i = new Intent(AdminSearchIzin.this, UserMain.class);
            i.putExtra("user",user);
            startActivity(i);
            finish();
        }
    }

    public void logout(View view){
        Intent i = new Intent(AdminSearchIzin.this, Login_Activity.class);
        startActivity(i);
        AdminSearchIzin.this.finish();
    }
}
