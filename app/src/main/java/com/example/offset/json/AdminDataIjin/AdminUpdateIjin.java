package com.example.offset.json.AdminDataIjin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.offset.json.AdminDataPegawai.AdminUpdatePegawai;
import com.example.offset.json.Other.Login_Activity;
import com.example.offset.json.R;
import com.example.offset.json.model.JenisIjin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminUpdateIjin extends AppCompatActivity {
    private static String ENDPOINT_jenis;
    private static String ENDPOINT_update;
    private RequestQueue requestQueue;
    private Gson gson;
    String tgl="";
    String tgl_dari="";
    String tgl_sampai="";

    String nik="",user="";
    String tgl_update="";
    String ket_lama, ket_baru, jenis_lama, jenis_baru;
    Spinner spin_jenis;
    EditText t_keterangan, t_nik, t_tanggal;
    List<String> list_spin_jenis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_update_ijin);

        //link to web service
        ENDPOINT_update = "http://"+getResources().getString(R.string.ip)+"/KP/updateIjin";
        ENDPOINT_jenis = "http://"+getResources().getString(R.string.ip)+"/KP/AllJenisIjin";

        //requirement volley, gson
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        //init other component
        t_nik = (EditText) findViewById(R.id.edt_nik);
        t_tanggal = (EditText) findViewById(R.id.edt_tgl);
        t_keterangan = (EditText) findViewById(R.id.edt_keterangan);
        spin_jenis = (Spinner) findViewById(R.id.spinner3);
        list_spin_jenis = new ArrayList<String>();

        //getting value from intent
        nik = getIntent().getStringExtra("nik");
        tgl_update = getIntent().getStringExtra("tgl_update");
        ket_lama = getIntent().getStringExtra("ket");
        jenis_lama = getIntent().getStringExtra("jenis");
        user = getIntent().getStringExtra("user");
        tgl = getIntent().getStringExtra("tgl");
        if (tgl.equals("yes")){
            tgl_dari = getIntent().getStringExtra("tgl_dari");
            tgl_sampai = getIntent().getStringExtra("tgl_sampai");
        }

        //set value before update
        t_nik.setText(nik);
        t_tanggal.setText(tgl_update);
        t_keterangan.setText(ket_lama);

        //fetching jenis ijin
        fetchPosts_jenis();
    }

    //update button clicked
    public void updateijin(View view){
        ket_baru = t_keterangan.getText()+"";
        jenis_baru = (spin_jenis.getSelectedItemPosition()+1)+"";

        new MaterialDialog.Builder(view.getContext())
                .title("Konfirmasi")
                .content("Update data ijin dengan NIK "+nik+" ?")
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

    //dialog delete
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:

                    if(nik.equals(null)){
                        Toast.makeText(AdminUpdateIjin.this, "NIK "+nik+" Tidak Valid", Toast.LENGTH_SHORT).show();
                    }
                    else{
                       }


                case DialogInterface.BUTTON_NEGATIVE:
                    //cancel deletion
                    //Toast.makeText(AdminDeletePegawai.this, "Proses penghapusan dihentikan", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    //fetching jenis_ijin procedure
    private void fetchPosts_jenis() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT_jenis, onPostsLoaded_jenis, onPostsError_jenis){
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded_jenis = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            //Get JsonArray
            //getting jenis_ijin, fetch to spinner jenis
            List<JenisIjin> jenis = Arrays.asList(gson.fromJson(response,JenisIjin[].class));;
            int pos = Integer.parseInt(jenis_lama);
            for (JenisIjin b : jenis) {
                list_spin_jenis.add(b.getDESKRIPSI());
            }
            ArrayAdapter adapter_jenis = new ArrayAdapter(AdminUpdateIjin.this, android.R.layout.simple_spinner_item, list_spin_jenis);
            spin_jenis.setAdapter(adapter_jenis);
            spin_jenis.setSelection(pos-1);
        }
    };

    private final Response.ErrorListener onPostsError_jenis = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
        }
    };

    //updating procedure
    private void fetchPosts_update() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT_update, onPostsLoaded_update, onPostsError_update){
            @Override
            protected Map<String, String> getParams() {
                //send data from user to web service
                Map<String, String> params = new HashMap<>();
                params.put("nik", nik);
                params.put("tgl", tgl_update);
                params.put("ket", ket_baru);
                params.put("jenis", jenis_baru);
                return params;
            }
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded_update = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Intent m = new Intent(AdminUpdateIjin.this, AdminIjin.class);
            if(tgl.equals("yes")){
                m.putExtra("nik", nik);
                m.putExtra("tgl","yes");
                m.putExtra("tgl_dari",tgl_dari);
                m.putExtra("tgl_sampai",tgl_sampai);
            }
            else{
                m.putExtra("tgl","no");
                m.putExtra("nik", nik);
            }
            startActivity(m);
            AdminUpdateIjin.this.finish();
            Toast.makeText(AdminUpdateIjin.this, "Data Ijin dengan NIK  "+nik+" tertanggal "+tgl_update+" berhasil diupdate", Toast.LENGTH_SHORT).show();
        }
    };

    private final Response.ErrorListener onPostsError_update= new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
            Toast.makeText(AdminUpdateIjin.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AdminUpdateIjin.this, AdminSearchIzin.class);
        i.putExtra("crud","u");
        i.putExtra("user",user);
        startActivity(i);
        finish();
    }

    public void logout(View view){
        Intent i = new Intent(AdminUpdateIjin.this, Login_Activity.class);
        startActivity(i);
        AdminUpdateIjin.this.finish();
    }

}
