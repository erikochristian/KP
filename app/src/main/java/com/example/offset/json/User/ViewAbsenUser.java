package com.example.offset.json.User;

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
import com.example.offset.json.Adapter.AdapterReportAbsen;
import com.example.offset.json.AdminAbsen.PageViewAbsen;
import com.example.offset.json.Other.Login_Activity;
import com.example.offset.json.R;
import com.example.offset.json.model.ReportAbsen;
import com.example.offset.json.model.ReportLembur;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewAbsenUser extends AppCompatActivity {
    String nik="",tgl_dari="",tgl_sampai="",user="";
    private static  String ENDPOINT;
    private RequestQueue requestQueue;
    private Gson gson;
    TextView t_nik, t_tanggal_dari, t_tanggal_sampai, t_goljam,t_keterangan;
    ListView listview,lv;
    List<ReportLembur> absens = null;
    List<ReportLembur> lemburs = null;
    String NIK_ABSEN = "-", TMSK = "-", TKLR = "-", JMSK = "-", JKLR = "-";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_absen_user);

        //getting value from intent
        nik = getIntent().getStringExtra("nik");
        tgl_dari = getIntent().getStringExtra("tgl1");
        tgl_sampai = getIntent().getStringExtra("tgl2");
        user = getIntent().getStringExtra("user");
        ENDPOINT = "http://"+getResources().getString(R.string.ip)+"/KP/cekReportLembur";
        listview = (ListView) findViewById(R.id.list);

        //requirement for volley and gson
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        fetchPosts();
    }
    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT, onPostsLoaded, onPostsError){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("nik", nik);
                params.put("tgl1", tgl_dari);
                params.put("tgl2", tgl_sampai);
                return params;
            }
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try{
                absens = Arrays.asList(gson.fromJson(response, ReportLembur[].class));
                AdapterReportAbsen customAdapterAbsen = new AdapterReportAbsen(ViewAbsenUser.this, R.layout.item_report_absen, absens);
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
            Toast.makeText(ViewAbsenUser.this,"Data tidak ditemukan.",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(ViewAbsenUser.this, SearchAbsenUser.class);
            i.putExtra("user",user);
            i.putExtra("fungsi","r");
            startActivity(i);
            finish();
        }
    };
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ViewAbsenUser.this, SearchAbsenUser.class);
        i.putExtra("user",user);
        i.putExtra("fungsi","r");
        startActivity(i);
        finish();
    }

    public void logout(View view){
        Intent i = new Intent(ViewAbsenUser.this, Login_Activity.class);
        startActivity(i);
        ViewAbsenUser.this.finish();
    }
}
