package com.example.offset.json.AdminAbsen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.offset.json.Adapter.AdapterAbsen;
import com.example.offset.json.AdminDataPegawai.PageView;
import com.example.offset.json.Other.Login_Activity;
import com.example.offset.json.R;
import com.example.offset.json.model.Absen;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageViewAbsen extends AppCompatActivity {
    String searchby="",nik="",tgl_dari="",tgl_sampai="";
    private static  String ENDPOINT;
    private RequestQueue requestQueue;
    private Gson gson;
    TextView t_nik, t_tanggal_dari, t_tanggal_sampai, t_goljam;
    ListView listview,lv;
    List<Absen> absens = null;
    String NIK_ABSEN = "-", SFT = "-", TMSK = "-", TKLR = "-", JMSK = "-", JKLR = "-", TR = "-", TR2 = "-";
    String MESIN = "-", UPDT = "-", GOLJAM = "-", SSL = "-", JKLR2 = "-";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_view_absen);

        t_nik = (TextView) findViewById(R.id.text_nik_absen);
        t_tanggal_dari = (TextView) findViewById(R.id.text_tglmsk_absen);
        t_tanggal_sampai = (TextView) findViewById(R.id.text_tglklr_absen);
        t_goljam = (TextView) findViewById(R.id.text_goljam_absen);
        listview = (ListView) findViewById(R.id.list);
        nik = getIntent().getStringExtra("nik");
        tgl_dari = getIntent().getStringExtra("tgl_dari");
        tgl_sampai = getIntent().getStringExtra("tgl_sampai");
        ENDPOINT = "http://"+getResources().getString(R.string.ip)+"/KP/cekAbsenNikTgl";


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

                if(i.getNIK()!=null){
                    NIK_ABSEN = i.getNIK();
                }
                if(i.getSFT()!=null){
                    SFT = i.getSFT();
                }
                if(i.getTMSK()!=null){
                    TMSK = i.getTMSK();
                }
                if(i.getTKLR()!=null){
                    TKLR = i.getTKLR();
                }
                if(i.getGOLJAM()!=null){
                    GOLJAM = i.getGOLJAM();
                }
                if(i.getJMSK()!=null){
                    JMSK = i.getJMSK();
                }
                if(i.getJKLR()!=null){
                    JKLR = i.getJKLR();
                }
                if(i.getTR()!=null){
                    TR = i.getTR();
                }
                if(i.getTR2()!=null){
                    TR2 = i.getTR2();
                }
                if(i.getMESIN()!=null){
                    MESIN = i.getMESIN();
                }
                if(i.getUPDT()!=null){
                    UPDT = i.getUPDT();
                }
                if(i.getSSL()!=null){
                    SSL = i.getSSL();
                }
                if(i.getJKLR2()!=null){
                    JKLR2 = i.getJKLR2();
                }

                Intent intent = new Intent(PageViewAbsen.this, PageViewDetil.class);
                intent.putExtra("NIK",NIK_ABSEN);
                intent.putExtra("SFT",SFT);
                intent.putExtra("TMSK",TMSK);
                intent.putExtra("TKLR",TKLR);
                intent.putExtra("GOLJAM",GOLJAM);
                intent.putExtra("JMSK",JMSK);
                intent.putExtra("JKLR",JKLR);
                intent.putExtra("TR",TR);
                intent.putExtra("TR2",TR2);
                intent.putExtra("MESIN",MESIN);
                intent.putExtra("UPDT",UPDT);
                intent.putExtra("SSL",SSL);
                intent.putExtra("JKLR2",JKLR2);
                intent.putExtra("nik",nik);
                intent.putExtra("tgl_dari",tgl_dari);
                intent.putExtra("tgl_sampai",tgl_sampai);
                startActivity(intent);
                PageViewAbsen.this.finish();
            }
        });
    }

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
                AdapterAbsen customAdapterAbsen = new AdapterAbsen(PageViewAbsen.this, R.layout.item_absen, absens);
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
            Toast.makeText(PageViewAbsen.this,"Data tidak ditemukan.",Toast.LENGTH_SHORT).show();
            Intent i = new Intent(PageViewAbsen.this, SearchAbsen.class);
            i.putExtra("crud","s");
            startActivity(i);
            finish();

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(PageViewAbsen.this, SearchAbsen.class);
        i.putExtra("crud","s");
        startActivity(i);
        finish();
    }

    public void logout(View view){
        Intent i = new Intent(PageViewAbsen.this, Login_Activity.class);
        startActivity(i);
        PageViewAbsen.this.finish();
    }
}
