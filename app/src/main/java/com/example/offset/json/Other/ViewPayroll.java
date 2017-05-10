package com.example.offset.json.Other;

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
import com.example.offset.json.Adapter.AdapterPayroll;
import com.example.offset.json.AdminAbsen.PageViewAbsen;
import com.example.offset.json.R;
import com.example.offset.json.model.ReportAbsen;
import com.example.offset.json.model.ReportLembur;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewPayroll extends AppCompatActivity {

    String nik="",tgl_dari="",tgl_sampai="",nama="",user;
    private static  String ENDPOINT;
    private RequestQueue requestQueue;
    private Gson gson;
    TextView t_totallembur,t_totalharian,t_totalpotongizin,t_totalpotongterlambat,t_keterangan, t_detail;
    ListView listview,lv;
    List<ReportAbsen> absens = null;
    List<ReportLembur> lemburs = null;
    String NIK_ABSEN = "-", TMSK = "-", TKLR = "-", JMSK = "-", JKLR = "-";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_payroll);

        //getting value from intent
        user = getIntent().getStringExtra("user");
        nik = getIntent().getStringExtra("nik");
        tgl_dari = getIntent().getStringExtra("tgl1");
        tgl_sampai = getIntent().getStringExtra("tgl2");
        t_keterangan = (TextView) findViewById(R.id.text_ket_lem);
        t_detail = (TextView) findViewById(R.id.text_detail);
        t_totallembur = (TextView) findViewById(R.id.text_totallembur);
        t_totalharian = (TextView) findViewById(R.id.text_totalharian);
        t_totalpotongizin = (TextView) findViewById(R.id.text_totalpotongizin);
        t_totalpotongterlambat = (TextView) findViewById(R.id.text_totalPotongTerlambat);

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
                lemburs = Arrays.asList(gson.fromJson(response, ReportLembur[].class));
                AdapterPayroll customAdapterAbsen = new AdapterPayroll(ViewPayroll.this, R.layout.item_report_lembur, lemburs);
                listview.setAdapter(customAdapterAbsen);
                nama = lemburs.get(0).getNama();
                t_detail.setText(nik+" - "+nama+" - UMR : "+lemburs.get(1).getUMR());
                t_totallembur.setText(lemburs.get(lemburs.size()-1).getTotalLembur());
                t_totalharian.setText(lemburs.get(lemburs.size()-1).getTotalHarian());
                t_totalpotongizin.setText(lemburs.get(lemburs.size()-1).getTotalPotongIzin());
                t_totalpotongterlambat.setText(lemburs.get(lemburs.size()-1).getTotalPotongTerlambat());
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
            Toast.makeText(ViewPayroll.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ViewPayroll.this, RecapPayroll.class);
        i.putExtra("user",user);
        i.putExtra("nik",nik);
        i.putExtra("tgl1",tgl_dari);
        i.putExtra("tgl2",tgl_sampai);
        startActivity(i);
        finish();

    }
    public void logout(View view){
        Intent i = new Intent(ViewPayroll.this, Login_Activity.class);
        startActivity(i);
        ViewPayroll.this.finish();
    }
}
