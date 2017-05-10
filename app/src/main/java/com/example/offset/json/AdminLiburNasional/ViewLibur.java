package com.example.offset.json.AdminLiburNasional;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.offset.json.Adapter.AdapterLibur;
import com.example.offset.json.AdminAbsen.PageViewAbsen;
import com.example.offset.json.Other.AdminMain;
import com.example.offset.json.Other.Login_Activity;
import com.example.offset.json.R;
import com.example.offset.json.model.LiburNasional;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewLibur extends AppCompatActivity {
    private static  String ENDPOINT;
    private static String ENDPOINT_delete;
    private RequestQueue requestQueue;
    private Gson gson;
    private ListView listview, lv;
    TextView t_tanggal,t_keterangan;
    String tgl_edit, ket_edit;
    List<LiburNasional> libur = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_libur);
        ENDPOINT = "http://"+getResources().getString(R.string.ip)+"/KP/cekLibur";
        ENDPOINT_delete = "http://"+getResources().getString(R.string.ip)+"/KP/deleteLibur";

        t_tanggal = (TextView) findViewById(R.id.text_tanggal);
        t_keterangan = (TextView) findViewById(R.id.text_keterangan);
        listview = (ListView) findViewById(R.id.list);

        //requirement for volley and gson
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        fetchPosts();

        //when list item clicked, get dialog
        lv = (ListView) findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                //getting nik & tgl for delete
                LiburNasional l = new LiburNasional();
                l = libur.get(position);
                tgl_edit = l.getTanggal();
                ket_edit = l.getKeterangan();

                //get dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Libur Nasional "+ l.getKeterangan()+" Tanggal "+tgl_edit).setPositiveButton("Update", dialogClickListener)
                        .setNegativeButton("Delete", dialogClickListener).show();

            }
        });
    }

    //dialog delete
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                {
                    Intent m = new Intent(ViewLibur.this, UpdateLibur.class);
                    m.putExtra("tgl", tgl_edit);
                    m.putExtra("ket", ket_edit);
                    startActivity(m);
                    ViewLibur.this.finish();
                }
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                {
                    //get dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewLibur.this);
                    builder.setMessage("Delete hari libur tanggal "+tgl_edit+"?").setPositiveButton("Ya", confirmationdialog)
                            .setNegativeButton("Tidak", confirmationdialog).show();
                }
                   break;
            }
        }
    };

    //dialog delete
    DialogInterface.OnClickListener confirmationdialog = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                {
                    fetchPosts_delete();
                    Toast.makeText(ViewLibur.this,"Hari libur tanggal "+tgl_edit+" telah dihapus", Toast.LENGTH_SHORT);
                    fetchPosts();
                }
                break;

                case DialogInterface.BUTTON_NEGATIVE:
                {
                    Toast.makeText(ViewLibur.this,"Proses delete dihentikan", Toast.LENGTH_SHORT);
                }
                break;
            }
        }
    };


    public void insert_libur(View view) {
        Intent m = new Intent(ViewLibur.this, InsertLibur.class);
        startActivity(m);
        ViewLibur.this.finish();
    }


    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT, onPostsLoaded, onPostsError){
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try{
                libur = Arrays.asList(gson.fromJson(response, LiburNasional[].class));

                AdapterLibur customadapter = new AdapterLibur(ViewLibur.this, R.layout.item_libur, libur);
                listview.setAdapter(customadapter);
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
        }
    };

    private void fetchPosts_delete() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT_delete, onPostsLoaded_delete, onPostsError_delete){
            @Override
            protected Map<String, String> getParams() {
                //throwing nik, tgl_delete to web service
                Map<String, String> params = new HashMap<>();
                params.put("tgl",tgl_edit);
                return params;
            }
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded_delete = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            fetchPosts();
        }
    };

    private final Response.ErrorListener onPostsError_delete = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
            Toast.makeText(ViewLibur.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();

        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ViewLibur.this, AdminMain.class);
        startActivity(i);
        finish();
    }

    public void logout(View view){
        Intent i = new Intent(ViewLibur.this, Login_Activity.class);
        startActivity(i);
        ViewLibur.this.finish();
    }
}
