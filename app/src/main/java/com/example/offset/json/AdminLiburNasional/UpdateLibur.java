package com.example.offset.json.AdminLiburNasional;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.example.offset.json.Other.AdminMain;
import com.example.offset.json.Other.Login_Activity;
import com.example.offset.json.Other.RegisterActivity;
import com.example.offset.json.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateLibur extends AppCompatActivity {
    private static String ENDPOINT;

    private RequestQueue requestQueue;
    private Gson gson;

    EditText edt_tanggal, edt_keterangan;
    private Calendar calendar;
    private int year, month, day;
    String tgl_update,tgl_baru, ket_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_libur);

        edt_tanggal = (EditText) findViewById(R.id.text_tanggal_libur);
        edt_keterangan = (EditText) findViewById(R.id.text_keterangan_libur);

        tgl_update = getIntent().getStringExtra("tgl");
        ket_update = getIntent().getStringExtra("ket");

        edt_keterangan.setText(ket_update);
        edt_tanggal.setText(tgl_update);

        //link to web service
        ENDPOINT = "http://"+getResources().getString(R.string.ip)+"/KP/updateLibur";

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

    public void update(View view) {

        tgl_baru = edt_tanggal.getText()+"";
        ket_update = edt_keterangan.getText()+"";

        if (edt_keterangan.getText().length()==0){
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_password));
            Toast.makeText(UpdateLibur.this,"Data tidak lengkap. Cek kembali",Toast.LENGTH_SHORT).show();
        }
        else{
            new MaterialDialog.Builder(view.getContext())
                    .title("Konfirmasi")
                    .content("Update data libur ?")
                    .positiveText("Ya")
                    .negativeText("Tidak")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            fetchPosts_update();
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

    //setdate
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
    }@Override
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
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        edt_tanggal.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }

    //updating procedure
    private void fetchPosts_update() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT, onPostsLoaded_update, onPostsError_update){
            @Override
            protected Map<String, String> getParams() {
                //send data from user to web service
                Map<String, String> params = new HashMap<>();
                params.put("tgl_lama", tgl_update);
                params.put("tgl_baru", tgl_baru);
                params.put("ket", ket_update);
                return params;
            }
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded_update = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            Intent m = new Intent(UpdateLibur.this, ViewLibur.class);
            startActivity(m);
            UpdateLibur.this.finish();
        }
    };

    private final Response.ErrorListener onPostsError_update= new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
            Toast.makeText(UpdateLibur.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(UpdateLibur.this, ViewLibur.class);
        startActivity(i);
        finish();
    }

    public void logout(View view){
        Intent i = new Intent(UpdateLibur.this, Login_Activity.class);
        startActivity(i);
        UpdateLibur.this.finish();
    }
}
