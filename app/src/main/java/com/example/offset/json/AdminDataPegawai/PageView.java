package com.example.offset.json.AdminDataPegawai;

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
import com.example.offset.json.Other.Login_Activity;
import com.example.offset.json.R;
import com.example.offset.json.model.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class PageView extends AppCompatActivity {

    // static final String ENDPOINT = "https://kylewbanks.com/rest/posts.json";

    private static String ENDPOINT;
    private RequestQueue requestQueue;
    private Gson gson;

    TextView t_nik, t_nama, t_bagian, t_tgllahir,t_goljam, t_domisili, t_kelamin, t_agama, t_umr, t_spsi, t_jamsos, t_dsu, t_beras, t_bpjs;

    String nik = "",searchby = "",cari="",user="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_view);

        //WS Link
        ENDPOINT = "http://"+getResources().getString(R.string.ip)+"/KP/cekKaryawan";

        //Initialize component
        t_nik = (TextView) findViewById(R.id.text_nik);
        t_bagian = (TextView) findViewById(R.id.text_bagian);
        t_tgllahir = (TextView) findViewById(R.id.text_tgllahir);
        t_goljam = (TextView) findViewById(R.id.text_goljam);
        t_domisili = (TextView) findViewById(R.id.text_domisili);
        t_kelamin = (TextView) findViewById(R.id.text_kelamin);
        t_agama = (TextView) findViewById(R.id.text_agama);
        t_umr = (TextView) findViewById(R.id.text_umr);
        t_spsi = (TextView) findViewById(R.id.text_spsi);
        t_jamsos = (TextView) findViewById(R.id.text_jamsos);
        t_dsu = (TextView) findViewById(R.id.text_dsu);
        t_beras = (TextView) findViewById(R.id.text_beras);
        t_bpjs = (TextView) findViewById(R.id.text_bpjs);

        //getting nik from intent
        nik = getIntent().getStringExtra("nik");
        searchby = getIntent().getStringExtra("searchby");
        cari = getIntent().getStringExtra("cari");
        user = getIntent().getStringExtra("user");

        //volleh & gson requirement
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        //fetch procedure called
        fetchPosts();
    }

    //fetch procedure
    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT, onPostsLoaded, onPostsError){
            @Override
            protected Map<String, String> getParams() {
                //send nik searched to web service
                Map<String, String> params = new HashMap<>();
                params.put("nik", nik);
                return params;
            }
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            //getting the data from ws, input into textview
            try{
                Post post = gson.fromJson(response, Post.class);
                t_nik.setText(post.getNIK() + "  -  " + post.getNAMA().trim());
                t_bagian.setText(post.getBAGIAN());
                t_tgllahir.setText(post.getTGL_LHR());
                t_domisili.setText(post.getDOMISILI().trim());
                t_agama.setText(post.getAGAMA().trim());
                t_goljam.setText(post.getGOLJAMKER());
                t_umr.setText(post.getUMR());
                t_spsi.setText(post.getSPSI());
                t_jamsos.setText(post.getJAMSOS());
                t_dsu.setText(post.getDSU());
                t_beras.setText(post.getBERAS());
                t_bpjs.setText(post.getBPJS());
                if(post.getKELAMIN().equals("L"))t_kelamin.setText("LAKI-LAKI");
                else t_kelamin.setText("PEREMPUAN");

            }
            catch (Exception e){
                Toast.makeText(PageView.this, "NIK Tidak ditemukan", Toast.LENGTH_LONG).show();
                Intent o = new Intent(PageView.this, MainActivity.class);
                startActivity(o);
                PageView.this.finish();
            }
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
            Toast.makeText(PageView.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();
            Intent o = new Intent(PageView.this, MainActivity.class);
            o.putExtra("crud","s");
            startActivity(o);
            PageView.this.finish();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(PageView.this, ListPegawai.class);
        i.putExtra("crud","s");
        i.putExtra("cari",cari);
        i.putExtra("searchby",searchby);
        i.putExtra("user",user);
        startActivity(i);
        finish();
    }

    public void logout(View view){
        Intent i = new Intent(PageView.this, Login_Activity.class);
        startActivity(i);
        PageView.this.finish();
    }
}
