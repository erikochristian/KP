package com.example.offset.json.Other;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.offset.json.Adapter.AdapterPayroll;
import com.example.offset.json.AdminAbsen.PageViewAbsen;
import com.example.offset.json.R;
import com.example.offset.json.User.UserMain;
import com.example.offset.json.model.ReportLembur;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RecapPayroll extends AppCompatActivity {

    private static  String ENDPOINT;
    private RequestQueue requestQueue;
    private Gson gson;
    TextView t_totallembur,t_totalharian,t_totalpotongizin,t_totalpotongterlambat,t_umr, t_detail,t_total;
    String nik="",tgl_dari="",tgl_sampai="",nama="",user;
    List<ReportLembur> lemburs = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recap_payroll);

        ENDPOINT = "http://"+getResources().getString(R.string.ip)+"/KP/cekReportLembur";

        //getting value from intent
        user= getIntent().getStringExtra("user");
        nik = getIntent().getStringExtra("nik");
        tgl_dari = getIntent().getStringExtra("tgl1");
        tgl_sampai = getIntent().getStringExtra("tgl2");
        t_detail = (TextView) findViewById(R.id.text_detail_rekap);
        t_totallembur = (TextView) findViewById(R.id.text_lembur_rekap);
        t_totalharian = (TextView) findViewById(R.id.text_hadir_rekap);
        t_umr = (TextView) findViewById(R.id.text_umr_rekap);
        t_totalpotongizin = (TextView) findViewById(R.id.text_izin_rekap);
        t_totalpotongterlambat = (TextView) findViewById(R.id.text_terlambat_rekap);
        t_total = (TextView) findViewById(R.id.text_total_rekap);


        //requirement for volley and gson
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
        fetchPosts();

    }

    public void search_payroll(View view){
        Intent i = new Intent(RecapPayroll.this, ViewPayroll.class);
        i.putExtra("nik",nik);
        i.putExtra("tgl1",tgl_dari);
        i.putExtra("tgl2",tgl_sampai);
        i.putExtra("user",user);
        startActivity(i);
        this.finish();
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
                lemburs = Arrays.asList(gson.fromJson(response, ReportLembur[].class));
                nama = lemburs.get(0).getNama();
                t_detail.setText(nik+" - "+nama);
                t_umr.setText("Rp. "+lemburs.get(0).getUMR()+".00");
                t_totallembur.setText("Rp. "+lemburs.get(lemburs.size()-1).getTotalLembur());
                t_totalharian.setText("Rp. "+lemburs.get(lemburs.size()-1).getTotalHarian());
                t_totalpotongizin.setText("Rp. "+lemburs.get(lemburs.size()-1).getTotalPotongIzin());
                t_totalpotongterlambat.setText("Rp. "+lemburs.get(lemburs.size()-1).getTotalPotongTerlambat());
                float umr = Float.parseFloat(lemburs.get(0).getUMR());
                float lembur = Float.parseFloat(lemburs.get(lemburs.size()-1).getTotalLembur());
                float harian = Float.parseFloat(lemburs.get(lemburs.size()-1).getTotalHarian());
                float izin = Float.parseFloat(lemburs.get(lemburs.size()-1).getTotalPotongIzin());
                float terlambat = Float.parseFloat(lemburs.get(lemburs.size()-1).getTotalPotongTerlambat());
                float total = umr + lembur + harian - izin - terlambat;
                String totals = total+"";
                t_total.setText("Rp. "+totals);
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
            Toast.makeText(RecapPayroll.this,"Data tidak ditemukan.",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(RecapPayroll.this, SearchPayroll.class);
            i.putExtra("user",user);
            startActivity(i);
            finish();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(RecapPayroll.this, SearchPayroll.class);
        i.putExtra("user",user);
        startActivity(i);
        finish();

    }
    public void logout(View view){
        Intent i = new Intent(RecapPayroll.this, Login_Activity.class);
        startActivity(i);
        RecapPayroll.this.finish();
    }
}
