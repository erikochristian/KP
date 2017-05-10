package com.example.offset.json.AdminDataPegawai;

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
import com.example.offset.json.Adapter.AdapterKaryawan;
import com.example.offset.json.Other.AdminMain;
import com.example.offset.json.Other.Login_Activity;
import com.example.offset.json.R;
import com.example.offset.json.model.Post;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListPegawai extends AppCompatActivity {

    private static  String ENDPOINT, ENDPOINT_DELETE;
    private RequestQueue requestQueue;
    private Gson gson;
    ListView listview,lv;
    List<Post> karyawans = null;
    String cari, crud = "", searchby = "", nik = "",user="";
    TextView txt_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_pegawai);

        cari = getIntent().getStringExtra("cari");
        searchby = getIntent().getStringExtra("searchby");
        crud = getIntent().getStringExtra("crud");
        user = getIntent().getStringExtra("user");

        txt_title = (TextView) findViewById(R.id.textView);

        if (cari.length() == 0){
            cari = "";
        }

        listview = (ListView) findViewById(R.id.list);

        ENDPOINT_DELETE = "http://"+getResources().getString(R.string.ip)+"/KP/deleteKaryawan";
        if (searchby.equals("nama")){
            ENDPOINT = "http://"+getResources().getString(R.string.ip)+"/KP/cekcarinama";
        }
        else{
            ENDPOINT = "http://"+getResources().getString(R.string.ip)+"/KP/cekNikKaryawan";
        }

        if (crud.equals("s")){
            txt_title.setText("CARI DATA PEGAWAI");
        }
        else if(crud.equals("d")){
            txt_title.setText("HAPUS DATA PEGAWAI");
        }
        else if(crud.equals("u")){
            txt_title.setText("UPDATE DATA PEGAWAI");
        }


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

                Post post = new Post();
                post =  karyawans.get(position);
                nik = post.getNIK();

                if (crud.equals("s")){
                    Intent intent = new Intent(ListPegawai.this, PageView.class);
                    intent.putExtra("nik", nik);
                    intent.putExtra("searchby",searchby);
                    intent.putExtra("cari",cari);
                    intent.putExtra("user",user);
                    startActivity(intent);
                    finish();
                }
                else if(crud.equals("d")){

                    new MaterialDialog.Builder(v.getContext())
                            .title("Konfirmasi")
                            .content("Hapus data karyawan dengan NIK = "+nik+" ?")
                            .positiveText("Ya")
                            .negativeText("Tidak")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    //delete data using nik
                                    if(nik.equals(null)){
                                        Toast.makeText(ListPegawai.this, "NIK = "+nik+" Tidak Valid", Toast.LENGTH_LONG).show();
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
                else if(crud.equals("u")){
                    Intent intent = new Intent(ListPegawai.this, AdminUpdatePegawai.class);
                    intent.putExtra("nik", nik);
                    intent.putExtra("searchby",searchby);
                    intent.putExtra("cari",cari);
                    intent.putExtra("user",user);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    //Dialog confirmation
    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:


                    // cancel delete
                case DialogInterface.BUTTON_NEGATIVE:

            }
        }
    };

    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT, onPostsLoaded, onPostsError){
            @Override
            protected Map<String, String> getParams() {
                //send nik searched to web service
                Map<String, String> params = new HashMap<>();
                params.put("cari", cari);
                return params;
            }
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try{
                karyawans = Arrays.asList(gson.fromJson(response, Post[].class));
                AdapterKaryawan customAdapterKaryawan = new AdapterKaryawan(ListPegawai.this, R.layout.item_pegawai, karyawans);
                listview.setAdapter(customAdapterKaryawan);
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
            Toast.makeText(ListPegawai.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();
            Intent o = new Intent(ListPegawai.this, MainActivity.class);
            o.putExtra("crud",crud);
            startActivity(o);
            ListPegawai.this.finish();
        }
    };

    //deletion procedure
    private void fetchPosts_delete() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT_DELETE, onPostsLoaded_delete, onPostsError_delete){
            @Override
            protected Map<String, String> getParams() {
                //throw nik for deletion
                Map<String, String> params = new HashMap<>();
                params.put("nik", nik);
                return params;
            }
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded_delete = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Toast.makeText(ListPegawai.this, "Karyawan dengan NIK = "+nik+" berhasil dihapus", Toast.LENGTH_LONG).show();
            Intent i = new Intent(ListPegawai.this, AdminPegawai.class);
            startActivity(i);
        }
    };

    private final Response.ErrorListener onPostsError_delete = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
            Toast.makeText(ListPegawai.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();
            Intent o = new Intent(ListPegawai.this, MainActivity.class);
            o.putExtra("crud",crud);
            startActivity(o);
            ListPegawai.this.finish();
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ListPegawai.this, MainActivity.class);
        i.putExtra("crud",crud);
        i.putExtra("user",user);
        startActivity(i);
        finish();
    }

    public void logout(View view){
        Intent i = new Intent(ListPegawai.this, Login_Activity.class);
        startActivity(i);
        ListPegawai.this.finish();
    }
}
