package com.example.offset.json.AdminAbsen;

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
import com.example.offset.json.Adapter.AdapterAbsen;
import com.example.offset.json.Other.Login_Activity;
import com.example.offset.json.R;
import com.example.offset.json.model.Absen;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteAbsen extends AppCompatActivity {
    String searchby="",nik="",tgl_dari="",tgl_sampai="",tmsk="",tklr="";
    private static  String ENDPOINT, ENDPOINT_delete;
    private RequestQueue requestQueue;
    private Gson gson;
    TextView t_nik, t_tanggal_dari, t_tanggal_sampai, t_goljam;
    ListView listview,lv;
    List<Absen> absens = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_absen);

        //initiate components
        t_nik = (TextView) findViewById(R.id.text_nik_absen);
        t_tanggal_dari = (TextView) findViewById(R.id.text_tglmsk_absen);
        t_tanggal_sampai = (TextView) findViewById(R.id.text_tglklr_absen);
        t_goljam = (TextView) findViewById(R.id.text_goljam_absen);
        listview = (ListView) findViewById(R.id.list);

        //link to web service
        ENDPOINT_delete = "http://"+getResources().getString(R.string.ip)+"/KP/deleteAbsen";
        nik = getIntent().getStringExtra("nik");
        tgl_dari = getIntent().getStringExtra("tgl_dari");
        tgl_sampai = getIntent().getStringExtra("tgl_sampai");
        ENDPOINT = "http://"+getResources().getString(R.string.ip)+"/KP/cekAbsenNikTgl";


        Log.i("AAAAA",searchby+nik+tgl_dari+tgl_sampai+ENDPOINT);

        //requirement for volley and gson
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        fetchPosts();

        //when list is clicked, get the data, go to adminupdate for updating process
        lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Absen i = new Absen();
                i = absens.get(position);
                nik = i.getNIK();
                tmsk = i.getTMSK();
                tklr = i.getTKLR();
                new MaterialDialog.Builder(v.getContext())
                        .title("Konfirmasi")
                        .content("Hapus data Absen dengan NIK = "+nik+" dan tanggal masuk = "+tmsk+" ?")
                        .positiveText("Ya")
                        .negativeText("Tidak")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                if(nik.equals("") || tklr.equals("") || tmsk.equals("")){
                                    Toast.makeText(DeleteAbsen.this, "Data Tidak Valid", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    fetchPosts_delete();
                                }
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

    //dialog delete
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    if(nik.equals("") || tklr.equals("") || tmsk.equals("")){
                        Toast.makeText(DeleteAbsen.this, "Data Tidak Valid", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        //delete data ijin
                        fetchPosts_delete();
                        Toast.makeText(DeleteAbsen.this, "Data Absen dengan NIK  "+nik+" dan Tanggal masuk  "+tmsk+" berhasil dihapus", Toast.LENGTH_SHORT).show();
                    }

                    //loading data again after deletion
                    fetchPosts();

                case DialogInterface.BUTTON_NEGATIVE:
                    //cancel deletion
                    //Toast.makeText(DeleteAbsen.this, "Proses penghapusan dihentikan", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT, onPostsLoaded, onPostsError){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                    params.put("nik", nik);
                    params.put("tgl_dari", tgl_dari);
                    params.put("tgl_sampai", tgl_sampai);

                return params;
            }
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try{
                absens = Arrays.asList(gson.fromJson(response, Absen[].class));
                AdapterAbsen customAdapterAbsen = new AdapterAbsen(DeleteAbsen.this, R.layout.item_absen, absens);
                listview.setAdapter(customAdapterAbsen);

            }
            catch (Exception e){
                Log.e("Error", e.toString());

            }
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
            Toast.makeText(DeleteAbsen.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();

        }
    };

    //deletion procedure
    private void fetchPosts_delete() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT_delete, onPostsLoaded_delete, onPostsError_delete){
            @Override
            protected Map<String, String> getParams() {
                //throwing nik, tgl_delete to web service
                Map<String, String> params = new HashMap<>();
                params.put("nik", nik);
                params.put("tmsk", tmsk);
                params.put("tklr", tklr);
                return params;
            }
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded_delete = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Toast.makeText(DeleteAbsen.this, "Data Absen dengan NIK  "+nik+" dan Tanggal masuk  "+tmsk+" berhasil dihapus", Toast.LENGTH_SHORT).show();
            fetchPosts();
        }
    };

    private final Response.ErrorListener onPostsError_delete = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
            Toast.makeText(DeleteAbsen.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(DeleteAbsen.this, SearchAbsen.class);
        i.putExtra("crud","d");
        startActivity(i);
        finish();
    }

    public void logout(View view){
        Intent i = new Intent(DeleteAbsen.this, Login_Activity.class);
        startActivity(i);
        DeleteAbsen.this.finish();
    }
}

