package com.example.offset.json.Other;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.offset.json.AdminAbsen.AdminAbsen;
import com.example.offset.json.AdminAbsen.InsertAbsen;
import com.example.offset.json.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText edt_username, edt_password, edt_nik, edt_nama;
    String user, pass, nik, nama;
    private RequestQueue requestQueue;
    private Gson gson;
    private static String ENDPOINT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //link to web service
        ENDPOINT = "http://"+getResources().getString(R.string.ip)+"/KP/insertLogin";

        //requirement volley, gson
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();

        //initialize component
        edt_username = (EditText) findViewById(R.id.txt_username);
        edt_password = (EditText) findViewById(R.id.txt_password);
        edt_nik = (EditText) findViewById(R.id.txt_nik);
        edt_nama = (EditText) findViewById(R.id.txt_nama);
    }

    public void register(View view){
        user = edt_username.getText()+"";
        pass = edt_password.getText()+"";
        nik = edt_nik.getText()+"";
        nama = edt_nama.getText()+"";
        View focusView = null;

        if (edt_username.getText().length()==0){
            edt_username.requestFocus();
            focusView = edt_username;
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_username));
            Toast.makeText(RegisterActivity.this,"Data tidak lengkap. Cek kembali!",Toast.LENGTH_SHORT).show();
        }
        else if(edt_password.getText().length()==0){
            edt_password.requestFocus();
            focusView = edt_password;
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_password));
            Toast.makeText(RegisterActivity.this,"Data tidak lengkap. Cek kembali!",Toast.LENGTH_SHORT).show();
        }
        else if(edt_nik.getText().length()==0){
            edt_nik.requestFocus();
            focusView = edt_nik;
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_nik));
            Toast.makeText(RegisterActivity.this,"Data tidak lengkap. Cek kembali!",Toast.LENGTH_SHORT).show();
        }
        else if(edt_nama.getText().length()==0){
            edt_nama.requestFocus();
            focusView = edt_nama;
            YoYo.with(Techniques.Shake)
                    .duration(500)
                    .playOn(findViewById(R.id.txt_nama));
            Toast.makeText(RegisterActivity.this,"Data tidak lengkap. Cek kembali!",Toast.LENGTH_SHORT).show();
        }
        else{
            new MaterialDialog.Builder(view.getContext())
                    .title("Konfirmasi")
                    .content("Buat akun baru ?")
                    .positiveText("Ya")
                    .negativeText("Tidak")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(RegisterActivity.this, Login_Activity.class);
        startActivity(i);
        finish();
    }

    public void logout(View view){
        Intent i = new Intent(RegisterActivity.this, Login_Activity.class);
        startActivity(i);
        RegisterActivity.this.finish();
    }

    //insertion procedure
    private void fetchPosts() {
        StringRequest request = new StringRequest(Request.Method.POST, ENDPOINT, onPostsLoaded, onPostsError){
            @Override
            protected Map<String, String> getParams() {
                //sending data to web service
                Map<String, String> params = new HashMap<>();
                params.put("user", user);
                params.put("pass", pass);
                params.put("nik", nik);
                params.put("nama", nama);
                return params;
            }
        };
        requestQueue.add(request);
    }

    private final Response.Listener<String> onPostsLoaded = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            Toast.makeText(RegisterActivity.this, "Akun baru berhasil dibuat. Silahkan login", Toast.LENGTH_LONG).show();
            Intent back = new Intent(RegisterActivity.this, Login_Activity.class);
            startActivity(back);
            RegisterActivity.this.finish();
        }
    };

    private final Response.ErrorListener onPostsError = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e("MainActivity", error.toString());
            Toast.makeText(RegisterActivity.this,"Koneksi gagal. Cek koneksi ke server!",Toast.LENGTH_SHORT).show();
        }
    };


}
