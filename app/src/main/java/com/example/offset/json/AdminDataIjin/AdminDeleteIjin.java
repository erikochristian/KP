package com.example.offset.json.AdminDataIjin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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
import com.example.offset.json.Adapter.AdapterIjin;
import com.example.offset.json.Other.Login_Activity;
import com.example.offset.json.R;
import com.example.offset.json.model.Izin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminDeleteIjin extends AppCompatActivity {
    private static String ENDPOINT;
    private static String ENDPOINT_delete;
    private RequestQueue requestQueue;
    private Gson gson;
    String nik = "";
    String tgl_delete="",user="";
    String tgl = "";
    String tgl_dari, tgl_sampai;
    List<Izin> ijin = null;
    private ListView listview, lv;
    TextView t_nik, t_tanggal, t_jenis, t_keterangan, t_welcome, t_welcome_nik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_delete_ijin);

        //Link to web service
        ENDPOINT_delete = "http://"+getResources().getString(R.string.ip)+"/KP/deleteIjin";

        //initialize component
        t_nik = (TextView) findViewById(R.id.text_nik);
        t_tanggal = (TextView) findViewById(R.id.text_tanggal);
        t_jenis = (TextView) findViewById(R.id.text_jenis);
        t_keterangan = (TextView) findViewById(R.id.text_keterangan);
        t_welcome = (TextView) findViewById(R.id.text_welcome);
        t_welcome_nik = (TextView) findViewById(R.id.text_welcome_nik);
        listview = (ListView) findViewById(R.id.list);

        //geting nik searched
        nik = getIntent().getStringExtra("nik");
        tgl = getIntent().getStringExtra("tgl");
        user = getIntent().getStringExtra("user");

        //Link to web service
        if(tgl.equals("yes")){
            tgl_dari = getIntent().getStringExtra("tgl_dari");
            tgl_sampai = getIntent().getStringExtra("tgl_sampai");
            ENDPOINT = "http://"+getResources().getString(R.string.ip)+"/KP/cekIjinTanggal";
        }
        else{
            ENDPOINT = "http://"+getResources().getString(R.string.ip)+"/KP/cekIjin";
        }

        //volley & gson requirement
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        //fetching data from web service
        fetchPosts();

        //when list item clicked, get dialog
        lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                //getting nik & tgl for delete
                Izin i = new Izin();
                i = ijin.get(position);
                nik = i.getNIK();
                tgl_delete = i.getTGL();
                new MaterialDialog.Builder(v.getContext())
                        .title("Konfirmasi")
                        .content("Hapus data ijin dengan NIK = "+nik+" ?")
                        .positiveText("Ya")
                        .negativeText("Tidak")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                fetchPosts_delete();
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
        });
    }

    //loading data list using nik
    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT, onPostsLoaded, onPostsError){
            //loading data where nik = nik
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                if(tgl_dari!=null && tgl_sampai!=null){
                    params.put("id", nik);
                    params.put("tgl_dari", tgl_dari);
                    params.put("tgl_sampai", tgl_sampai);
                }
                else{
                    params.put("id", nik);
                }
                return params;
            }
        };
        requestQueue.add(request);
    }

    //fetching data into list
    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try{
                ijin = Arrays.asList(gson.fromJson(response, Izin[].class));
                if(ijin.size()>0){
                    // set nik, nama data onto welcome text
                    t_welcome.setText("Daftar Ijin "+ijin.get(0).getNAMA());
                    t_welcome_nik.setText("NIK "+ijin.get(0).getNIK());

                }
                //fetch data ijin onto list using Class AdapterIjin (custom adapter)
                AdapterIjin customAdapterIjin = new AdapterIjin(AdminDeleteIjin.this, R.layout.item_ijin, ijin);
                listview.setAdapter(customAdapterIjin);
            }
            //Exception nik not found
            catch (Exception e){
                Log.e("Error", e.toString());
                Toast.makeText(AdminDeleteIjin.this, "NIK Tidak ditemukan", Toast.LENGTH_LONG).show();
                Intent o = new Intent(AdminDeleteIjin.this, AdminSearchIzin.class);
                startActivity(o);
                AdminDeleteIjin.this.finish();
            }
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
        }
    };

    //deletion procedure
    private void fetchPosts_delete() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT_delete, onPostsLoaded_delete, onPostsError_delete){
            @Override
            protected Map<String, String> getParams() {
                //throwing nik, tgl_delete to web service
                Map<String, String> params = new HashMap<>();
                params.put("id", nik);
                params.put("tgl",tgl_delete);
                return params;
            }
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded_delete = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Toast.makeText(AdminDeleteIjin.this, "Data Ijin dengan NIK  "+nik+" tertanggal "+tgl_delete+" berhasil dihapus", Toast.LENGTH_SHORT).show();
            fetchPosts();
        }
    };

    private final Response.ErrorListener onPostsError_delete = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
            Toast.makeText(AdminDeleteIjin.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AdminDeleteIjin.this, AdminSearchIzin.class);
        i.putExtra("crud","d");
        i.putExtra("user",user);
        startActivity(i);
        finish();
    }

    public void logout(View view){
        Intent i = new Intent(AdminDeleteIjin.this, Login_Activity.class);
        startActivity(i);
        AdminDeleteIjin.this.finish();
    }
}
