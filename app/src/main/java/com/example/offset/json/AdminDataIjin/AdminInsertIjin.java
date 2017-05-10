package com.example.offset.json.AdminDataIjin;

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
import com.example.offset.json.AdminDataPegawai.AdminInsertPegawai;
import com.example.offset.json.Other.Login_Activity;
import com.example.offset.json.Other.RegisterActivity;
import com.example.offset.json.R;
import com.example.offset.json.model.JenisIjin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AdminInsertIjin extends AppCompatActivity {
    private static String ENDPOINT_jenis;
    private static String ENDPOINT;

    private RequestQueue requestQueue;
    private Gson gson;
    int selisih = 0;
    EditText t_nik, t_keterangan;
    Spinner spin_jenis;
    TextView text_tanggal_dari, text_tanggal_sampai;
    private Calendar calendar;
    private int year, month, day;
    List<String> list_spin_jenis;
    String nik, tanggal, keterangan, id_jenis, tanggal2, tanggalfix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_insert_ijin);

        //link to web service
        ENDPOINT = "http://"+getResources().getString(R.string.ip)+"/KP/insertIjin";
        ENDPOINT_jenis = "http://"+getResources().getString(R.string.ip)+"/KP/AllJenisIjin";

        //initialize component
        t_nik = (EditText) findViewById(R.id.txt_nik);
        t_keterangan = (EditText) findViewById(R.id.txt_keterangan);
        spin_jenis = (Spinner) findViewById(R.id.spinner);
        text_tanggal_dari = (TextView) findViewById(R.id.txt_tgl_dari);
        text_tanggal_sampai = (TextView) findViewById(R.id.txt_tgl_sampai);

        //requirement volley, gson
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        //calender requirement
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        //fetch jenis_ijin to spinner jenis
        list_spin_jenis = new ArrayList<String>();
        fetchPosts_jenis();

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

    //insert button clicked
    public void insert(View view){

        //get data from edittext and spinner
        nik = t_nik.getText()+"";
        tanggal = text_tanggal_dari.getText()+"";
        tanggal2 = text_tanggal_sampai.getText()+"";
        keterangan = t_keterangan.getText()+"";
        id_jenis = (spin_jenis.getSelectedItemPosition()+1)+"";

        //checking null value
        if (t_keterangan.getText().length()==0){
            keterangan="-";
        }

        if(t_nik.getText().length()==0){
            Toast.makeText(AdminInsertIjin.this, "Data Tidak Lengkap. Cek Kembali!", Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_nik));
        }
        else if(text_tanggal_dari.getText().length()==0){
            Toast.makeText(AdminInsertIjin.this, "Data Tidak Lengkap. Cek Kembali!", Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_tgl_dari));
        }
        else if(text_tanggal_sampai.getText().length()==0){
            Toast.makeText(AdminInsertIjin.this, "Data Tidak Lengkap. Cek Kembali!", Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_tgl_sampai));
        }
        else{
            new MaterialDialog.Builder(view.getContext())
                    .title("Konfirmasi")
                    .content("Tambahkan data ijin dengan NIK = "+nik+" ?")
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

    //fetching jenis ijin
    private void fetchPosts_jenis() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT_jenis, onPostsLoaded_jenis, onPostsError_jenis){
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded_jenis = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {

            //Getting all jenis ijin to list jenis
            List<JenisIjin> jenis = Arrays.asList(gson.fromJson(response, JenisIjin[].class));
            Log.i("MainActivity", jenis.size() + " posts loaded.");

            //get deskripsi jenis ijin onto list
            list_spin_jenis.clear();
            for (JenisIjin b : jenis) {
                list_spin_jenis.add(b.getDESKRIPSI());
            }

            //using the list for spinner
            ArrayAdapter adapter_jenis = new ArrayAdapter(AdminInsertIjin.this, android.R.layout.simple_spinner_item, list_spin_jenis);
            spin_jenis.setAdapter(adapter_jenis);
        }
    };

    private final Response.ErrorListener onPostsError_jenis = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
            Toast.makeText(AdminInsertIjin.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();
        }
    };

    //insertion procedure
    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT, onPostsLoaded, onPostsError){
            @Override
            protected Map<String, String> getParams() {
                //sending data to web service
                Map<String, String> params = new HashMap<>();
                params.put("nik", nik);
                params.put("tgl", tanggal);
                params.put("jenis", id_jenis);
                params.put("keterangan", keterangan);
                params.put("tgl2",tanggal2);
                return params;
            }
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Toast.makeText(AdminInsertIjin.this, "Ijin pegawai dengan nik "+nik+" berhasil ditambahkan", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(AdminInsertIjin.this, AdminIjin.class);
            startActivity(i);
            AdminInsertIjin.this.finish();
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
            Toast.makeText(AdminInsertIjin.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AdminInsertIjin.this, AdminIjin.class);
        i.putExtra("crud","u");
        startActivity(i);
        finish();
    }

    public void logout(View view){
        Intent i = new Intent(AdminInsertIjin.this, Login_Activity.class);
        startActivity(i);
        AdminInsertIjin.this.finish();
    }
}
