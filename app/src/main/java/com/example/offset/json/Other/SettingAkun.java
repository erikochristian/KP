package com.example.offset.json.Other;

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
import com.example.offset.json.Adapter.AdapterAkun;
import com.example.offset.json.AdminAbsen.DeleteAbsen;
import com.example.offset.json.AdminAbsen.PageViewAbsen;
import com.example.offset.json.AdminAbsen.PageViewDetil;
import com.example.offset.json.AdminAbsen.SearchAbsen;
import com.example.offset.json.R;
import com.example.offset.json.model.Absen;
import com.example.offset.json.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingAkun extends AppCompatActivity {

    private static  String ENDPOINT, ENDPOINT_update, ENDPOINT_update2;
    private RequestQueue requestQueue;
    private Gson gson;
    TextView t_nik, t_nama, t_username, t_status;
    ListView listview,lv;
    List<User> users = null;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_akun);

        t_nik = (TextView) findViewById(R.id.text_akun_nik);
        t_nama = (TextView) findViewById(R.id.text_akun_nama);
        t_username = (TextView) findViewById(R.id.text_akun_username);
        t_status = (TextView) findViewById(R.id.text_akun_status);
        listview = (ListView) findViewById(R.id.list);

        ENDPOINT = "http://"+getResources().getString(R.string.ip)+"/KP/ReadAkun";
        ENDPOINT_update= "http://"+getResources().getString(R.string.ip)+"/KP/UpdateAkun";
        ENDPOINT_update2= "http://"+getResources().getString(R.string.ip)+"/KP/KunciAkun";

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
                User i = new User();
                i = users.get(position);
                username = i.getId();

                if (i.getStatus().equals("n")){
                    new MaterialDialog.Builder(v.getContext())
                            .title("Konfirmasi")
                            .content("Beri akses pada akun dengan username = "+username+"?")
                            .positiveText("Ya")
                            .negativeText("Tidak")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    fetchPosts_update();
                                    fetchPosts();
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
                else{
                    new MaterialDialog.Builder(v.getContext())
                            .title("Konfirmasi")
                            .content("Kunci akun dengan username = "+username+"?")
                            .positiveText("Ya")
                            .negativeText("Tidak")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    fetchPosts_update2();
                                    fetchPosts();
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

                }

        });

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
                users = Arrays.asList(gson.fromJson(response, User[].class));
                AdapterAkun customAdapterAkun = new AdapterAkun(SettingAkun.this, R.layout.item_akun, users);
                listview.setAdapter(customAdapterAkun);
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

    private void fetchPosts_update() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT_update, onPostsLoaded_update, onPostsError_update){
            @Override
            protected Map<String, String> getParams() {
                //throwing nik, tgl_delete to web service
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                return params;
            }
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded_update = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Toast.makeText(SettingAkun.this, "Akun "+username+" telah diberi hak akses.", Toast.LENGTH_SHORT).show();
            fetchPosts();
        }
    };

    private final Response.ErrorListener onPostsError_update = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
            Toast.makeText(SettingAkun.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();
        }
    };


    private void fetchPosts_update2() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT_update2, onPostsLoaded_update2, onPostsError_update2){
            @Override
            protected Map<String, String> getParams() {
                //throwing nik, tgl_delete to web service
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                return params;
            }
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded_update2 = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Toast.makeText(SettingAkun.this, "Akun "+username+" telah dikunci.", Toast.LENGTH_SHORT).show();
            fetchPosts();
        }
    };

    private final Response.ErrorListener onPostsError_update2 = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
            Toast.makeText(SettingAkun.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(SettingAkun.this, AdminMain.class);
        startActivity(i);
        finish();
    }

    public void logout(View view){
        Intent i = new Intent(SettingAkun.this, Login_Activity.class);
        startActivity(i);
        SettingAkun.this.finish();
    }
}
