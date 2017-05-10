package com.example.offset.json.AdminDataIjin;

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

public class AdminSearchUpdateIjin extends AppCompatActivity {
    //THIS ACTIVITY SHOW DATA IJIN FROM ONE PEGAWAI, TO SELECT WHICH DATA TO UPDATE

    private static String ENDPOINT ;
    private RequestQueue requestQueue;
    private Gson gson;
    String nik = "";
    String tgl_delete="",user="";
    String ket,jenis;
    String tgl = "";
    String tgl_dari, tgl_sampai;
    List<Izin> ijin = null;
    private ListView listview, lv;
    TextView t_nik, t_tanggal, t_jenis, t_keterangan, t_welcome, t_welcome_nik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_search_update_ijin);

        //Init component
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

        //gson, volley requirement
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        //fetch data ijin procedure called
        fetchPosts();

        //when list is clicked, get the data, go to adminupdate for updating process
        lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Izin i = new Izin();
                i = ijin.get(position);
                nik = i.getNIK();
                tgl_delete = i.getTGL();
                ket = "";
                if(i.getKET()!=null){
                    ket = i.getKET();
                }
                else{
                    ket = "-";
                }
                jenis = i.getJENIS();
                Intent intent = new Intent(AdminSearchUpdateIjin.this, AdminUpdateIjin.class);
                intent.putExtra("nik", nik);
                intent.putExtra("tgl_update", tgl_delete);
                intent.putExtra("jenis",jenis);
                intent.putExtra("user",user);
                intent.putExtra("ket", ket);
                if (tgl.equals("yes")){
                    intent.putExtra("tgl","yes");
                    intent.putExtra("tgl_dari",tgl_dari);
                    intent.putExtra("tgl_sampai",tgl_sampai);
                }
                else{
                    intent.putExtra("tgl","no");
                }
                startActivity(intent);
                AdminSearchUpdateIjin.this.finish();
            }
        });
    }

    //fetching data ijin procedure
    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT, onPostsLoaded, onPostsError){
            @Override
            protected Map<String, String> getParams() {
                //send nik searched to web service
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
                //getting data from web service
                ijin = Arrays.asList(gson.fromJson(response, Izin[].class));
                if(ijin.size()>0){
                    t_welcome.setText("Daftar Ijin "+ijin.get(0).getNAMA());
                    t_welcome_nik.setText("NIK "+ijin.get(0).getNIK());

                }
                //fetch data onto list
                AdapterIjin customAdapterIjin = new AdapterIjin(AdminSearchUpdateIjin.this, R.layout.item_ijin, ijin);
                listview.setAdapter(customAdapterIjin);
            }
            catch (Exception e){
                Log.e("Error", e.toString());
                Toast.makeText(AdminSearchUpdateIjin.this, "NIK Tidak ditemukan", Toast.LENGTH_LONG).show();
                Intent o = new Intent(AdminSearchUpdateIjin.this, AdminIjin.class);
                startActivity(o);
                AdminSearchUpdateIjin.this.finish();
            }
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
            Toast.makeText(AdminSearchUpdateIjin.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(AdminSearchUpdateIjin.this, AdminSearchIzin.class);
        i.putExtra("crud","u");
        i.putExtra("user",user);
        startActivity(i);
        finish();
    }

    public void logout(View view){
        Intent i = new Intent(AdminSearchUpdateIjin.this, Login_Activity.class);
        startActivity(i);
        AdminSearchUpdateIjin.this.finish();
    }

}
