package com.example.offset.json.AdminLiburNasional;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.offset.json.Other.Login_Activity;
import com.example.offset.json.Other.RegisterActivity;
import com.example.offset.json.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class InsertLibur extends AppCompatActivity {

    private static String ENDPOINT;

    private RequestQueue requestQueue;
    private Gson gson;

    EditText edt_tanggal, edt_keterangan;
    private Calendar calendar;
    private int year, month, day;
    String tgl_insert, ket_insert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_libur);

        //link to web service
        ENDPOINT = "http://"+getResources().getString(R.string.ip)+"/KP/insertLibur";

        edt_keterangan = (EditText) findViewById(R.id.text_keterangan_libur);
        edt_tanggal = (EditText) findViewById(R.id.text_tanggal_libur);

        //calender
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        //requirement volley, gson
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        edt_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
            }

        });

    }

    public void  insert(View view) {
        tgl_insert = edt_tanggal.getText() + "";
        ket_insert = edt_keterangan.getText() + "";

        if (edt_tanggal.getText().length() == 0) {
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.text_tanggal_libur));
            Toast.makeText(this, "Data Tidak Lengkap. Cek Kembali!!", Toast.LENGTH_SHORT);
        } else if (edt_keterangan.getText().length() == 0) {
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.text_keterangan_libur));
            Toast.makeText(this, "Data Tidak Lengkap. Cek Kembali!!", Toast.LENGTH_SHORT);
        } else {
            fetchPosts();
        }
        if (tgl_insert != null || ket_insert != null) {

        }
    }
    //setdate
        //Datepicker dialog
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, year, month, day);
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
    private void showDate(int year, int month, int day, int choose) {

        edt_tanggal.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }

    //insertion procedure
    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT, onPostsLoaded, onPostsError){
            @Override
            protected Map<String, String> getParams() {
                //sending data to web service
                Map<String, String> params = new HashMap<>();
                params.put("tgl", tgl_insert);
                params.put("ket", ket_insert);
                return params;
            }
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Intent m = new Intent(InsertLibur.this, ViewLibur.class);
            startActivity(m);
            InsertLibur.this.finish();
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
            Toast.makeText(InsertLibur.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(InsertLibur.this, ViewLibur.class);
        startActivity(i);
        finish();
    }

    public void logout(View view){
        Intent i = new Intent(InsertLibur.this, Login_Activity.class);
        startActivity(i);
        InsertLibur.this.finish();
    }
}
