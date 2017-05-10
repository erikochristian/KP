package com.example.offset.json.AdminDataIjin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.offset.json.Adapter.AdapterIjin;
import com.example.offset.json.AdminAbsen.PageViewAbsen;
import com.example.offset.json.Other.Login_Activity;
import com.example.offset.json.R;
import com.example.offset.json.model.Izin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageViewIzin extends AppCompatActivity {
    private static  String ENDPOINT;
    private RequestQueue requestQueue;
    private Gson gson;
    String nik = "",tgl = "",user="";
    String tgl_dari, tgl_sampai;

    private ListView listview;

    TextView t_nik, t_tanggal, t_jenis, t_keterangan, t_welcome, t_welcome_nik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_view_izin);


        //Initialize other compoonent
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


        //requirement for volley and gson
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        Log.i("COBAA", ENDPOINT+tgl_dari+tgl_sampai+tgl);

        //Fetching data
        fetchPosts();
    }

    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT, onPostsLoaded, onPostsError){
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

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try{
                List<Izin> ijin = Arrays.asList(gson.fromJson(response, Izin[].class));
                if(ijin.size()>0){

                    t_welcome.setText("Daftar Ijin  "+ijin.get(0).getNAMA());
                    t_welcome_nik.setText("NIK "+ijin.get(0).getNIK());

                }
                AdapterIjin customAdapterIjin = new AdapterIjin(PageViewIzin.this, R.layout.item_ijin, ijin);
                listview.setAdapter(customAdapterIjin);


            }
            catch (Exception e){
                Log.e("Error", e.toString());
                Toast.makeText(PageViewIzin.this, "NIK Tidak ditemukan", Toast.LENGTH_LONG).show();
                Intent o = new Intent(PageViewIzin.this, AdminSearchIzin.class);
                startActivity(o);
                PageViewIzin.this.finish();
            }
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
            Toast.makeText(PageViewIzin.this,"Data tidak ditemukan.",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(PageViewIzin.this, AdminSearchIzin.class);
            i.putExtra("crud","s");
            i.putExtra("user",user);
            startActivity(i);
            finish();
        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(PageViewIzin.this, AdminSearchIzin.class);
        i.putExtra("crud","s");
        i.putExtra("user",user);
        startActivity(i);
        finish();
    }

    public void logout(View view){
        Intent i = new Intent(PageViewIzin.this, Login_Activity.class);
        startActivity(i);
        PageViewIzin.this.finish();
    }
}
