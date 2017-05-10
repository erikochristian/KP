package com.example.offset.json.AdminAbsen;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.offset.json.Other.Login_Activity;
import com.example.offset.json.R;
import com.example.offset.json.model.KodeGoljam;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateAbsen extends AppCompatActivity {
    String searchby="",nik="",tgl_dari="",tgl_sampai="";
    String NIK_ABSEN = "-", SFT = "-", TMSK = "-", TKLR = "-", JMSK = "0", JKLR = "0", TR = "0", TR2 = "0";
    String MESIN = "-", UPDT = "-", GOLJAM = "-", SSL = "0", JKLR2 = "0";
    EditText t_NIK, t_SFT, t_TMSK, t_TKLR, t_JMSK, t_JKLR, t_TR, t_TR2, t_MESIN, t_UPDT, t_GOLJAM, t_SSL, t_JKLR2;
    Spinner spinner_sft, spinner_goljam;
    private String array_spinner_sft[];
    private static String ENDPOINT;
    private static String ENDPOINT_goljam ;
    List<String> list_spin_goljam;

    private RequestQueue requestQueue;
    private Gson gson;
    private Calendar calendar;
    private int year, month, day;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_absen);

        //link to web service
        ENDPOINT = "http://"+getResources().getString(R.string.ip)+"/KP//updateAbsen";
        ENDPOINT_goljam = "http://"+getResources().getString(R.string.ip)+"/KP/cekGoljam";

        //requirement volley, gson
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        nik = getIntent().getStringExtra("nik");
        tgl_dari = getIntent().getStringExtra("tgl_dari");
        tgl_sampai = getIntent().getStringExtra("tgl_sampai");
        NIK_ABSEN = getIntent().getStringExtra("NIK");
        SFT = getIntent().getStringExtra("SFT");
        TMSK = getIntent().getStringExtra("TMSK");
        TKLR = getIntent().getStringExtra("TKLR");
        JMSK = getIntent().getStringExtra("JMSK");
        JKLR = getIntent().getStringExtra("JKLR");
        TR = getIntent().getStringExtra("TR");
        TR2 = getIntent().getStringExtra("TR2");
        MESIN = getIntent().getStringExtra("MESIN");
        GOLJAM = getIntent().getStringExtra("GOLJAM");
        SSL = getIntent().getStringExtra("SSL");
        JKLR2 = getIntent().getStringExtra("JKLR2");

        t_NIK = (EditText) findViewById(R.id.txt_nik_absen);
        t_TMSK = (EditText) findViewById(R.id.txt_tmsk_absen);
        t_TKLR = (EditText) findViewById(R.id.txt_tklr_absen);
        t_JMSK = (EditText) findViewById(R.id.txt_jmsk_absen);
        t_JKLR = (EditText) findViewById(R.id.txt_jklr_absen);
        t_TR = (EditText) findViewById(R.id.txt_tr_absen);
        t_TR2 = (EditText) findViewById(R.id.txt_tr2_absen);
        t_MESIN = (EditText) findViewById(R.id.txt_mesin_absen);
        t_SSL = (EditText) findViewById(R.id.txt_ssl_absen);
        t_JKLR2 = (EditText) findViewById(R.id.txt_jklr2_absen);
        spinner_sft = (Spinner) findViewById(R.id.spinner_sft);
        spinner_goljam = (Spinner) findViewById(R.id.spinner_goljam);

        t_NIK.setText(NIK_ABSEN);
        t_TMSK.setText(TMSK);
        t_TKLR.setText(TKLR);
        t_JMSK.setText(JMSK);
        t_JKLR.setText(JKLR);
        t_TR.setText(TR);
        t_TR2.setText(TR2);
        t_MESIN.setText(MESIN);
        t_SSL.setText(SSL);
        t_JKLR2.setText(JKLR2);

        //Spinner Shift
        array_spinner_sft =new String[3];
        array_spinner_sft[0]="1";
        array_spinner_sft[1]="2";
        array_spinner_sft[2]="3";
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, array_spinner_sft);
        spinner_sft.setAdapter(adapter);
        spinner_sft.setSelection(Integer.parseInt(SFT)-1);



        //spinner goljam
        list_spin_goljam = new ArrayList<String>();
        fetchPosts_goljam();

        //calender requirement
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        //pilih tanggal clicked, showing datepicker dialog
        t_TMSK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
            }

        });

        t_TKLR.setOnClickListener(new View.OnClickListener() {
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
            t_TMSK.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-").append(day));
        }
        else{
            t_TKLR.setText(new StringBuilder().append(year).append("-")
                    .append(month).append("-").append(day));
        }
    }

    public void update_absen(View view) throws ParseException {

        //get data from edittext
        NIK_ABSEN = t_NIK.getText()+"";
        SFT = spinner_sft.getSelectedItem()+"";
        TMSK = t_TMSK.getText()+"";
        TKLR = t_TKLR.getText()+"";
        JMSK = t_JMSK.getText()+"";
        JKLR = t_JKLR.getText()+"";
        GOLJAM = spinner_goljam.getSelectedItem()+"";


        if(t_TR.getText().length()==0){
            TR = "-";
        }
        else{
            TR = t_TR.getText()+"";

        }
        if(t_TR2.getText().length()==0){
            TR2 = "-";
        }
        else{
            TR2 = t_TR2.getText()+"";

        }
        if(t_SSL.getText().length()==0){
            SSL = "-";
        }
        else{
            SSL = t_SSL.getText()+"";

        }
        if(t_JKLR2.getText().length()==0){
            JKLR2 = "-";
        }
        else{
            JKLR2 = t_JKLR2.getText()+"";
        }

        if (t_NIK.getText().length() == 0|| t_TMSK.getText().length() == 0|| t_TKLR.getText().length() == 0 || t_JMSK.getText().length() == 0 || t_JKLR.getText().length() == 0 ) {
            Toast.makeText(UpdateAbsen.this, "NIK, tanggal masuk, tanggal keluar, jam masuk dan jam keluar tidak boleh kosong", Toast.LENGTH_LONG).show();
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_nik_absen));
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_tmsk_absen));
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_tklr_absen));
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_jmsk_absen));
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_jklr_absen));
        }

        else{

            new MaterialDialog.Builder(view.getContext())
                    .title("Konfirmasi")
                    .content("Ubah data absen?")
                    .positiveText("Ya")
                    .negativeText("Tidak")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            fetchPosts();
                        }

                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
        }

    }


    //update procedure
    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT, onPostsLoaded, onPostsError){
            @Override
            protected Map<String, String> getParams() {
                //sending data to web service
                Map<String, String> params = new HashMap<>();
                params.put("nik", NIK_ABSEN);
                params.put("sft", SFT);
                params.put("tmsk", TMSK);
                params.put("tklr", TKLR);
                params.put("jmsk", JMSK);
                params.put("jklr", JKLR);
                params.put("tr", TR);
                params.put("tr2", TR2);
                params.put("mesin", MESIN);
                params.put("updt", UPDT);
                params.put("goljam", GOLJAM);
                params.put("ssl", SSL);
                params.put("jklr2", JKLR2);
                return params;
            }
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Toast.makeText(UpdateAbsen.this, "Data absen berhasil ditambahkan", Toast.LENGTH_LONG).show();
            Intent back = new Intent(UpdateAbsen.this, AdminAbsen.class);
            startActivity(back);
            UpdateAbsen.this.finish();
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
            Toast.makeText(UpdateAbsen.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();

        }
    };

    private void fetchPosts_goljam() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT_goljam, onPostsLoaded_goljam, onPostsError_goljam){
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded_goljam = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            //Get JsonArray
            //fetch data onto list bagian
            List<KodeGoljam> goljam = Arrays.asList(gson.fromJson(response, KodeGoljam[].class));

            //set data to spinner
            list_spin_goljam.clear();

            for (KodeGoljam b : goljam) {
                list_spin_goljam.add(b.getKode());
            }
            ArrayAdapter adapter_goljam = new ArrayAdapter(UpdateAbsen.this, android.R.layout.simple_spinner_item, list_spin_goljam);
            spinner_goljam.setAdapter(adapter_goljam);

            if (GOLJAM.equals("COS")){
                spinner_goljam.setSelection(1);
            }
            else{
                spinner_goljam.setSelection(0);
            }

        }
    };

    private final Response.ErrorListener onPostsError_goljam = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
            Toast.makeText(UpdateAbsen.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(UpdateAbsen.this, SearchUpdateAbsen.class);
        i.putExtra("nik",nik);
        i.putExtra("tgl_dari",tgl_dari);
        i.putExtra("tgl_sampai",tgl_sampai);
        startActivity(i);
        finish();
    }

    public void logout(View view){
        Intent i = new Intent(UpdateAbsen.this, Login_Activity.class);
        startActivity(i);
        UpdateAbsen.this.finish();
    }
}
